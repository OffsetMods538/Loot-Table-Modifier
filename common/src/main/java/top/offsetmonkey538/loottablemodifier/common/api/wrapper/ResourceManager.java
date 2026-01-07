package top.offsetmonkey538.loottablemodifier.common.api.wrapper;

import it.unimi.dsi.fastutil.Pair;

import java.io.BufferedReader;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface ResourceManager {
    Stream<Pair<Identifier, Supplier<BufferedReader>>> listResources(String string, Predicate<String> predicate);
}
