---
title: Make Creepers and Zombies drop tnt
---

There are two ways to go about doing this, depending on how you want the tnt to drop.  
You can either add them in a pool that's separate from the existing drops or do it in the same pool.

But what's the difference?  
In a loot table, each pool generates drops by choosing one or multiple entries from itself.  
Adding the tnt in a separate pool means that the tnt will *always* drop no matter what other drops are in the table.  
Adding the tnt to an existing pool means that the tnt is one of the options that the game may drop, among the normal drops.

## In a separate pool
```json
{
  "actions": [
    {
      "type": "loot-table-modifier:pool_add",
      "pools": [
        {
          "rolls": 1,
          "entries": [
            {
              "type": "minecraft:item",
              "name": "minecraft:tnt"
            }
          ]
        }
      ]
    }
  ],
  "predicate": {
    "type": "loot-table-modifier:table",
    "identifiers": [
      "minecraft:entities/creeper",
      "minecraft:entities/zombie"
    ]
  }
}
```

### Explanation

The action [`pool_add`](/reference/actions#add-pool) adds the pool to the matched tables.

The predicate [`table`](/reference/predicates#loot-table) matches either the creeper or zombie loot tables.

## In an existing pool
```json
{
  "actions": [
    {
      "type": "loot-table-modifier:entry_add",
      "entries": [
        {
          "type": "minecraft:item",
          "name": "minecraft:tnt"
        }
      ]
    }
  ],
  "predicate": {
    "type": "loot-table-modifier:table",
    "identifiers": [
      "minecraft:entities/creeper",
      "minecraft:entities/zombie"
    ]
  }
}
```

### Explanation

The action [`entry_add`](/reference/actions#add-entry) adds the entry to a pool.

The predicate [`table`](/reference/predicates#loot-table) matches either the creeper or zombie loot tables.

The action will only be applied on the first pool it finds in the target table, so you may want to use a predicate like below to specify which pool it should be added to.  
(Also, if adding some configuration to how many pools it would try matching or whatever would be useful for your use case, hit me up on [discord](https://discord.offsetmonkey538.top))
```json {"Creeper predicate, matches the pool dropping gunpowder":5-20} {"Zombie predicate, matches the pool dropping rotten flesh":22-37}
{
  "predicate": {
    "type": "loot-table-modifier:any_of",
    "terms": [

      {
        "type": "loot-table-modifier:all_of",
        "terms": [
          {
            "type": "loot-table-modifier:table",
            "identifiers": [
              "minecraft:entities/creeper"
            ]
          },
          {
            "type": "loot-table-modifier:entry_item",
            "name": "minecraft:gunpowder"
          }
        ]
      },
      

      {
        "type": "loot-table-modifier:all_of",
        "terms": [
          {
            "type": "loot-table-modifier:table",
            "identifiers": [
              "minecraft:entities/zombie"
            ]
          },
          {
            "type": "loot-table-modifier:entry_item",
            "name": "minecraft:rotten_flesh"
          }
        ]
      }
    ]
  }
}
```
