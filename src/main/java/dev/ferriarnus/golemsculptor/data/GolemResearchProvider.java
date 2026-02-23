package dev.ferriarnus.golemsculptor.data;

import com.minecolonies.api.research.AbstractResearchProvider;
import com.minecolonies.api.util.constant.Constants;
import dev.ferriarnus.golemsculptor.GolemSculptor;
import dev.ferriarnus.golemsculptor.block.BlockRegistry;
import dev.ferriarnus.golemsculptor.building.BuildingRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GolemResearchProvider extends AbstractResearchProvider {
    private static final ResourceLocation COMBAT   = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "combat");

    public static final ResourceLocation REINFORCED = ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "effects/reinforced");

    public static final ResourceLocation KNOCKBACK = ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "effects/knockback");

    public GolemResearchProvider(@NotNull PackOutput packOutput, @NotNull CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, provider);
    }

    @Override
    protected Collection<ResearchBranch> getResearchBranchCollection() {
        return List.of();
    }

    @Override
    protected Collection<ResearchEffect> getResearchEffectCollection() {
        List<ResearchEffect> effects = new ArrayList<>();

        effects.add(new ResearchEffect(REINFORCED).setTranslatedName("Golem Armor +3").setLevels(new double[] {3, 6, 10, 13, 16})); // +4 base armour

        effects.add(new ResearchEffect(KNOCKBACK).setTranslatedName("Golem Knockback +1").setLevels(new double[] {1.0, 2.0, 3.0, 4.0, 5.0}));

        effects.add(new ResearchEffect(BuildingRegistry.GOLEM_SCULPTOR.get().getBuildingBlock()).setTranslatedName("Unlocks Golem Sculpter").setLevels(new double[] {5}));
        return effects;
    }

    @Override
    protected Collection<Research> getResearchCollection() {
        List<Research> researches = new ArrayList<>();

        Research golemsculpter = new Research(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "combat/golem_sculptor"), COMBAT)
                .setTranslatedName("Reinforcements")
                .setTranslatedSubtitle("You have been summoned")
                .setOnlyChild()
                .setIcon(BlockRegistry.GOLEM_SCULPTOR.asItem())
                .addBuildingRequirement(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "stonemason"), 1)
                .addBuildingRequirement(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "enchanter"), 1)
                .addItemCost(Items.ENCHANTED_BOOK, 1, provider)
                .addItemCost(Items.JACK_O_LANTERN, 1, provider)
                .addEffect(BuildingRegistry.GOLEM_SCULPTOR.get().getBuildingBlock(), 1)
                .addToList(researches);

        Research reinforced = new Research(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "combat/reinforced"), COMBAT)
                .setTranslatedName("Reinforced")
                .setTranslatedSubtitle("Sturdier")
                .setIcon(BlockRegistry.GOLEM_SCULPTOR.asItem())
                .setParentResearch(golemsculpter)
                .addBuildingRequirement(BuildingRegistry.GOLEM_SCULPTOR.getId(),1)
                .addItemCost(Items.STONE_BRICKS, 64, provider)
                .addEffect(REINFORCED, 1)
                .addToList(researches);

        Research iron_bound = new Research(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "combat/iron_bound"), COMBAT)
                .setTranslatedName("Iron Bound")
                .setTranslatedSubtitle("Fortified")
                .setIcon(BlockRegistry.GOLEM_SCULPTOR.asItem())
                .setParentResearch(reinforced)
                .addBuildingRequirement(BuildingRegistry.GOLEM_SCULPTOR.getId(),2)
                .addItemCost(Items.IRON_BARS, 16, provider)
                .addEffect(REINFORCED, 2)
                .addToList(researches);

        Research deepslate = new Research(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "combat/deepslate"), COMBAT)
                .setTranslatedName("Compressed")
                .setTranslatedSubtitle("Under pressure")
                .setIcon(BlockRegistry.GOLEM_SCULPTOR.asItem())
                .setParentResearch(iron_bound)
                .addBuildingRequirement(BuildingRegistry.GOLEM_SCULPTOR.getId(),3)
                .addItemCost(Items.DEEPSLATE_BRICKS, 64, provider)
                .addEffect(REINFORCED, 3)
                .addToList(researches);

        Research obsidian = new Research(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "combat/obsidian"), COMBAT)
                .setTranslatedName("Lava Forged")
                .setTranslatedSubtitle("Sculpted to perfection")
                .setIcon(BlockRegistry.GOLEM_SCULPTOR.asItem())
                .setParentResearch(deepslate)
                .addBuildingRequirement(BuildingRegistry.GOLEM_SCULPTOR.getId(),4)
                .addItemCost(Items.OBSIDIAN, 32, provider)
                .addEffect(REINFORCED, 4)
                .addToList(researches);

        Research netherite = new Research(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "combat/netherite"), COMBAT)
                .setTranslatedName("Unbreakable")
                .setTranslatedSubtitle("Lava Proof")
                .setIcon(BlockRegistry.GOLEM_SCULPTOR.asItem())
                .setParentResearch(obsidian)
                .addBuildingRequirement(BuildingRegistry.GOLEM_SCULPTOR.getId(),5)
                .addItemCost(Items.NETHERITE_BLOCK, 2, provider)
                .addEffect(REINFORCED, 5)
                .addToList(researches);

        Research knockback = new Research(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "combat/knockback"), COMBAT)
                .setTranslatedName("Don't push me")
                .setTranslatedSubtitle("Cause I'm close to the edge")
                .setIcon(BlockRegistry.GOLEM_SCULPTOR.asItem())
                .setParentResearch(golemsculpter)
                .addBuildingRequirement(BuildingRegistry.GOLEM_SCULPTOR.getId(),1)
                .addItemCost(Items.PISTON, 1, provider)
                .addEffect(KNOCKBACK, 1)
                .addToList(researches);

        Research knockback2 = new Research(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "combat/knockback2"), COMBAT)
                .setTranslatedName("Out of the way!")
                .setTranslatedSubtitle("No time to waste")
                .setIcon(BlockRegistry.GOLEM_SCULPTOR.asItem())
                .setParentResearch(knockback)
                .addBuildingRequirement(BuildingRegistry.GOLEM_SCULPTOR.getId(),2)
                .addItemCost(Items.PISTON, 4, provider)
                .addEffect(KNOCKBACK, 2)
                .addToList(researches);

        Research knockback3 = new Research(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "combat/knockback3"), COMBAT)
                .setTranslatedName("A Stone's Throw")
                .setTranslatedSubtitle("Through the roof")
                .setIcon(BlockRegistry.GOLEM_SCULPTOR.asItem())
                .setParentResearch(knockback2)
                .addBuildingRequirement(BuildingRegistry.GOLEM_SCULPTOR.getId(),3)
                .addItemCost(Items.PISTON, 8, provider)
                .addEffect(KNOCKBACK, 3)
                .addToList(researches);

        Research knockback4 = new Research(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "combat/knockback4"), COMBAT)
                .setTranslatedName("Yeet")
                .setTranslatedSubtitle("Cannonball!")
                .setIcon(BlockRegistry.GOLEM_SCULPTOR.asItem())
                .setParentResearch(knockback3)
                .addBuildingRequirement(BuildingRegistry.GOLEM_SCULPTOR.getId(),4)
                .addItemCost(Items.PISTON, 12, provider)
                .addEffect(KNOCKBACK, 4)
                .addToList(researches);

        Research knockback5 = new Research(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "combat/knockback5"), COMBAT)
                .setTranslatedName("Yeet²")
                .setTranslatedSubtitle("I can see Heaven")
                .setIcon(BlockRegistry.GOLEM_SCULPTOR.asItem())
                .setParentResearch(knockback4)
                .addBuildingRequirement(BuildingRegistry.GOLEM_SCULPTOR.getId(),5)
                .addItemCost(Items.PISTON, 16, provider)
                .addEffect(KNOCKBACK, 5)
                .addToList(researches);

        return researches;
    }
}
