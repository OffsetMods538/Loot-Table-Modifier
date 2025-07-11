// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

// https://astro.build/config
export default defineConfig({
    integrations: [
        starlight({
            title: 'Loot Table Modifier Docs',
            logo: {
                src: './src/assets/face.svg'
            },
            social: [
                {icon: 'github', label: 'GitHub', href: 'https://github.com/OffsetMods538/Loot-Table-Modifier'},
                {icon: 'discord', label: 'Discord', href: 'https://discord.offsetmonkey538.top'}
            ],
            sidebar: [
                {
                    label: 'Guides',
                    items: [
                        // Each item here is one entry in the navigation menu.
                        {label: 'About', slug: 'guides/about'},
                    ],
                },
                {
                    label: 'Reference',
                    autogenerate: {directory: 'reference'},
                },
            ],
        }),
    ]
});
