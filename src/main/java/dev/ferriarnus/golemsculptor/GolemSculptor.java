package dev.ferriarnus.golemsculptor;

import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.core.generation.DatagenLootTableManager;
import com.minecolonies.core.generation.defaults.DefaultEnchantmentProvider;
import dev.ferriarnus.golemsculptor.block.BlockRegistry;
import dev.ferriarnus.golemsculptor.blockentity.BlockEntityRegistry;
import dev.ferriarnus.golemsculptor.building.BuildingRegistry;
import dev.ferriarnus.golemsculptor.data.GolemResearchProvider;
import dev.ferriarnus.golemsculptor.entity.EntityRegistry;
import dev.ferriarnus.golemsculptor.job.JobsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = GolemSculptor.MODID)
@Mod(GolemSculptor.MODID)
public class GolemSculptor {
    public static final String MODID = "golemsculptor";

    public GolemSculptor(IEventBus modEventBus) {
        JobsRegistry.register(modEventBus);
        BuildingRegistry.register(modEventBus);
        BlockRegistry.register(modEventBus);
        BlockEntityRegistry.register(modEventBus);
        EntityRegistry.register(modEventBus);
    }

    @SubscribeEvent
    public static void dataGeneratorSetup(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        RegistrySetBuilder enchRegBuilder = new RegistrySetBuilder().add(Registries.ENCHANTMENT, DefaultEnchantmentProvider::bootstrap);
        DatapackBuiltinEntriesProvider enchRegProvider = new DatapackBuiltinEntriesProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), enchRegBuilder, Set.of(Constants.MOD_ID, "minecraft"));
        generator.addProvider(false, enchRegProvider);
        final CompletableFuture<HolderLookup.Provider> provider = enchRegProvider.getRegistryProvider().thenApply(p -> new DatagenLootTableManager(p, event.getExistingFileHelper()));

        generator.addProvider(event.includeServer(), new GolemResearchProvider(generator.getPackOutput(), provider));

    }
}
