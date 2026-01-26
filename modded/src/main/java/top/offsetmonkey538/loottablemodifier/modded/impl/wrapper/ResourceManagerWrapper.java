package top.offsetmonkey538.loottablemodifier.modded.impl.wrapper;

import it.unimi.dsi.fastutil.Pair;
import top.offsetmonkey538.loottablemodifier.common.api.wrapper.ResourceManager;
import top.offsetmonkey538.monkeylib538.common.api.wrapper.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record ResourceManagerWrapper(net.minecraft.server.packs.resources.ResourceManager vanillaIdentifier) implements ResourceManager {
    @Override
    public Stream<Pair<Identifier, Supplier<BufferedReader>>> listResources(String string, Predicate<String> predicate) {
        return vanillaIdentifier.listResources(string, id -> predicate.test(id.toString())).entrySet().stream()
                .map(entry -> Pair.of(
                        // The entry can contain either a ResourceLocation or Identifier, but that's fine cause this code never explicitly mentions either of those so compiling for whatever version will choose the correct thing.
                        Identifier.of(entry.getKey().toString()),
                        () -> {
                            try {
                                return entry.getValue().openAsReader();
                            } catch (IOException e) {
                                // Rethrow as runtime, otherwise can't be a supplier. Will be called in LootTableModifierCommon.loadModifiers which catches all exceptions
                                throw new RuntimeException(e);
                            }
                        }
                ));
    }
}

