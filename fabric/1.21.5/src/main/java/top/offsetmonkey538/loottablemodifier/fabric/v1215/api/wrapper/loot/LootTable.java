package top.offsetmonkey538.loottablemodifier.fabric.v1215.api.wrapper.loot;

import java.util.ArrayList;
import java.util.List;

public interface LootTable {
    String getType();

    ArrayList<LootPool> getPools();
    void setPools(List<LootPool> entries);

    ArrayList<LootFunction> getFunctions();
    void setFunctions(List<LootFunction> functions);
}

