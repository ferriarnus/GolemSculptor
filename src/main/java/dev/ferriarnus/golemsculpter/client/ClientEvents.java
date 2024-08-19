package dev.ferriarnus.golemsculpter.client;

import dev.ferriarnus.golemsculpter.GolemSculpter;
import dev.ferriarnus.golemsculpter.entity.EntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = GolemSculpter.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.GOLEM.get(), SculptedGolemRenderer::new);
    }
}
