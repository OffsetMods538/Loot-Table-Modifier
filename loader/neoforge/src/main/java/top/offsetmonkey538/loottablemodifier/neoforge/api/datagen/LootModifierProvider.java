//package top.offsetmonkey538.loottablemodifier.neoforge.api.datagen;
//
//import com.mojang.serialization.Codec;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.data.PackOutput;
//import net.neoforged.neoforge.common.data.JsonCodecProvider;
//import top.offsetmonkey538.loottablemodifier.common.api.resource.LootModifier;
//
//import java.util.concurrent.CompletableFuture;
//
//import static top.offsetmonkey538.loottablemodifier.common.LootTableModifierCommon.MOD_ID;
//
//public abstract class LootModifierProvider extends JsonCodecProvider<LootModifier> {
//    public LootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
//        super(output, PackOutput.Target.DATA_PACK, MOD_ID + "/loot_modifier", LootModifier.CODEC, lookupProvider, modId);
//    }
//}
//