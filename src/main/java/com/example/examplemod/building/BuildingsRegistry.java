package com.example.examplemod.building;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.job.JobsRegistry;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.coremod.colony.buildings.modules.MinimumStockModule;
import com.minecolonies.coremod.colony.buildings.modules.WorkerBuildingModule;
import com.minecolonies.coremod.colony.buildings.moduleviews.MinimumStockModuleView;
import com.minecolonies.coremod.colony.buildings.moduleviews.WorkerBuildingModuleView;
import com.minecolonies.coremod.colony.buildings.views.EmptyView;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BuildingsRegistry {

    public final static DeferredRegister<BuildingEntry> BUILDINGS = DeferredRegister.create(new ResourceLocation(Constants.MOD_ID, "buildings"), ExampleMod.MODID);

    public static void register() {
        BUILDINGS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BuildingEntry> SCULPTOR = BUILDINGS.register("sculptor", () -> new BuildingEntry.Builder()
            .setBuildingBlock(BlockRegistry.SCUPTOR.get())
            .setBuildingProducer(BuildingSculptor::new)
            .setBuildingViewProducer(() -> EmptyView::new)
            .setRegistryName(new ResourceLocation(ExampleMod.MODID, "sculptor"))
            .addBuildingModuleProducer(() -> new WorkerBuildingModule(JobsRegistry.SCULPTOR.get(), Skill.Mana, Skill.Creativity, true, (b) -> 1), () -> WorkerBuildingModuleView::new)
            .addBuildingModuleProducer(MinimumStockModule::new, () -> MinimumStockModuleView::new)
            .createBuildingEntry());
}
