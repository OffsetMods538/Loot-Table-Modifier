---
title: Remove sticks
---

For removing an item from a loot table, the [`entry_remove`](todo) action should be used in most cases.  
Using [`pool_remove`](todo) would remove the whole pool, including all the other entries in there.

```json
{
  "actions": [
    {
      "type": "loot-table-modifier:entry_remove"
    }
  ],
  "predicate": {
    "type": "loot-table-modifier:entry_item",
    "name": "minecraft:stick"
  }
}
```

### Explanation

The action [`entry_remove`](../../reference/loot_modifier#add-pool) removes the matched entries from their pools.

The predicate [`entry_item`](../../reference/loot_modifier#loot-table) matches entries containing a stick.
