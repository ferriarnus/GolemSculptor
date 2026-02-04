package dev.ferriarnus.golemsculpter.client;

import dev.ferriarnus.golemsculpter.GolemSculpter;
import dev.ferriarnus.golemsculpter.entity.EntityRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = GolemSculpter.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.GOLEM.get(), SculptedGolemRenderer::new);
    }
}
