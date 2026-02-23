package dev.ferriarnus.golemsculptor.block;

import dev.ferriarnus.golemsculptor.GolemSculptor;
import com.minecolonies.api.items.ItemBlockHut;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GolemSculptor.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GolemSculptor.MODID);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    public static DeferredBlock<SculptorHutBlock> GOLEM_SCULPTOR = BLOCKS.register("golem_sculptor", SculptorHutBlock::new);
    public static DeferredItem<ItemBlockHut> GOLEM_SCUlPTOR_ITEM = ITEMS.register("golem_sculptor", () -> new ItemBlockHut(GOLEM_SCULPTOR.get(), new Item.Properties()));

}
