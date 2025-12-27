# Loot Table Modifier
[![Chat with me on Discord](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/social/discord-singular_vector.svg)](https://discord.offsetmonkey538.top/)
[![Available on Modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/mod/loot-table-modifier)  
[![Requires MonkeyLib538](https://raw.githubusercontent.com/OffsetMods538/MonkeyLib538/master/images/requires_monkeylib538.png)](https://modrinth.com/mod/monkeylib538)
[![Requires Fabric API](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/fabric-api_vector.svg)](https://modrinth.com/mod/fabric-api)

## Why?
Vanilla datapacks can only replace existing loot tables, which is not good for being compatible with other datapacks that also try to replace the same table.  
This mod fixes that by implementing a system of *loot modifiers*, which do just that, modify loot (tables)!

Unlike some other mods (*ahem* KubeJS) which work by running code at runtime to intercept and change drops, Loot Table Modifier modifies the loot tables as they're loaded from datapacks.  
This means that the mod will only hurt performance during data loading, but has no performance impact during gameplay!¹

###### ¹ Except when you make zombies drop a bajillion stacks of diamonds or something crazy like that, but that's really not the mod's fault 😅

## Usage
### Players
This mod does *nothing* when installed on its own. There needs to be some sort of datapack present that uses loot modifiers.  
If you are using some mod or datapack which depends on Loot Table Modifier, just download the latest version for the Minecraft version you use from the versions page and add it to your mods folder.

List of projects utilizing Loot Table Modifier:
- I don't know of any yet 😅 Please do hit me up on discord or something if you wanna add your project here :D

### Mod, Modpack or Datapack developers
Follow the documentation available [here](https://loot-table-modifier.docs.offsetmonkey538.top).

This mod is allowed to be used in Modrinth modpacks.  
I don't think I can legally prevent redistribution when using the MIT license, but I'd really appreciate if the mod wouldn't be redistributed as part of a modpack on other platforms.


###### This mod collects anonymous usage statistics, read more about what's collected [here](https://github.com/OffsetMods538/MonkeyMetrics-Server). No personal data is collected. This can be disabled by modifying the config at `config/monkeylib538/telemetry.json` or by running the `/monkeylib538 telemetry set isEnabled false` command.
