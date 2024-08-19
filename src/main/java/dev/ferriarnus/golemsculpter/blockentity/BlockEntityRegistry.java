package dev.ferriarnus.golemsculpter.blockentity;

import dev.ferriarnus.golemsculpter.GolemSculpter;
import dev.ferriarnus.golemsculpter.block.BlockRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GolemSculpter.MODID);

    public static void register() {
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BlockEntityType<SculptorBlockEntity>> SCULPTOR = BLOCK_ENTITIES.register("sculptor", () -> BlockEntityType.Builder.of(SculptorBlockEntity::new, BlockRegistry.SCUPTOR.get()).build(null));
}
