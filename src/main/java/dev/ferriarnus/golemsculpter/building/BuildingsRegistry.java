package dev.ferriarnus.golemsculpter.building;

import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.core.colony.buildings.modules.BuildingModules;
import com.minecolonies.core.colony.buildings.modules.NoPrivateCrafterWorkerModule;
import com.minecolonies.core.colony.buildings.modules.WorkerBuildingModule;
import com.minecolonies.core.colony.buildings.moduleviews.WorkerBuildingModuleView;
import com.minecolonies.core.colony.buildings.views.EmptyView;
import dev.ferriarnus.golemsculpter.GolemSculpter;
import dev.ferriarnus.golemsculpter.block.BlockRegistry;
import dev.ferriarnus.golemsculpter.job.JobsRegistry;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.api.util.constant.Constants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BuildingsRegistry {

    public final static DeferredRegister<BuildingEntry> BUILDINGS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "buildings"), GolemSculpter.MODID);

    public static void register(IEventBus bus) {
        BUILDINGS.register(bus);
    }

    public static final BuildingEntry.ModuleProducer<WorkerBuildingModule,WorkerBuildingModuleView> GOLEM_SCULPTOR_WORK =
            new BuildingEntry.ModuleProducer<>("golem_sculptor_work", () -> new WorkerBuildingModule(JobsRegistry.SCULPTOR.get(), Skill.Mana, Skill.Creativity, true, (b) -> 1), () -> WorkerBuildingModuleView::new);

    public static final DeferredHolder<BuildingEntry, BuildingEntry> SCULPTOR = BUILDINGS.register("sculptor", () -> new BuildingEntry.Builder()
            .setBuildingBlock(BlockRegistry.SCUPTOR.get())
            .setBuildingProducer(BuildingSculptor::new)
            .setBuildingViewProducer(() -> BuildingSculptor.View::new)
            .setRegistryName(ResourceLocation.fromNamespaceAndPath(GolemSculpter.MODID, "sculptor"))
            .addBuildingModuleProducer(GOLEM_SCULPTOR_WORK)
            .addBuildingModuleProducer(BuildingModules.MIN_STOCK)
            .createBuildingEntry());
}
