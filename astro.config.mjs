// @ts-check
import { defineConfig } from 'astro/config';
import starlightSocialIcons from './src/utils/socialIcons.ts';
import starlight from '@astrojs/starlight';

// https://astro.build/config
export default defineConfig({
    site: process.env.CUSTOM_SITE_ENV || undefined,
    base: process.env.CUSTOM_BASE_ENV || undefined,
    integrations: [
        starlightSocialIcons({
            modrinth: "https://modrinth.com/mod/loot-table-modifier"
        }),
        starlight({
            title: 'Loot Table Modifier',
            credits: true,
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
                {label: 'Getting Started', slug: 'getting_started'},
                {
                    label: 'Examples',
                    items: [
                        {label: 'Replace any ingot with a diamond', slug: 'examples/replace_ingot_w_diamond'},
                        {label: 'Make Creepers and Zombies drop tnt', slug: 'examples/creepers_and_zombies_drop_tnt'},
                        {label: 'Remove sticks', slug: 'examples/remove_sticks'},
                        {label: 'Remove glowstone and gunpowder from witches', slug: 'examples/remove_glowstone_and_gunpowder_witches'},
                    ],
                },
                {
                    label: 'Reference',
                    items: [
                        {label: 'Loot Modifier', slug: 'reference/loot_modifier'},
                        {label: 'Actions', slug: 'reference/actions'},
                        {label: 'Regex Identifier', slug: 'reference/regex_identifier'},
                        {label: 'Predicates', slug: 'reference/predicates'},
                        {label: 'Badges', slug: 'reference/badges'},
                    ],
                },
            ],
        }),
    ]
});
