package com.example.examplemod.block;

import com.example.examplemod.ExampleMod;
import com.minecolonies.api.items.ItemBlockHut;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExampleMod.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static RegistryObject<SculptorHutBlock> SCUPTOR = BLOCKS.register("sculptor", SculptorHutBlock::new);
    public static RegistryObject<ItemBlockHut> SCUPTOR_ITEM = ITEMS.register("sculptor", () -> new ItemBlockHut(SCUPTOR.get(), new Item.Properties()));

}
