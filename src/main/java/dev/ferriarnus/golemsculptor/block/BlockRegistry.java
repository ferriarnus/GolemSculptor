package dev.ferriarnus.golemsculptor.block;

import dev.ferriarnus.golemsculptor.GolemSculptor;
import com.minecolonies.api.items.ItemBlockHut;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GolemSculptor.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GolemSculptor.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GolemSculptor.MODID);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        CREATIVE_MODE_TABS.register(bus);
    }

    public static DeferredBlock<SculptorHutBlock> GOLEM_SCULPTOR = BLOCKS.register("golem_sculptor", SculptorHutBlock::new);
    public static DeferredItem<ItemBlockHut> GOLEM_SCULPTOR_ITEM = ITEMS.register("golem_sculptor", () -> new ItemBlockHut(GOLEM_SCULPTOR.get(), new Item.Properties()));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GOLEM_SCULPTOR_TAB = CREATIVE_MODE_TABS.register("golem_sculptor", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.golem_sculptor"))
            .icon(() -> GOLEM_SCULPTOR_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(GOLEM_SCULPTOR_ITEM.get());
            }).build());
}
