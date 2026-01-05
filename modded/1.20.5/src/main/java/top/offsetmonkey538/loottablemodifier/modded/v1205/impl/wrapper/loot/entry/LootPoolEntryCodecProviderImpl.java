package top.offsetmonkey538.loottablemodifier.modded.v1205.impl.wrapper.loot.entry;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.entry.LootPoolEntryWrapper;

public final class LootPoolEntryCodecProviderImpl implements LootPoolEntry.CodecProvider {
    @Override
    public Codec<LootPoolEntry> get() {
        return LootPoolEntries.CODEC.xmap(LootPoolEntryWrapper::create, wrappedEntry -> ((LootPoolEntryWrapper) wrappedEntry).vanillaEntry);
    }
}
