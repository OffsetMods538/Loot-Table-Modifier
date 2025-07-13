---
title: Setting up
---

## Installing
To use Loot Table Modifier, you first gotta have it installed (I am so good at writing useful documentation, I know 👍)

When developing a plain datapack, all you need to do is download it from [modrinth](https://modrinth.com/mod/loot-table-modifier) and put it in your mods folder.  
Currently, as of v2 alpha 1, only **fabric** is supported. Running though [Sinytra Connector](https://modrinth.com/mod/connector) may or may not be possible (please do let me know on [discord](https://discord.offsetmonkey538.top) if you test it :D), but a NeoForge version is planned for the future.

### Mods
Mods can include Loot Table Modifier as a JIJ dependency like this:
```groovy
repositories {
    // ...rest of repositories block
    maven {
        name = "OffsetMods538"
        url = "https://maven.offsetmonkey538.top/releases"
        content {
            includeGroup "top.offsetmonkey538.loottablemodifer"
        }
    }
}

dependencies {
    // ...rest of dependencies block
    
    include implementation("top.offsetmonkey538.loottablemodifier:loot-table-modifier:VERSION_HERE")
}
```
Make sure to replace `VERSION_HERE` with the actual version you want to use!


## Loot Modifiers

The mod uses Loot Modifiers to figure out what and how to modify.  
They are just JSON files containing stuff that will be explained in the examples.  
Loot modifiers are located in a datapack in `data/namespace/loot-table-modifier/loot_modifier/` (of course use your own namespace instead of `namespace`)

For mod developers, there's a datagen provider available with the name `LootModifierProvider`. I uhh... have forgotten to write javadoc for that, but you can look at the included `LootTableModifierDatagen` class (not part of api package) and as a TLDR: call `addModifier` with an id and figure out what you need to feed to the builder of `LootModifier` :D

For datapack makers (or mod developers who can't figure out datagen because there's no javadoc for it) there is also an online generator available [here](https://misode-2l52sq8cl-misodes-projects.vercel.app/) under `Modded Generators`.


## Badges
###### No need to use them if you don't want to
Badges are meant for developers to put on their project pages, so users can see that Loot Table Modifier is used or required.  
They're based on [Devin's Badges](https://github.com/intergrav/devins-badges).

The `requires` badge is meant for datapacks which want to display that Loot Table Modifier is required for the pack to function.  
It can be used in Markdown like this: `[![This project requires Loot Table Modifier to be installed](https://raw.githubusercontent.com/OffsetMods538/Loot-Table-Modifier/master/images/requires_badge.svg)](https://modrinth.com/mod/loot-table-modifier)`  
And will look like this:  
[![This project requires Loot Table Modifier to be installed](https://raw.githubusercontent.com/OffsetMods538/Loot-Table-Modifier/master/images/requires_badge.svg)](https://modrinth.com/mod/loot-table-modifier)  
Clicking it will bring the user to the Modrinth page for Loot Table Modifier

<br>

The `uses` badge is meant for mods or modpacks which want to display that Loot Table Modifier is included. (Youuuu probably won't display that at the top of your readme 😅)  
It can be used in Markdown like this: `[![This project includes Loot Table Modifier](https://raw.githubusercontent.com/OffsetMods538/Loot-Table-Modifier/master/images/uses_badge.svg)](https://modrinth.com/mod/loot-table-modifier)`  
And will look like this:  
[![This project includes Loot Table Modifier](https://raw.githubusercontent.com/OffsetMods538/Loot-Table-Modifier/master/images/uses_badge.svg)](https://modrinth.com/mod/loot-table-modifier)  
Clicking it will bring the user to the Modrinth page for Loot Table Modifier
