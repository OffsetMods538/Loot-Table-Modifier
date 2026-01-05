package top.offsetmonkey538.loottablemodifier.modded.v1201.impl.wrapper.loot.entry;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.LootCondition;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.loot.entry.LootPoolEntry;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.LootConditionWrapper;
import top.offsetmonkey538.loottablemodifier.modded.impl.wrapper.loot.entry.LootPoolEntryWrapper;
import top.offsetmonkey538.loottablemodifier.modded.v1201.codec.GSONCodec;

public final class LootPoolEntryCodecProviderImpl implements LootPoolEntry.CodecProvider {
    private static final Codec<LootPoolEntry> CODEC = new GSONCodec<>(Deserializers.createFunctionSerializer(), LootPoolEntryContainer.class)
            .xmap(LootPoolEntryWrapper::create, wrappedEntry -> ((LootPoolEntryWrapper) wrappedEntry).vanillaEntry);

    @Override
    public Codec<LootPoolEntry> get() {
        return CODEC;
    }
}
