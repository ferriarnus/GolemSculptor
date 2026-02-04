package dev.ferriarnus.golemsculpter.block;

import dev.ferriarnus.golemsculpter.GolemSculpter;
import com.minecolonies.api.items.ItemBlockHut;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GolemSculpter.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GolemSculpter.MODID);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    public static DeferredBlock<SculptorHutBlock> SCUPTOR = BLOCKS.register("sculptor", SculptorHutBlock::new);
    public static DeferredItem<ItemBlockHut> SCUPTOR_ITEM = ITEMS.register("sculptor", () -> new ItemBlockHut(SCUPTOR.get(), new Item.Properties()));

}
