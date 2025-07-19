---
title: Regex Identifier
---

Identifiers (officially resource locations) are used all over Minecraft for identifying stuff like items, blocks, entities, loot tables and so on.  
They consist of a namespace and a path, separated by a colon. Full documentation on identifiers can be found [here](https://minecraft.wiki/w/Resource_location).

Loot modifier predicates often use `RegexIdentifier`s for matching these identifiers.  
A `RegexIdentifier` allows either matching a literal identifier directly or using a regex pattern.

Instead of matching an exact piece of text, regex allows describing a pattern to match many possible variations.  
For that, regex uses *metacharacters*. The most useful combination of which for matching identifiers is `.*`, which matches any character from zero to infinite times.  
This is very useful for identifiers because you can build patterns like `minecraft:.*_ingot` for matching any ingot from minecraft.

Full documentation supported regex syntax can be found [here](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html), and you can use a site like [regex101](https://regex101.com/) to write and test you regular expressions (just make sure to choose the `Java 8` flavor)

A `RegexIdentifier` in json can either be an inlined string or an object with the `regexPattern` field.  
Examples:
```json {"Matches only the exact identifier of minecraft:diamond_sword":2-3} {"Matches all minecraft sword types (minecraft:stone_sword, minecraft:iron_sword, etc)":5-8} {"Matches anything from minecraft":10-13} {"Matches any minecraft oak block/item, except for the stripped variants":15-18}
{
  
  "value1": "minecraft:diamond_sword",
  
  
  "value2": {
    "regexPattern": "minecraft:.*_sword"
  },
  
  
  "value3": {
    "regexPattern": "minecraft:.*"
  },
  
  
  "value4": {
    "regexPattern": "minecraft:oak_.*"
  }
}
```
