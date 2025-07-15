---
title: Replace any Minecraft ingot with a diamond
---

The following loot modifier will replace any Minecraft ingot item with a diamond:
```json5
{
  "actions": {
    "type": "loot-table-modifier:entry_item_set",
    "name": "minecraft:diamond"
  },
  "predicates": {
    "type": "loot-table-modifier:entry_item",
    "name": {
      "regexPattern": "minecraft:.*_ingot"
    }
  }
}
```

### Explanation

The action [`entry_item_set`](TODO link to wherever reference for that will be) replaces the item in an existing matched item entry.

The predicate [`entry_item`](TODO link to wherever reference for that will be) matches specific item entries based on their IDs. It may also match using [regex](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html), which is done in this case.  
The regex pattern `minecraft:.*_ingot` will match every item that has an identifier that begins with `minecraft:` and ends with `_ingot`. For example `minecraft:iron_ingot` and `minecraft:gold_ingot` will both be matched.
