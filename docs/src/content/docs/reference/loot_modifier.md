---
title: Loot Modifier
---

Loot modifiers are JSON files located in datapacks at `data/namespace/loot-table-modifier/loot_modifier`.

Loot modifiers consist of two parts: the actions and a predicate.

At data loading, the mod looks through every existing loot table and its pools and entries.  
For every entry, pool and table, it will try matching the predicates of every loot modifier.  
When a loot modifier's predicate matches, its actions will be executed on the matched entry, pool or table.

A predicate specifically matching a pool will also activate the action on all its entries.  
A predicate specifically matching an entry will also activate the action on the table and pool.

## Actions

Actions tell the mod how a matched loot table, pool or entry should be modified.

|                      |                                            |
|----------------------|--------------------------------------------|
| [`pool_add`]()       | Adds the provided pools to a matched table |
| [`pool_remove`]()    | Removes matched pools                      |
| [`entry_add`]()      | Adds the provided entry to matched pools   |
| [`entry_item_set`]() | Sets the item in a matched item entry      |

## Predicates

Predicates tell the mod which loot tables, pools or entries should be modified.

|                  |                                                   |
|------------------|---------------------------------------------------|
| `inverted`       | Inverts the result of the provided predicate      |
| `any_of`         | Matches when any of the provided predicates match |
| `all_of`         | Matches when all of the provided predicates match |
| [`entry_item`]() | Matches an item entry based on its identifier     |
| [`table`]()      | Matches a table based on its identifier or type   |
