// @ts-check
import { defineConfig } from 'astro/config';
import starlightSocialIcons from './src/utils/socialIcons';
import starlight from '@astrojs/starlight';

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
                        {label: 'About', slug: 'guides/about'},
                    ],
                },
                {
                    label: 'Reference',
                    items: [
                        //{label: 'About', slug: 'guides/about'},
                    ],
                },
            ],
        }),
    ]
});
