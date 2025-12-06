---
title: Actions
---

:::note
Fields marked as `Optional` have their value set to the default ones.
:::

Actions tell the mod how a matched loot table, pool or entry should be modified.

Actions are json objects that consist of their identifier `type` and then any other fields specific to each action.  
Below is a list of all currently supported actions:

|                                                                 |                                                       |
|-----------------------------------------------------------------|-------------------------------------------------------|
| [`loot-table-modifier:pool_add`](#add-pool)                     | Adds the provided pools to matched tables             |
| [`loot-table-modifier:pool_remove`](#remove-pool)               | Removes matched pools                                 |
| [`loot-table-modifier:entry_add`](#add-entry)                   | Adds the provided entries to matched pools            |
| [`loot-table-modifier:entry_remove`](#remove-entry)             | Removes matched entries                               |
| [`loot-table-modifier:entry_item_set`](#set-item-in-item-entry) | Sets the item in matched item entries                 |
| [`loot-table-modifier:condition_add`](#add-condition)           | Adds the provided conditions to matched pools/entries |

### Add pool
```json
{
  "type": "loot-table-modifier:pool_add",
  "pools": [
    {
      /* Loot pool */
    },
    {
      /* Loot pool */
    }
  ]
}
```
This action adds the provided loot pools to matched tables.

See: [Make Creepers and Zombies drop tnt](/examples/creepers_and_zombies_drop_tnt)

### Remove pool
```json
{
  "type": "loot-table-modifier:pool_remove"
}
```
This action removes all matched pools.

### Add entry
```json
{
  "type": "loot-table-modifier:entry_add",
  "entries": [
    {
      /* Loot entry */
    },
    {
      /* Loot entry */
    }
  ]
}
```
This action adds the provided loot entries to matched pools.

See: [Make Creepers and Zombies drop tnt](/examples/creepers_and_zombies_drop_tnt)

### Remove entry
```json
{
  "type": "loot-table-modifier:entry_remove"
}
```
This action removes all matched entries.

See: [Remove sticks](/examples/remove_sticks), [Remove glowstone and gunpowder from witches](/examples/remove_glowstone_and_gunpowder_witches)

### Set item in item entry
```json {"    Optional":4-5}
{
  "type": "loot-table-modifier:entry_item_set",
  "name": /* Identifier of an item */
}
```
This action will replace the item in a matched item entry with the provided item.  
If the matched entry is not an item entry, an error will be raised at runtime. When development mode is enabled, the error also crashes the game.

See: [Replace any Minecraft ingot with a diamond](/examples/replace_ingot_w_diamond)

### Add condition
```json {"    Optional":11-12} {"    Optional":13-14}
{
  "type": "loot-table-modifier:condition_add",
  "conditions": [
    {
      /* Loot condition */
    },
    {
      /* Loot condition */
    }
  ],
  
  "includePools": true,
  
  "includeEntries": true
}
```
This action adds the provided loot conditions to matched pools and/or entries.  
If `includePools` is false, the condition won't be added to matched pools.  
If `includeEntries` is false, the condition won't be added to matched entries.  
By default, both `includePools` and `includeEntries` are true.

See: [Make squid drop ink only when killed by player](/examples/squid_drop_ink_from_player)
