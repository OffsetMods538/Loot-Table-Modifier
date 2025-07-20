---
title: Getting Started
---

## Installing
:::note
Currently, as of v2 alpha 1, only **fabric** is supported. Running though [Sinytra Connector](https://modrinth.com/mod/connector) may or may not be possible (please do let me know on [discord](https://discord.offsetmonkey538.top) if you test it :D), but a NeoForge version is planned for the future.
:::

To use Loot Table Modifier, you first gotta have it installed (I am so damn good at writing useful documentation, no need to thank me 👍)

When developing a plain datapack, all you need to do is download it from [modrinth](https://modrinth.com/mod/loot-table-modifier) and put it in your mods folder.  

You may also want to show the `Requires` badge in your readme, see below for an example and [here](#badges) for more info.  
[![This project requires Loot Table Modifier to be installed](https://raw.githubusercontent.com/OffsetMods538/Loot-Table-Modifier/master/images/requires_badge.svg)](https://modrinth.com/mod/loot-table-modifier)

### Mod developers
Mods can depend on Loot Table Modifier like this:
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
    
    implementation "top.offsetmonkey538.loottablemodifier:loot-table-modifier:VERSION_HERE"
}
```
Make sure to replace `VERSION_HERE` with the actual version you want to use!  
See my maven page [here](https://maven.offsetmonkey538.top/#/releases/top/offsetmonkey538/loottablemodifier/loot-table-modifier) for all available versions and their javadocs.

Also add this to your `fabric.mod.json` file:
```json {"      This matches the major version of 2":3-4}
{
  "depends": {
    
    "loot-table-modifier": ">=2.0.0 <3.0.0"
  }
}
```

## Development Mode

Development mode enables additional logging and the ability to export modified loot tables.

Dev mode will automatically be enabled when Minecraft is launched from an IDE or can be enabled by setting the JVM property `lootTableModifierDev` to `true`.  
That can be done with Prism Launcher by going to `Settings`, selecting `Java` and then adding `-DlootTableModifierDev=true` into `JVM arguments`.

Mod developers who, for some weird reason, *don't* want their logs to be spammed with random unnecessary stuff can set the property to `false` to override the IDE check.

Modified loot tables can then be exported using the command `/loot-table-modifier debug export`.

## Next Up
Take a look at how loot modifiers work [here](/reference/loot_modifier) and then take a look at the examples.
