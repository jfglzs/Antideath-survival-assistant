package io.github.jfglzs.asa.utils;

import io.github.jfglzs.asa.AsaMod;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.*;

public class MixinManager implements IMixinConfigPlugin {
    private static final int MC_VERSION;
    private static Map<Int2BooleanFunction, List<String>> MIXIN_MAP = new HashMap<>();

    public static final Int2BooleanFunction REQUIRE_1211 = ver -> ver >= 1211;
    public static final Int2BooleanFunction REQUIRE_1214 = ver -> ver >= 1214;
    public static final Int2BooleanFunction REQUIRE_1218 = ver -> ver >= 1218;
    public static final Int2BooleanFunction REQUIRE_12110 = ver -> ver >= 12110;
    public static final Int2BooleanFunction REQUIRE_12111 = ver -> ver >= 12111;
    public static final Int2BooleanFunction REQUIRE_261 = ver -> ver >= 261;
    public static final Int2BooleanFunction REQUIRE_262 = ver -> ver >= 262;

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!mixinClassName.startsWith("io.github.jfglzs.asa.mixin.")) return true;
        for (Int2BooleanFunction ft : MIXIN_MAP.keySet()) {
            if (!ft.apply(MC_VERSION)) {
                for (String ClassName : MIXIN_MAP.get(ft)) {
                    if (mixinClassName.startsWith(ClassName)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    static {
        MC_VERSION = Integer.parseInt(AsaMod.MINECRAFT_VERSION.replace(".", ""));
        MIXIN_MAP.put(REQUIRE_1211, new ArrayList<>());
        MIXIN_MAP.put(REQUIRE_1214, new ArrayList<>());
        MIXIN_MAP.put(REQUIRE_1218, new ArrayList<>());
        MIXIN_MAP.put(REQUIRE_12110, new ArrayList<>());
        MIXIN_MAP.put(REQUIRE_12111, new ArrayList<>());
        MIXIN_MAP.put(REQUIRE_261, new ArrayList<>());
        MIXIN_MAP.put(REQUIRE_262, new ArrayList<>());

        putToMap(REQUIRE_1214, "feature.optItemModel.ModelManager_Mixin");
        putToMap(REQUIRE_1214, "feature.optItemModel.Identifier_Mixin");
        putToMap(REQUIRE_261, "feature.optItemModel.CuboidItemModelWrapper_Mixin");
        putToMap(REQUIRE_261, "feature.optItemModel.LayerRenderState_Mixin");
    }

    private static void putToMap(Int2BooleanFunction function, String className) {
        MIXIN_MAP.get(function).add("io.github.jfglzs.asa.mixin." + className);
    }
}
