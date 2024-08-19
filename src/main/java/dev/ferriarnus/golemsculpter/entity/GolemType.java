package dev.ferriarnus.golemsculpter.entity;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum GolemType {

    ANDESITE(Items.ANDESITE, Items.COPPER_INGOT),
    GRANITE(Items.POLISHED_GRANITE, Items.IRON_INGOT),
    QUARTZ(Items.QUARTZ_BLOCK, Items.GOLD_BLOCK),
    PRISMARINE(Items.PRISMARINE, Items.REDSTONE),
    OBSIDIAN(Items.OBSIDIAN, Items.DIAMOND);

    public final Item main;
    public final Item repair;

    GolemType(Item main, Item repair) {
        this.main = main;
        this.repair = repair;
    }
}
