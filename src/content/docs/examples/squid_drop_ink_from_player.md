---
title: Make squid drop ink only when killed by player
---

For making a drop only appear from a player kill, the [`condition_add`](/reference/actions#add-condition) action should be used to add the vanilla `killed_by_player` condition.

```json {"Only apply to entries, won't affect anything else in the pool":10-11} {"First part matches the squid table":17-23} {"Second part matches entries containing inc sac":25-29}
{
  "actions": [
    {
      "type": "loot-table-modifier:condition_add",
      "conditions": [
        {
          "condition": "minecraft:killed_by_player"
        }
      ],
      
      "includePools": false
    }
  ],
  "predicate": {
    "type": "loot-table-modifier:all_of",
    "terms": [
      
      {
        "type": "loot-table-modifier:table",
        "identifiers": [
          "minecraft:entities/squid"
        ]
      },
      
      
      {
        "type": "loot-table-modifier:entry_item", 
        "name": "minecraft:ink_sac"
      }
    ]
  }
}
```
