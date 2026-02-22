package dev.ferriarnus.golemsculpter.entity;

import dev.ferriarnus.golemsculpter.GolemSculpter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class EntityRegistry {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, GolemSculpter.MODID);

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }

    public static final DeferredHolder<EntityType<?>,EntityType<SculptedGolemEntity>> GOLEM = ENTITY_TYPES
            .register("golem", () -> EntityType.Builder.of(SculptedGolemEntity::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .setTrackingRange(256)
                    .updateInterval(2)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("golem")
            );
}
