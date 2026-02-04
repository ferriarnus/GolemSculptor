package dev.ferriarnus.golemsculpter;

import dev.ferriarnus.golemsculpter.entity.EntityRegistry;
import dev.ferriarnus.golemsculpter.entity.SculptedGolemEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = GolemSculpter.MODID)
public class Events {

    @SubscribeEvent
    static void attribute(EntityAttributeCreationEvent event){
        event.put(EntityRegistry.GOLEM.get(), SculptedGolemEntity.getDefaultAttributes().build());
    }
}
