---
title: Replace any Minecraft ingot with a diamond
---

The following loot modifier will replace any Minecraft ingot item with a diamond:
```json
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

The action [`entry_item_set`](/reference/actions#set-item-in-item-entry) replaces the item in an existing matched item entry.

The predicate [`entry_item`](/reference/predicates#item-entry) matches specific item entries based on their IDs. It can match using a [regex identifier](/reference/regex_identifier), which allows using regex patterns.  
The regex pattern `minecraft:.*_ingot` will match every item that has an identifier that begins with `minecraft:` and ends with `_ingot`. For example `minecraft:iron_ingot` and `minecraft:gold_ingot` will both be matched.
