---
title: Loot Modifier
---
The mod uses loot modifiers to figure out how and what it should modify.  
Loot modifiers are JSON files located in datapacks at `data/namespace/loot-table-modifier/loot_modifier/modifier_name.json`.

Loot modifiers consist of two parts: the [actions](../actions) and a [predicate](../predicates).

During data loading, the mod looks through every existing loot table and its pools and entries.  
For every entry, pool and table, it will try matching the predicates of loot modifiers.  
When a loot modifier's predicate matches, its actions will be executed on the matched entry, pool or table.

The mod counts less specific predicates as matching everything underneath.  
This means that using the [`add_entry`](../actions#add-entry) action with the [`table`](../predicates#loot-table) predicate will add the entries to all pools in the matched table.  
The same applies in reverse; a more specific predicate also counts as matching everything above it, meaning that using the [`add_pool`](../actions#add-pool) action with the [`entry_item`](../predicates#item-entry) predicate will add the pools to all tables that contain a matching item entry.

## Generation
Loot modifiers can be generated using the misode generator available [here](https://misode-itd7xiyf1-misodes-projects.vercel.app/) under `Modded Generators`

Mod developers can use the `LootModifierProvider` datagen provider. Javadoc can be seen in-IDE or [here](https://maven.offsetmonkey538.top/#/releases/top/offsetmonkey538/loottablemodifier/loot-table-modifier).
