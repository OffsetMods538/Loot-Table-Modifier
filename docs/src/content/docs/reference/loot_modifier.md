---
title: Loot Modifier
---

Loot modifiers are JSON files located in datapacks at `data/namespace/loot-table-modifier/loot_modifier`.

Loot modifiers consist of two parts: the actions and a predicate.

During data loading, the mod looks through every existing loot table and its pools and entries.  
For every entry, pool and table, it will try matching the predicates of loot modifiers.  
When a loot modifier's predicate matches, its actions will be executed on the matched entry, pool or table.

The mod counts less specific predicates as matching everything underneath.  
This means that using the [`add_entry`](#add-entry) action with the [`table`](#table) predicate will add the entries to all pools in the matched table.  
The same applies in reverse; a more specific predicate also counts as matching everything above it, meaning that using the [`add_pool`](#add-pool) action with the [`entry_item`](#item-entry) predicate will add the pools to all tables that contain a matching item entry.


:::note
I use the `?` character at the end of field names to signify optional fields. The value I add for optional fields in the default one. The field names do not actually have a `?` at the end!  
I also make use of comments in codeblocks to explain stuff, which you'll have to remove because Minecraft doesn't understand them.
```json
{
  // Range of 0 to 10
  "field1?": 1,
  // Range of 10 to 100
  "field2": 20
}
```
The default value of `field1` is `1`.
Both of the following snippets would be valid based on the above description:
```json
{
  "field2": 10
}
```
```json
{
  "field1": 2,
  "field2": 9
}
```
:::

## Actions

Actions tell the mod how a matched loot table, pool or entry should be modified.

Actions are json objects that consist of their identifier `type` and then any other fields specific to each action.  
Below is a list of all currently supported actions:

|                                                                 |                                            |
|-----------------------------------------------------------------|--------------------------------------------|
| [`loot-table-modifier:pool_add`](#add-pool)                     | Adds the provided pools to matched tables  |
| [`loot-table-modifier:pool_remove`](#remove-pool)               | Removes matched pools                      |
| [`loot-table-modifier:entry_add`](#add-entry)                   | Adds the provided entries to matched pools |
| [`loot-table-modifier:entry_item_set`](#set-item-in-item-entry) | Sets the item in matched item entries      |

### Add pool
```json
{
  "type": "loot-table-modifier:pool_add",
  "pools": [
    {
      // Loot pool, exactly the same as vanilla
    }
  ]
}
```
This action adds the provided loot pools to matched tables.

See: [todo](todo)

### Remove pool
```json
{
  "type": "loot-table-modifier:pool_remove"
}
```
This action removes all matched pools.

See: [todo](todo)

### Add entry
```json
{
  "type": "loot-table-modifier:entry_add",
  "entries": [
    {
      // Loot entry, exactly the same as vanilla
    }
  ]
}
```
This action adds the provided loot entries to matched pools.

See: [todo](todo)

### Set item in item entry
```json
{
  "type": "loot-table-modifier:entry_item_set",
  "name": /* Identifier of an item */,
  // Either true or false
  "canReplaceEntry?": false
}
```
This action will replace the item in a matched item entry with the provided item.  
If `canReplaceEntry` is enabled, any other matched entry will be replaced with an item entry containing the provided item.
By default, it will not replace other types of entries, but that can be enabled by setting `canReplaceEntry` to true.

See: [Replace any Minecraft ingot with a diamond](../../guides/examples/replace_ingot_w_diamond)

## Predicates

:::note
Predicates may use this thing I call a `RegexIdentifier`.

Identifiers are used all over Minecraft for identifying stuff like items (`minecraft:diamond_sword`), blocks (`minecraft:stone`), entities (`minecraft:zombie`), even loot tables (`minecraft:chests/end_city_treasure`) and so on.  
In short, a `RegexIdentifier` allows either matching an identifier directly or using a regex pattern for that. (you can view the supported regex syntax [here](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html))

A `RegexIdentifier` can either be an inlined string or an object with the `regexPattern` field.

In this case, only the exact identifier of `minecraft:diamond_sword` will be matched.
```json
{
  "coolValue": "minecraft:diamond_sword"
}
```

In this case, the regex pattern would match all the Minecraft swords (`minecraft:wooden_sword`, `minecraft:stone_sword`, etc.)
```json
{
  "coolValue": {
    "regexPattern": "minecraft:.*_sword"
  }
}
```
:::

Predicates tell the mod which loot tables, pools or entries should be modified.  
Different predicates can be combined using `invert`, `any_of` and `all_of`.

Below is a list of all supported predicates:

|                                                 |                                                   |
|-------------------------------------------------|---------------------------------------------------|
| [`loot-table-modifier:inverted`](#invert)       | Inverts the result of the provided predicate      |
| [`loot-table-modifier:any_of`](#any-of)         | Matches when any of the provided predicates match |
| [`loot-table-modifier:all_of`](#all-of)         | Matches when all of the provided predicates match |
| [`loot-table-modifier:entry_item`](#item-entry) | Matches an item entry based on its identifier     |
| [`loot-table-modifier:table`](#loot-table)      | Matches a table based on its identifier or type   |

### Invert
```json
{
  "type": "loot-table-modifier:inverted",
  "term": {
    // Another predicate
  }
}
```
This predicate will match when the provided predicate doesn't. A logical `NOT` operation.

### Any of
```json
{
  "type": "loot-table-modifier:any_of",
  "terms": [
    {
      // Another predicate
    },
    {
      // Yet another predicate
    }
  ]
}
```
This predicate will match when any of the provided predicates match. A logical `OR` operation.

### All of
```json
{
  "type": "loot-table-modifier:all_of",
  "terms": [
    {
      // Another predicate
    },
    {
      // Yet another predicate
    }
  ]
}
```
This predicate will match when all of the provided predicates match. A logical `AND` operation.

### Item Entry
```json
{
  "type": "loot-table-modifier:entry_item",
  "name": /* RegexIdentifier */
}
```
This predicate will match item entries based on their identifiers.

### Loot Table
```json
{
  "type": "loot-table-modifier:table",
  "identifiers?": /* RegexIdentifier */,
  "types?": /* RegexIdentifier */
}
```
This predicate will match loot tables based on their identifiers or types.
