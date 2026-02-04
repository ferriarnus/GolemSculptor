package dev.ferriarnus.golemsculpter.building;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.IColonyView;
import com.minecolonies.api.colony.buildings.IGuardBuilding;
import com.minecolonies.api.colony.requestsystem.location.ILocation;
import com.minecolonies.api.entity.ai.statemachine.AIOneTimeEventTarget;
import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.research.util.ResearchConstants;
import com.minecolonies.api.util.MessageUtils;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.colony.jobs.AbstractJobGuard;
import com.minecolonies.core.colony.requestsystem.locations.EntityLocation;
import com.minecolonies.core.colony.requestsystem.locations.StaticLocation;
import com.minecolonies.core.items.ItemBannerRallyGuards;
import com.minecolonies.core.util.ServerUtils;
import dev.ferriarnus.golemsculpter.entity.SculptedGolemEntity;
import dev.ferriarnus.golemsculpter.entity.GolemType;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.util.BlockPosUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.minecolonies.core.colony.buildings.AbstractBuildingGuards.GUARD_TASK;

public class BuildingSculptor extends AbstractBuilding implements IGuardBuilding {

    private UUID followPlayerUUID;
    private ILocation rallyLocation;
    private BlockPos guardPos = this.getID();

    private SculptedGolemEntity[] golems = new SculptedGolemEntity[5];

    protected BuildingSculptor(@NotNull IColony colony, BlockPos pos) {
        super(colony, pos);
    }

    @Override
    public String getSchematicName() {
        return "sculptor";
    }

    public boolean addGolem(SculptedGolemEntity entity) {
        if (golems[entity.getGolemType().ordinal()] == null) {
            golems[entity.getGolemType().ordinal()] = entity;
            return true;
        }
        return false;
    }

    public void removeGolem(SculptedGolemEntity entity) {
        if (entity.getGolemType() == null) {
            return;
        }
        if (golems[entity.getGolemType().ordinal()] == entity) {
            golems[entity.getGolemType().ordinal()] = null;
        }
    }

    public List<ItemStack> getGolemItems(GolemType type) {
        return List.of(new ItemStack(type.main, 6), new ItemStack(type.repair, 6));
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compound) {
        super.deserializeNBT(provider, compound);

    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return super.serializeNBT(provider);
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
        }
        if (golems[1] == null && this.getBuildingLevel() > 1) {
            return GolemType.GRANITE;
        }
        if (golems[2] == null && this.getBuildingLevel() > 2) {
            return GolemType.QUARTZ;
        }
        if (golems[3] == null && this.getBuildingLevel() > 3) {
            return GolemType.PRISMARINE;
        }
        if (golems[4] == null && this.getBuildingLevel() > 4) {
            return GolemType.OBSIDIAN;
        }
        return null;
    }

    @Override
    public String getTask() {
        return "Golem";
    }

    @Override
    public @Nullable BlockPos getNextPatrolTarget(boolean b) {
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
            return this.getTask().equals("com.minecolonies.core.guard.setting.follow") ? ServerUtils.getPlayerFromUUID(this.followPlayerUUID, this.colony.getWorld()) : null;
        }
    }

    @Override
    public void setPlayerToFollow(Player player) {
        this.followPlayerUUID = player.getUUID();
        //TODO link to golems
        for (ICitizenData iCitizenData : this.getAllAssignedCitizen()) {
            AbstractJobGuard<?> job = iCitizenData.getJob(AbstractJobGuard.class);
            if (job != null && job.getWorkerAI() != null) {
                job.getWorkerAI().registerTarget(new AIOneTimeEventTarget<>(AIWorkerState.PREPARING));
            }
        }
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
                    MessageUtils.format("item.minecolonies.banner_rally_guards.outofrange").sendTo(new Player[]{player});
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
        boolean reduceSaturation = this.rallyLocation != null && location == null;

        this.rallyLocation = location;

        //TODO link to golems
        for (ICitizenData iCitizenData : this.getAllAssignedCitizen()) {
            if (reduceSaturation && iCitizenData.getSaturation() < 6.0) {
                iCitizenData.decreaseSaturation(6.0);
            }
        }
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
