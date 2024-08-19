package dev.ferriarnus.golemsculpter.job;

import dev.ferriarnus.golemsculpter.GolemSculpter;
import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.util.constant.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import com.minecolonies.coremod.colony.jobs.views.DefaultJobView;

import java.util.function.Supplier;

public class JobsRegistry {

    public final static DeferredRegister<JobEntry> JOBS = DeferredRegister.create(new ResourceLocation(Constants.MOD_ID, "jobs"), GolemSculpter.MODID);

    public static void register() {
        JOBS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<JobEntry> SCULPTOR = register(JOBS, "sculptor", () -> new JobEntry.Builder()
            .setJobProducer(JobSculptor::new)
            .setJobViewProducer(() -> DefaultJobView::new)
            .setRegistryName(new ResourceLocation(GolemSculpter.MODID, "sculptor"))
            .createJobEntry());

    private static RegistryObject<JobEntry> register(final DeferredRegister<JobEntry> deferredRegister, final String path, final Supplier<JobEntry> supplier) {
        ModJobs.jobs.add(new ResourceLocation(Constants.MOD_ID, path));
        return deferredRegister.register(path, supplier);
    }

}
