package owca.coffeemod;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import owca.coffeemod.init.BlockInit;
import owca.coffeemod.objects.blocks.CoffeeBushBlock;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BiomeFeatureProvider {

    public static void addFeatures() {
        for (Biome biome : WorldGenRegistries.field_243657_i) {
            if (biome.getCategory() == Biome.Category.PLAINS) {
                addFeatureToBiome(biome, GenerationStage.Decoration.VEGETAL_DECORATION, BiomeFeatures.COFFEE_BUSH);
            }
        }
    }

    public static void addFeatureToBiome(Biome biome, GenerationStage.Decoration feature, ConfiguredFeature<?, ?> configuredFeature) {
        ConvertImmutableFeatures(biome);
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = biome.func_242440_e().field_242484_f;

        while(biomeFeatures.size() <= feature.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }

        biomeFeatures.get(feature.ordinal()).add(() -> configuredFeature);
    }

    private static void ConvertImmutableFeatures(Biome biome) {
        if (biome.func_242440_e().field_242484_f instanceof ImmutableList) {
            biome.func_242440_e().field_242484_f = biome.func_242440_e().field_242484_f.stream().map(Lists::newArrayList).collect(Collectors.toList());
        }

    }

    public static class BiomeFeatures {
        public static ConfiguredFeature<?, ?> COFFEE_BUSH;

        public static ConfiguredFeature<?, ?> newConfiguredFeature(String registryName, ConfiguredFeature<?, ?> configuredFeature) {
            Registry.register(WorldGenRegistries.field_243653_e, new ResourceLocation("coffeemod", registryName), configuredFeature);
            return configuredFeature;
        }

        static {
            ConfiguredFeature<?, ?> configuredFeature1 = Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockInit.COFFEE_BUSH.get().getDefaultState().with(CoffeeBushBlock.AGE, 7)), new CoffeeBushBlock.CoffeeBushBlockPlacer())).tries(1).func_227317_b_().build())
                    .withPlacement(Features.Placements.field_244001_l)
                    .func_242731_b(1);
            COFFEE_BUSH = newConfiguredFeature("coffee_bush", configuredFeature1);
        }
    }
}
