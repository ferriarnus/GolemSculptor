package dev.ferriarnus.golemsculptor.job;

import com.minecolonies.core.colony.jobs.views.DefaultJobView;
import dev.ferriarnus.golemsculptor.GolemSculptor;
import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.util.constant.Constants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class JobsRegistry {

    public final static DeferredRegister<JobEntry> JOBS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "jobs"), GolemSculptor.MODID);

    public static void register(IEventBus bus) {
        JOBS.register(bus);
    }

    public static final DeferredHolder<JobEntry, JobEntry> GOLEM_SCULPTOR = register(JOBS, "golem_sculptor", () -> new JobEntry.Builder()
            .setJobProducer(JobSculptor::new)
            .setJobViewProducer(() -> DefaultJobView::new)
            .setRegistryName(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "golem_sculptor"))
            .createJobEntry());

    private static DeferredHolder<JobEntry, JobEntry> register(final DeferredRegister<JobEntry> deferredRegister, final String path, final Supplier<JobEntry> supplier) {
        ModJobs.jobs.add(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path));
        return deferredRegister.register(path, supplier);
    }

}
