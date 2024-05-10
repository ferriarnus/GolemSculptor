package com.example.examplemod;

import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.SculptedGolem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ExampleMod.MODID)
public class Events {

    @SubscribeEvent
    static void attribute(EntityAttributeCreationEvent event){
        event.put(EntityRegistry.GOLEM.get(), SculptedGolem.createAttributes().build());
    }
}
