---
title: Setting up
---

## Installing
To use Loot Table Modifier, you first gotta have it installed (I am so damn good at writing useful documentation, no need to thank me 👍)

When developing a plain datapack, all you need to do is download it from [modrinth](https://modrinth.com/mod/loot-table-modifier) and put it in your mods folder.  
Currently, as of v2 alpha 1, only **fabric** is supported. Running though [Sinytra Connector](https://modrinth.com/mod/connector) may or may not be possible (please do let me know on [discord](https://discord.offsetmonkey538.top) if you test it :D), but a NeoForge version is planned for the future.

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

Also add this to your `fabric.mod.json` file:
```json
{
  "depends": {
    // ...Others
    // This matches the major version of 2. I plan on following semver for future releases so it should be safe to depend on this.
    "loot-table-modifier": ">=2.0.0 <3.0.0"
  }
}
```


## Loot Modifiers

The mod uses loot modifiers to figure out how and what it should modify. Loot modifiers are located in a datapack at `data/namespace/loot-table-modifier/loot_modifier/` and must be JSON files.  
Full documentation on loot modifiers can be found [here](/reference/loot_modifier).

For mod developers, there's a datagen provider available with the name `LootModifierProvider`. I uhh... seem to have forgotten to write javadoc for that, but you can take a look at the included `LootTableModifierDatagen` class which I use for testing :D

For datapack makers (or mod developers who can't figure out datagen because there's no javadoc for it) there is also an online generator available [here](https://misode-itd7xiyf1-misodes-projects.vercel.app/) under `Modded Generators`.

## Development Mode

Development mode enables exporting modified loot tables for debugging and additional logging.  
Dev mode will automatically be enabled when Minecraft is launched from an IDE or can be enabled by setting the JVM property `lootTableModifierDev` to `true`.  
That can be done with Prism Launcher by going to `Settings`, selecting `Java` and then adding `-DlootTableModifierDev=true` into `JVM arguments`.

Mod developers who, for some weird reason, *don't* want their logs to be spammed with random unnecessary stuff can set the property to `false` to override the ide check.

Modified loot tables can be exported using the command `/loot-table-modifier debug export`.

## Badges
###### No need to use them if you don't want to
Badges are meant for developers to put on their project pages, so users can see that Loot Table Modifier is used or required.  
They're based on [Devin's Badges](https://github.com/intergrav/devins-badges).

The `requires` badge is meant for datapacks or mods which want to display that Loot Table Modifier is required for it to function.  
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
