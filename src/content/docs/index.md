---
title: Loot Table Modifier
template: splash
hero:
  tagline: Makes it possible for datapacks to modify loot tables, instead of just replacing them.
  image:
    file: ../../assets/icon.svg
  actions:
    - text: Get Started
      link: /getting_started/
      icon: right-arrow
    - text: View on Modrinth
      link: https://modrinth.com/mod/loot-table-modifier
      icon: external
      variant: minimal
    - text: Chat with me on Discord
      link: https://discord.offsetmonkey538.top
      icon: discord
      variant: minimal
---

# About

:::caution
This documentation is written for Loot Table Modifier v2 beta 1.  
Both the mod and this wiki are in development.  
If you encounter any issues or have suggestions, please *please* **please** come join my [discord](https://discord.offsetmonkey538.top) and yell at me about them.
:::


Vanilla datapacks can only replace existing loot tables, which is not good for being compatible with other datapacks that may also try to replace the same loot tables.  
This mod fixes that by implementing a system of *loot modifiers*, which do just that, modify loot (tables)!

Loot modifiers are in datapacks, meaning mods and modpacks are able to use them too!

Unlike some mods which replace stuff at runtime, Loot Table Modifier does all of its processing during data loading. This means that the mod will have no performance impact during gameplay! (Except of course if you make a zombie drop a bajillion stacks of diamonds, though that's not the mod's fault...)
