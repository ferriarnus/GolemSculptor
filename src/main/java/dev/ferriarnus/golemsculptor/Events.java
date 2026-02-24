package dev.ferriarnus.golemsculptor;

import dev.ferriarnus.golemsculptor.entity.EntityRegistry;
import dev.ferriarnus.golemsculptor.entity.SculptedGolemEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = GolemSculptor.MODID)
public class Events {

    @SubscribeEvent
    static void attribute(EntityAttributeCreationEvent event){
        event.put(EntityRegistry.SCULPTED_GOLEM.get(), SculptedGolemEntity.getDefaultAttributes().build());
    }
}
