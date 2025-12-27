package top.offsetmonkey538.loottablemodifier.platform;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.ApiStatus;
import top.offsetmonkey538.loottablemodifier.api.wrapper.Identifier;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import static top.offsetmonkey538.loottablemodifier.LootTableModifierCommon.load;

public interface PlatformMain {
    @ApiStatus.Internal
    PlatformMain INSTANCE = load(PlatformMain.class);

    static void registerExamplePack() {
        INSTANCE.registerExamplePackImpl();
    }

    static void writeSorted(JsonWriter jsonWriter, JsonElement json) throws IOException {
        INSTANCE.writeSortedImpl(jsonWriter, json);
    }

    static Identifier id(String path) {
        return INSTANCE.idImpl(path);
    }

    static <T> Predicate<T> allOf(List<? extends Predicate<? super T>> predicates) {
        return INSTANCE.allOfImpl(predicates);
    }

    static <T> Predicate<T> anyOf(List<? extends Predicate<? super T>> predicates) {
        return INSTANCE.anyOfImpl(predicates);
    }

    void registerExamplePackImpl();
    void writeSortedImpl(JsonWriter jsonWriter, JsonElement json) throws IOException;
    Identifier idImpl(String path);
    <T> Predicate<T> allOfImpl(List<? extends Predicate<? super T>> predicates);
    <T> Predicate<T> anyOfImpl(List<? extends Predicate<? super T>> predicates);
}
