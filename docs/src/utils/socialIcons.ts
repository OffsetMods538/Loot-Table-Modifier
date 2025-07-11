import { fileURLToPath } from 'node:url';
import type { AstroConfig, AstroIntegration, HookParameters, ViteUserConfig } from 'astro';
import { AstroError } from 'astro/errors';
import { dirname, resolve } from 'pathe';

type Plugin = NonNullable<ViteUserConfig['plugins']>[number];

export type Hooks = Required<Astro.IntegrationHooks>;

export const createResolver = (_base: string) => {
	let base = _base;
	if (base.startsWith('file://')) {
		base = dirname(fileURLToPath(base));
	}

	return {
		resolve: (...path: Array<string>) => resolve(base, ...path),
	};
};

export type HookUtility<THook extends keyof Hooks, TArgs extends Array<any>, TReturn> = (
	params: HookParameters<THook>,
	...args: TArgs
) => TReturn;

export const defineUtility =
	<THook extends keyof Hooks>(_hook: THook) =>
	/**
	 * The function itself
	 * @param {Function} fn;
	 */

	// biome-ignore lint/suspicious/noExplicitAny: <explanation>
	<TArgs extends Array<any>, T>(fn: HookUtility<THook, TArgs, T>): HookUtility<THook, TArgs, T> =>
		fn;

function getPluginNames(plugins: AstroConfig['vite']['plugins']) {
	const names: string[] = [];

	if (plugins) {
		for (const plugin of plugins) {
			if (!plugin) continue;

			if (Array.isArray(plugin)) {
				names.push(...getPluginNames(plugin));
				continue;
			}

			if (plugin instanceof Promise) {
				continue;
			}

			names.push(plugin.name);
		}
	}

	return names;
}

export const hasVitePlugin = defineUtility('astro:config:setup')(
	(
		{ config },
		{
			plugin,
		}: {
			plugin: string | Plugin;
		}
	): boolean => {
		if (!plugin || plugin instanceof Promise) return false;

		const currentPlugins = new Set(getPluginNames(config?.vite?.plugins));

		const plugins = new Set<string>();

		if (typeof plugin === 'string') {
			plugins.add(plugin);
		}

		if (typeof plugin === 'object') {
			if (Array.isArray(plugin)) {
				const names = new Set(
					getPluginNames(plugin as NonNullable<AstroConfig['vite']['plugins']>)
				);
				for (const name of names) plugins.add(name);
			} else {
				plugins.add(plugin.name);
			}
		}

		return [...plugins].some((name) => currentPlugins.has(name));
	}
);

export const addVitePlugin = defineUtility('astro:config:setup')(
	(
		params,
		{
			plugin,
			warnDuplicated = true,
		}: {
			plugin: Plugin;
			warnDuplicated?: boolean;
		}
	) => {
		const { updateConfig, logger } = params;

		if (warnDuplicated && hasVitePlugin(params, { plugin })) {
			const plug = plugin as Plugin;
			if (plug === null || !plug) return;
			logger.warn(
				`The Vite plugin "${
					// @ts-ignore
					plug.name
				}" is already present in your Vite configuration, this plugin may not behave correctly.`
			);
		}

		updateConfig({
			vite: {
				plugins: [plugin],
			},
		});
	}
);

type VirtualImport = {
	id: string;
	content: string;
	context?: 'server' | 'client' | undefined;
};

type Imports = Record<string, string> | Array<VirtualImport>;

const incrementPluginName = (name: string) => {
	let count = 1;
	return `${name.replace(/-(\d+)$/, (_, c) => {
		count = Number.parseInt(c) + 1;
		return '';
	})}-${count}`;
};

const resolveVirtualModuleId = <T extends string>(id: T): `\0${T}` => {
	return `\0${id}`;
};

const createVirtualModule = (
	name: string,
	_imports: Imports,
	bypassCoreValidation: boolean
): Plugin => {
	// We normalize the imports into an array
	const imports: Array<VirtualImport> = Array.isArray(_imports)
		? _imports
		: Object.entries(_imports).map(([id, content]) => ({
				id,
				content,
				context: undefined,
			}));

	// We check for virtual imports with overlapping contexts, eg. several imports
	// with the same id and context server
	const duplicatedImports: Record<string, Array<string>> = {};
	for (const { id, context } of imports) {
		duplicatedImports[id] ??= [];
		duplicatedImports[id]?.push(...(context === undefined ? ['server', 'client'] : [context]));
	}
	for (const [id, contexts] of Object.entries(duplicatedImports)) {
		if (contexts.length !== [...new Set(contexts)].length) {
			throw new AstroError(
				`Virtual import with id "${id}" has been registered several times with conflicting contexts.`
			);
		}
	}

	const resolutionMap = Object.fromEntries(
		imports.map(({ id }) => {
			if (!bypassCoreValidation && id.startsWith('astro:')) {
				throw new AstroError(
					`Virtual import name prefix can't be "astro:" (for "${id}") because it's reserved for Astro core.`
				);
			}

			return [resolveVirtualModuleId(id), id];
		})
	);

	return {
		name,
		resolveId(id) {
			if (imports.find((_import) => _import.id === id)) {
				return resolveVirtualModuleId(id);
			}
			return;
		},
		load(id, options) {
			const resolution = resolutionMap[id];
			if (resolution) {
				const context = options?.ssr ? 'server' : 'client';
				const data = imports.find(
					(_import) =>
						_import.id === resolution &&
						(_import.context === undefined || _import.context === context)
				);

				if (data) {
					return data.content;
				}
			}
			return;
		},
	};
};

export const addVirtualImports = defineUtility('astro:config:setup')(
	(
		params,
		{
			name,
			imports,
			__enableCorePowerDoNotUseOrYouWillBeFired = false,
		}: {
			name: string;
			imports: Imports;
			__enableCorePowerDoNotUseOrYouWillBeFired?: boolean;
		}
	) => {
		let pluginName = `vite-plugin-${name}`;

		while (hasVitePlugin(params, { plugin: pluginName }))
			pluginName = incrementPluginName(pluginName);

		addVitePlugin(params, {
			warnDuplicated: false,
			plugin: createVirtualModule(pluginName, imports, __enableCorePowerDoNotUseOrYouWillBeFired),
		});
	}
);

export function starlightSocialIcons(socials: {
	modrinth: string;
}): AstroIntegration {
	return {
		name: 'starlight-social-icon-augment',
		hooks: {
			'astro:config:setup': (params) => {
				addVirtualImports(params, {
					name: 'starlight-social-icon-augment',
					imports: {
						'virtual:starlight-social-icon-augment': `
                            export const socials = ${JSON.stringify(socials)}
                        `,
					},
				});
			},
		},
	};
}

export default starlightSocialIcons;