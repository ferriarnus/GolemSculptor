package dev.ferriarnus.golemsculpter;

import dev.ferriarnus.golemsculpter.entity.EntityRegistry;
import dev.ferriarnus.golemsculpter.entity.SculptedGolem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = GolemSculpter.MODID)
public class Events {

    @SubscribeEvent
    static void attribute(EntityAttributeCreationEvent event){
        event.put(EntityRegistry.GOLEM.get(), SculptedGolem.createAttributes().build());
    }
}
