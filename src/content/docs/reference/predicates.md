---
title: Predicates
---

:::note
Predicates may use this thing I call a `RegexIdentifier`, which you can read about [here](../regex_identifier).
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
    /* Predicate */
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
      /* Predicate */
    },
    {
      /* Predicate */
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
      /* Predicate */
    },
    {
      /* Predicate */
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
```json {"    Optional":3-4} {"    Optional":5-6}
{
  "type": "loot-table-modifier:table",
  
  "identifiers": /* RegexIdentifier */,
  
  "types": /* RegexIdentifier */
}
```
This predicate will match loot tables based on their identifiers or types.
