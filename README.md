# Loot Table Modifier

Allows datapacks (and thus mods as well) to modify loot tables.  
Kind of like a json wrapper around the Fabric API loot table modification api.

Also provides a datagen provider for creating loot table modifiers in mods.

An example json file:
```json5
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