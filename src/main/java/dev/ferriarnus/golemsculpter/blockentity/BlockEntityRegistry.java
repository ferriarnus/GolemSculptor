package dev.ferriarnus.golemsculpter.blockentity;

import dev.ferriarnus.golemsculpter.GolemSculpter;
import dev.ferriarnus.golemsculpter.block.BlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GolemSculpter.MODID);

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<SculptorBlockEntity>> SCULPTOR = BLOCK_ENTITIES
            .register("sculptor", () -> BlockEntityType.Builder.of(SculptorBlockEntity::new, BlockRegistry.SCUPTOR.get()).build(null));
}
