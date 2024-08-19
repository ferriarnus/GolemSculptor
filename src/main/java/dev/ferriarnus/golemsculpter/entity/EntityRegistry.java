package dev.ferriarnus.golemsculpter.entity;

import dev.ferriarnus.golemsculpter.GolemSculpter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GolemSculpter.MODID);

    public static void register() {
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<EntityType<SculptedGolem>> GOLEM = ENTITY_TYPES.register("golem", () -> EntityType.Builder.of(SculptedGolem::new, MobCategory.MISC).sized(1.4F, 2.7F).clientTrackingRange(10).build("golem"));
}
