package dev.ferriarnus.golemsculpter;

import dev.ferriarnus.golemsculpter.block.BlockRegistry;
import dev.ferriarnus.golemsculpter.blockentity.BlockEntityRegistry;
import dev.ferriarnus.golemsculpter.building.BuildingsRegistry;
import dev.ferriarnus.golemsculpter.entity.EntityRegistry;
import dev.ferriarnus.golemsculpter.job.JobsRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod(GolemSculpter.MODID)
public class GolemSculpter {
    public static final String MODID = "golemsculpter";
    public GolemSculpter() {
        JobsRegistry.register();
        BuildingsRegistry.register();
        BlockRegistry.register();
        BlockEntityRegistry.register();
        EntityRegistry.register();
    }
}
