package dev.ferriarnus.golemsculpter;

import dev.ferriarnus.golemsculpter.block.BlockRegistry;
import dev.ferriarnus.golemsculpter.blockentity.BlockEntityRegistry;
import dev.ferriarnus.golemsculpter.building.BuildingsRegistry;
import dev.ferriarnus.golemsculpter.entity.EntityRegistry;
import dev.ferriarnus.golemsculpter.job.JobsRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(GolemSculpter.MODID)
public class GolemSculpter {
    public static final String MODID = "golemsculpter";

    public GolemSculpter(IEventBus modEventBus) {
        JobsRegistry.register(modEventBus);
        BuildingsRegistry.register(modEventBus);
        BlockRegistry.register(modEventBus);
        BlockEntityRegistry.register(modEventBus);
        EntityRegistry.register(modEventBus);
    }
}
