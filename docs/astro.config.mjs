// @ts-check
import { defineConfig } from 'astro/config';
import starlightSocialIcons from './src/utils/socialIcons';
import starlight from '@astrojs/starlight';

const site = 'https://docs.loot-table-modifier.pages.dev/';

// https://astro.build/config
export default defineConfig({
    integrations: [
        starlightSocialIcons({
            modrinth: "https://modrinth.com/mod/loot-table-modifier"
        }),
        starlight({
            title: 'Loot Table Modifier',
            logo: {
                src: './src/assets/face.svg'
            },
            customCss: [
                './src/styles/custom.css'
            ],
            components: {
                SocialIcons: './src/utils/SocialIcons.astro',
                PageFrame: './src/utils/PageFrame.astro'
            },
            social: [
                {icon: 'github', label: 'GitHub', href: 'https://github.com/OffsetMods538/Loot-Table-Modifier'},
                {icon: 'discord', label: 'Discord', href: 'https://discord.offsetmonkey538.top'}
            ],
            sidebar: [
                {
                    label: 'Guides',
                    items: [
                        {label: 'Setting up', slug: 'guides/setting_up'},
                        {
                            label: 'Examples',
                            items: [
                                {label: 'Replace any ingot with a diamond', slug: 'guides/examples/replace_ingot_w_diamond'},
                                //{label: 'Make Creepers and Zombies drop tnt', slug: 'guides/examples/creepers_and_zombies_drop_tnt'},
                                //{label: 'Remove sticks', slug: 'guides/examples/remove_sticks'},
                                //{label: 'Remove glowstone and gunpowder from witches', slug: 'guides/examples/remove_glowstone_and_gunpowder_witches'},
                            ],
                        },
                    ],
                },
                {
                    label: 'Reference',
                    items: [
                        {label: 'Loot Modifier', slug: 'reference/loot_modifier'},
                    ],
                },
            ],
            head: [
                {
                    tag: 'meta',
                    attrs: {
                        name: 'og:image',
                        content: `${site}og.png`
                    }
                },
                {
                    tag: 'meta',
                    attrs: {
                        name: 'twitter:image',
                        content: `${site}og.png`
                    }
                },
            ],
        }),
    ]
});
