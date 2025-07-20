---
title: Remove glowstone and gunpowder from witches
---

For removing an item from a loot table, the [`entry_remove`](todo) action should be used in most cases.  
Using [`pool_remove`](todo) would remove the whole pool, including all the other entries in there.

```json {"First part matches the witch table":10-16} {"Second part matches either glowstone dust or gunpowder":18-31}
{
  "actions": [
    {
      "type": "loot-table-modifier:entry_remove"
    }
  ],
  "predicate": {
    "type": "loot-table-modifier:all_of",
    "terms": [
      
      {
        "type": "loot-table-modifier:table",
        "identifiers": [
          "minecraft:entities/witch"
        ]
      },
      
      
      {
        "type": "loot-table-modifier:any_of",
        "terms": [
          {
            "type": "loot-table-modifier:entry_item",
            "name": "minecraft:glowstone_dust"
          },
          {
            "type": "loot-table-modifier:entry_item",
            "name": "minecraft:gunpowder"
          }
        ]
      }
    ]
  }
}
```
