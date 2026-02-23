package dev.ferriarnus.golemsculptor.blockentity;

import com.minecolonies.api.util.IItemHandlerCapProvider;
import dev.ferriarnus.golemsculptor.GolemSculptor;
import dev.ferriarnus.golemsculptor.block.BlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = GolemSculptor.MODID)
public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GolemSculptor.MODID);

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<SculptorBlockEntity>> GOLEM_SCULPTOR = BLOCK_ENTITIES
            .register("golem_sculptor", () -> BlockEntityType.Builder.of(SculptorBlockEntity::new, BlockRegistry.GOLEM_SCULPTOR.get()).build(null));

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityRegistry.GOLEM_SCULPTOR.get(), IItemHandlerCapProvider::getItemHandlerCap);
    }
}
