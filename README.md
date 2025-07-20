# Loot Table Modifier
[![discord-singular](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/social/discord-singular_vector.svg)](https://discord.offsetmonkey538.top/)
[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/mod/loot-table-modifier)  
[![Requires Fabric API](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/fabric-api_vector.svg)](https://modrinth.com/mod/fabric-api)

todo: good readme for alpha version

Allows datapacks to modify loot tables, instead of just overwriting them.  
Version 2 of the mod is currently in alpha. v2 adds more ways to modify loot tables than just adding to them  
v2 will be backwards-compatible with v1 modifiers, so no need to worry about them breaking.

If you want to use v1, then see the original description below.
If you do decide to try out the alpha version of v2 (please do), then please go ahead and read the documentation [here](https://loot-table-modifier.docs.offsetmonkey538.top/) and if you encounter any problems, have suggestions for new actions/predicates or just want to say literally anything about the mod, please please please join my discord and tell me about it. I want to make this as good as I can and any sort of feedback really helps.  



## Original Description

Allows datapacks (and thus mods as well) to add to loot tables, instead of just overwriting them.

This mod shouldn't impact performance while playing the game, but only when datapacks are reloading (joining a world, starting a server, `/reload` command, whatever else).  
Performance impact during pack reloading varies depending on the datapacks.  
The mod writes how long applying modifiers took in the console.

Also provides a datagen provider for creating loot table modifiers in mods.

A modifier json file includes two components:
1. `"modifies"` - string or array, which defines the loot tables to modify. For example `"minecraft:entities/creeper"` or `"minecraft:chests/abandoned_mineshaft"`
2. `"pools"` - array of loot pools. This works exactly the same as the `"pools"` in a vanilla loot table, thus you can generate a loot table online with a tool like [misode.github.io](https://misode.github.io/loot-table/) and copy over the `"pools"` from the generated json. (I may fork it and add a generator for specifically this mod in the future™)  
An example json file:
```json5
// example_pack/data/example/loot-table-modifier/loot_modifier/drop_tnt.json
{
    // Can also be a single identifier without an array
    // "modifies": "minecraft:entities/creeper",
    "modifies": [
        "minecraft:entities/creeper",
        "minecraft:entities/zombie"
    ],
    "pools": [
        {
            "bonus_rolls": 0.0,
            "entries": [
                {
                    "type": "minecraft:item",
                    "functions": [
                        {
                            "add": false,
                            "count": {
                                "type": "minecraft:uniform",
                                "max": 1.0,
                                "min": 0.0
                            },
                            "function": "minecraft:set_count"
                        }
                    ],
                    "name": "minecraft:tnt"
                }
            ],
            "rolls": 1.0
        }
    ]
}
```

Depend on inside mod:
```groovy
repositories {
    // ...
    maven {
        name = "OffsetMods538"
        url = "https://maven.offsetmonkey538.top/releases"
        content {
            includeGroup "top.offsetmonkey538.loottablemodifier"
        }
    }
}


dependencies {
    // ...
    modImplementation "top.offsetmonkey538.loottablemodifier:loot-table-modifier:1.0.1+1.21.1"
}
```
