package dev.ferriarnus.golemsculptor.building;

import com.minecolonies.core.colony.buildings.modules.BuildingModules;
import com.minecolonies.core.colony.buildings.modules.NoPrivateCrafterWorkerModule;
import com.minecolonies.core.colony.buildings.modules.WorkerBuildingModule;
import com.minecolonies.core.colony.buildings.moduleviews.WorkerBuildingModuleView;
import dev.ferriarnus.golemsculptor.GolemSculptor;
import dev.ferriarnus.golemsculptor.block.BlockRegistry;
import dev.ferriarnus.golemsculptor.job.JobsRegistry;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.api.util.constant.Constants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BuildingRegistry {

    public final static DeferredRegister<BuildingEntry> BUILDINGS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "buildings"), GolemSculptor.MODID);

    public static void register(IEventBus bus) {
        BUILDINGS.register(bus);
    }

    public static final BuildingEntry.ModuleProducer<WorkerBuildingModule,WorkerBuildingModuleView> GOLEM_SCULPTOR_WORK =
            new BuildingEntry.ModuleProducer<>("golem_sculptor_work", () -> new NoPrivateCrafterWorkerModule(JobsRegistry.GOLEM_SCULPTOR.get(), Skill.Mana, Skill.Creativity, true, (b) -> 1), () -> WorkerBuildingModuleView::new);

    public static final DeferredHolder<BuildingEntry, BuildingEntry> GOLEM_SCULPTOR = BUILDINGS.register("golem_sculptor", () -> new BuildingEntry.Builder()
            .setBuildingBlock(BlockRegistry.GOLEM_SCULPTOR.get())
            .setBuildingProducer(BuildingSculptor::new)
            .setBuildingViewProducer(() -> BuildingSculptor.View::new)
            .setRegistryName(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "golem_sculptor"))
            .addBuildingModuleProducer(GOLEM_SCULPTOR_WORK)
            .addBuildingModuleProducer(BuildingModules.MIN_STOCK)
            .createBuildingEntry());
}
