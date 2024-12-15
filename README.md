# Loot Table Modifier
[![discord-singular](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/social/discord-singular_vector.svg)](https://discord.offsetmonkey538.top/)
[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/mod/loot-table-modifier)  
[![Requires Fabric API](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/fabric-api_vector.svg)](https://modrinth.com/mod/fabric-api)

Allows datapacks (and thus mods as well) to add to loot tables, instead of just overwriting them.

Also provides a datagen provider for creating loot table modifiers in mods.

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
    "loot_pools": [
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
    modImplementation "top.offsetmonkey538.loottablemodifier:loot-table-modifier:1.0.0+1.21.1"
}
```