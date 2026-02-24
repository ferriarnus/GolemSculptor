package dev.ferriarnus.golemsculptor.building;

import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.IColonyView;
import com.minecolonies.api.colony.buildings.IGuardBuilding;
import com.minecolonies.api.colony.requestsystem.location.ILocation;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.research.util.ResearchConstants;
import com.minecolonies.api.util.MessageUtils;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.colony.requestsystem.locations.EntityLocation;
import com.minecolonies.core.colony.requestsystem.locations.StaticLocation;
import com.minecolonies.core.items.ItemBannerRallyGuards;
import com.minecolonies.core.util.AttributeModifierUtils;
import com.minecolonies.core.util.ServerUtils;
import dev.ferriarnus.golemsculptor.GolemSculptor;
import dev.ferriarnus.golemsculptor.entity.SculptedGolemEntity;
import dev.ferriarnus.golemsculptor.entity.GolemType;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.util.BlockPosUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class BuildingSculptor extends AbstractBuilding implements IGuardBuilding {

    public static final ResourceLocation GOLEM_HEALTH_MOD_BUILDING_NAME = ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "golembuildinghp");

    public static final String GOLEM_SCULPTOR = "golem_sculptor";

    private static final String TAG_ALOCATION = "andesite";
    private static final String TAG_GLOCATION = "granite";
    private static final String TAG_QLOCATION = "quartz";
    private static final String TAG_PLOCATION = "prismarine";
    private static final String TAG_OLOCATION = "obsidian";

    private UUID followPlayerUUID;
    private ILocation rallyLocation;
    private BlockPos guardPos = this.getID();

    private BlockPos aPos = null;
    private BlockPos gPos = null;
    private BlockPos qPos = null;
    private BlockPos pPos = null;
    private BlockPos oPos = null;

    private final SculptedGolemEntity[] golems = new SculptedGolemEntity[5];

    protected BuildingSculptor(@NotNull IColony colony, BlockPos pos) {
        super(colony, pos);
    }

    @Override
    public String getSchematicName() {
        return GOLEM_SCULPTOR;
    }

    public boolean addGolem(SculptedGolemEntity entity) {
        if (golems[entity.getGolemType().ordinal()] == null) {
            golems[entity.getGolemType().ordinal()] = entity;
            return true;
        }

        return false;
    }

    public void removeGolem(SculptedGolemEntity entity) {
        if (golems[entity.getGolemType().ordinal()] == entity) {
            golems[entity.getGolemType().ordinal()] = null;
        }
    }

    public List<ItemStack> getGolemItems(GolemType type) {
        return List.of(new ItemStack(type.main, 6), new ItemStack(type.repair, 4));
    }

    @Nullable
    public SculptedGolemEntity getGolem(GolemType type) {
        return golems[type.ordinal()];
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compound) {
        super.deserializeNBT(provider, compound);

        aPos = BlockPosUtil.readOrNull(compound, TAG_ALOCATION);
        gPos = BlockPosUtil.readOrNull(compound, TAG_GLOCATION);
        qPos = BlockPosUtil.readOrNull(compound, TAG_QLOCATION);
        pPos = BlockPosUtil.readOrNull(compound, TAG_PLOCATION);
        oPos = BlockPosUtil.readOrNull(compound, TAG_OLOCATION);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        final CompoundTag compound = super.serializeNBT(provider);

        BlockPosUtil.writeOptional(compound, TAG_ALOCATION, aPos);
        BlockPosUtil.writeOptional(compound, TAG_GLOCATION, gPos);
        BlockPosUtil.writeOptional(compound, TAG_QLOCATION, qPos);
        BlockPosUtil.writeOptional(compound, TAG_PLOCATION, oPos);
        BlockPosUtil.writeOptional(compound, TAG_OLOCATION, pPos);

        return compound;
    }

    @Override
    public void serializeToView(@NotNull RegistryFriendlyByteBuf buf, boolean fullSync) {
        super.serializeToView(buf, fullSync);
        buf.writeInt(0); //Patrols
        buf.writeInt(0); //Guards
        buf.writeBoolean(false); //Minepos
    }

    @Nullable
    public GolemType canMakeGolem() {
        if (golems[0] == null) {
            return GolemType.ANDESITE;
        } else if (golems[1] == null && this.getBuildingLevel() > 1) {
            return GolemType.GRANITE;
        } else if (golems[2] == null && this.getBuildingLevel() > 2) {
            return GolemType.QUARTZ;
        } else if (golems[3] == null && this.getBuildingLevel() > 3) {
            return GolemType.PRISMARINE;
        } else if (golems[4] == null && this.getBuildingLevel() > 4) {
            return GolemType.OBSIDIAN;
        }

        return null;
    }

    @Nullable
    public GolemType canRepair() {
        for (SculptedGolemEntity golem : golems) {
            if (golem == null) {
                continue;
            }

            float hp = golem.getHealth()  / golem.getMaxHealth();
            if (hp < 0.75f) {
                return golem.getGolemType();
            }
        }

        return null;
    }

    @Nullable
    public BlockPos getPosition(SculptedGolemEntity golem) {
        BlockPos pos = switch (golem.getGolemType()) {
            case ANDESITE -> aPos;
            case GRANITE -> gPos;
            case QUARTZ -> qPos;
            case PRISMARINE -> pPos;
            case OBSIDIAN -> oPos;
        };

        if (pos == null) {
            loadPos();
        }

        return pos;
    }

    private void loadPos() {
        if (tileEntity == null) {
            return;
        }

        final Map<String, Set<BlockPos>> map = tileEntity.getWorldTagNamePosMap();
        final Set<BlockPos> andesitePos = map.getOrDefault(TAG_ALOCATION, new HashSet<>());
        final Set<BlockPos> granitePos = map.getOrDefault(TAG_GLOCATION, new HashSet<>());
        final Set<BlockPos> quartzPos = map.getOrDefault(TAG_QLOCATION, new HashSet<>());
        final Set<BlockPos> prismarinePos = map.getOrDefault(TAG_PLOCATION, new HashSet<>());
        final Set<BlockPos> obsidianPos = map.getOrDefault(TAG_OLOCATION, new HashSet<>());

        if (!andesitePos.isEmpty()) {
            aPos = andesitePos.iterator().next();
        }

        if (!granitePos.isEmpty()) {
            gPos = granitePos.iterator().next();
        }

        if (!quartzPos.isEmpty()) {
            qPos = quartzPos.iterator().next();
        }

        if (!prismarinePos.isEmpty()) {
            pPos = prismarinePos.iterator().next();
        }

        if (!obsidianPos.isEmpty()) {
            oPos = obsidianPos.iterator().next();
        }
    }

    public double getBonusHealth() {
        return getBuildingLevel() * 3;
    }

    public float getAttackDamage() {
        return 2.0F;
    }

    @Override
    public void onUpgradeComplete(int newLevel) {
        for (SculptedGolemEntity golem : golems) {
            if (golem != null) {
                final AttributeModifier healthModBuildingHP = new AttributeModifier(GOLEM_HEALTH_MOD_BUILDING_NAME, getBonusHealth(), AttributeModifier.Operation.ADD_VALUE);
                AttributeModifierUtils.addHealthModifier(golem, healthModBuildingHP);
            }
        }

        super.onUpgradeComplete(newLevel);
    }

    @Override
    public void onDestroyed() {
        for (SculptedGolemEntity golem : golems) {
            if (golem != null) {
                golem.remove(Entity.RemovalReason.DISCARDED);
            }
        }

        super.onDestroyed();
    }

    @Override
    public String getTask() {
        return "Golem";
    }

    @Nullable
    @Override
    public BlockPos getNextPatrolTarget(boolean b) {
        return null;
    }

    @Override
    public void arrivedAtPatrolPoint(AbstractEntityCitizen abstractEntityCitizen) {

    }

    @Override
    public int getPatrolDistance() {
        return 0;
    }

    @Override
    public boolean shallRetrieveOnLowHealth() {
        return false;
    }

    @Override
    public boolean shallPatrolManually() {
        return false;
    }

    @Override
    public boolean isTightGrouping() {
        return false;
    }

    @Override
    public BlockPos getGuardPos(@NotNull AbstractEntityCitizen abstractEntityCitizen) {
        return this.guardPos;
    }

    @Override
    public void setGuardPos(BlockPos blockPos) {
        this.guardPos = blockPos;
    }

    @Override
    public Player getPlayerToFollowOrRally() {
        if (this.rallyLocation != null && this.rallyLocation instanceof EntityLocation entityLocation) {
            return entityLocation.getPlayerEntity();
        } else {
            return null;
        }
    }

    @Override
    public void setPlayerToFollow(Player player) {
        this.followPlayerUUID = player.getUUID();
    }

    @Override
    public ILocation getRallyLocation() {
        if (this.rallyLocation == null) {
            return null;
        } else {
            boolean outOfRange = false;
            IColony colonyAtPosition = IColonyManager.getInstance().getColonyByPosFromDim(this.rallyLocation.getDimension(), this.rallyLocation.getInDimensionLocation());
            if ((colonyAtPosition == null || colonyAtPosition.getID() != this.colony.getID()) && (this.getColony().getResearchManager().getResearchEffects().getEffectStrength(ResearchConstants.TELESCOPE) <= 0.0 || BlockPosUtil.getDistance2D(this.rallyLocation.getInDimensionLocation(), this.colony.getCenter()) > 500L)) {
                outOfRange = true;
            }

            if (this.rallyLocation instanceof EntityLocation) {
                Player player = ((EntityLocation)this.rallyLocation).getPlayerEntity();
                if (player == null) {
                    this.setRallyLocation(null);
                    return null;
                } else if (outOfRange) {
                    MessageUtils.format("item.minecolonies.banner_rally_guards.outofrange").sendTo(player);
                    this.setRallyLocation(null);
                    return null;
                } else {
                    int size = player.getInventory().getContainerSize();

                    for(int i = 0; i < size; ++i) {
                        ItemStack stack = player.getInventory().getItem(i);
                        if (stack.getItem() instanceof ItemBannerRallyGuards && ((ItemBannerRallyGuards)stack.getItem()).isActiveForGuardTower(stack, this)) {
                            return this.rallyLocation;
                        }
                    }

                    return null;
                }
            } else if (this.rallyLocation instanceof StaticLocation && outOfRange) {
                MessageUtils.format("item.minecolonies.banner_rally_guards.outofrange").sendTo(this.colony.getImportantMessageEntityPlayers());
                this.setRallyLocation(null);
                return null;
            } else {
                return this.rallyLocation;
            }
        }
    }

    @Override
    public void setRallyLocation(ILocation location) {
        this.rallyLocation = location;
    }

    @Override
    public BlockPos getPositionToFollow() {
        Player followPlayer = ServerUtils.getPlayerFromUUID(this.followPlayerUUID, this.colony.getWorld());
        return followPlayer != null && followPlayer.level().dimension() == this.colony.getDimension() ? followPlayer.blockPosition() : this.getPosition();
    }

    @Override
    public void addPatrolTarget(BlockPos blockPos) {

    }

    @Override
    public void resetPatrolTargets() {

    }

    @Override
    public int getBonusVision() {
        return 0;
    }

    @Override
    public void calculateMobs() {

    }

    @Override
    public boolean requiresManualTarget() {
        return false;
    }

    @Override
    public void setTempNextPatrolPoint(BlockPos blockPos) {

    }

    @Override
    public BlockPos getMinePos() {
        return null;
    }

    public static class View extends AbstractBuildingGuards.View {

        public View(IColonyView c, @NotNull BlockPos l) {
            super(c, l);
        }

        @Override
        public void deserialize(@NotNull RegistryFriendlyByteBuf buf) {
            super.deserialize(buf);
        }

        @Override
        public List<BlockPos> getPatrolTargets() {
            return List.of();
        }

        @Override
        public @NotNull List<Integer> getGuards() {
            return List.of();
        }

        @Override
        public BlockPos getMinePos() {
            return null;
        }
    }
}
