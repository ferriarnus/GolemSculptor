package dev.ferriarnus.golemsculpter.entity;

import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.TickRateStateMachine;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.TickingTransition;
import com.minecolonies.api.entity.mobs.AbstractEntityMinecoloniesMonster;
import com.minecolonies.api.entity.other.AbstractFastMinecoloniesEntity;
import com.minecolonies.api.entity.pathfinding.registry.IPathNavigateRegistry;
import com.minecolonies.api.util.Log;
import com.minecolonies.core.entity.ai.minimal.EntityAIInteractToggleAble;
import com.minecolonies.core.entity.pathfinding.navigation.AbstractAdvancedPathNavigate;
import com.minecolonies.core.entity.pathfinding.navigation.PathingStuckHandler;
import dev.ferriarnus.golemsculpter.blockentity.SculptorBlockEntity;
import dev.ferriarnus.golemsculpter.building.BuildingSculptor;
import dev.ferriarnus.golemsculpter.ai.GolemAi;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.minecolonies.core.entity.ai.minimal.EntityAIInteractToggleAble.*;

public class SculptedGolemEntity extends AbstractFastMinecoloniesEntity {

    private static final double CITIZEN_SWIM_BONUS = 2.0;

    private AbstractAdvancedPathNavigate pathNavigate;

    private final ITickRateStateMachine<IState> stateMachine;

    private GolemType type;
    private BuildingSculptor building;
    private long position;

    protected SculptedGolemEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GolemAi(this));
        this.goalSelector.addGoal(4, new EntityAIInteractToggleAble(this, FENCE_TOGGLE, TRAP_TOGGLE, DOOR_TOGGLE));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractEntityMinecoloniesMonster.class, 10, false, false, e -> true));

        setCustomNameVisible(true);
        this.setPersistenceRequired();

        stateMachine = new TickRateStateMachine<>(GolemAi.State.INIT, this::handleStateException);
        stateMachine.addTransition(new TickingTransition<>(GolemAi.State.INIT, this::isInitialized, () -> GolemAi.State.ALIVE, 20));
        stateMachine.addTransition(new TickingTransition<>(GolemAi.State.INIT, this::shouldDespawn, () -> GolemAi.State.DEAD, 20));
        stateMachine.addTransition(new TickingTransition<>(GolemAi.State.ALIVE, this::shouldDespawn, () -> GolemAi.State.DEAD, 100));
        stateMachine.addTransition(new TickingTransition<>(GolemAi.State.DEAD, () -> true, this::getState, 500));
    }

    private void handleStateException(final RuntimeException e) {
        Log.getLogger().warn("Golem entity threw an exception:", e);
    }

    private boolean shouldDespawn() {
        if (this.level() != null && !this.isInvisible()) {
            if (this.building != null) {
                return false;
            }
            var entity = this.level().getBlockEntity(BlockPos.of(position));
            if (entity instanceof SculptorBlockEntity blockEntity && blockEntity.getBuilding() instanceof BuildingSculptor sculptor) {
                this.building = sculptor;
                return false;
            }

        }
        this.remove(RemovalReason.DISCARDED);
        return true;
    }

    private boolean isInitialized() {
        if (this.level() != null && this.isAlive() && !this.isInvisible()) {
            if (this.building != null) {
                return true;
            }
            var entity = this.level().getBlockEntity(BlockPos.of(position));
            if (entity instanceof SculptorBlockEntity blockEntity && blockEntity.getBuilding() instanceof BuildingSculptor sculptor) {
                this.building = sculptor;
                return true;
            }
        }
        return false;
    }

    public IState getState() {
        return this.stateMachine.getState();
    }

    public void setBuilding(BuildingSculptor building) {
        this.building = building;
        this.position = building.getPosition().asLong();
    }

    public BuildingSculptor getBuilding() {
        return building;
    }

    public GolemType getGolemType() {
        return type;
    }

    public void setGolemType(GolemType type) {
        this.type = type;
    }

    @Override
    public int getTeamId() {
        return building.getColony().getID();
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (source.getEntity() instanceof LivingEntity) {
            this.setTarget((LivingEntity)source.getEntity());
        }

        return super.hurt(source, damage);
    }

    @Override
    public void remove(RemovalReason reason) {
        if (building != null) {
            building.removeGolem(this);
        }
        super.remove(reason);
    }

    @Override
    public void aiStep() {
        if (this.level() != null && !this.level().isClientSide) {
            this.stateMachine.tick();
        }

        this.updateSwingTime();
        super.aiStep();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putLong("position", this.position);
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.position = compound.getLong("position");
        super.readAdditionalSaveData(compound);
    }

    @NotNull
    @Override
    public AbstractAdvancedPathNavigate getNavigation() {
        if (this.pathNavigate == null) {
            this.pathNavigate = IPathNavigateRegistry.getInstance().getNavigateFor(this);
            this.navigation = pathNavigate;
            this.pathNavigate.setCanFloat(true);
            this.pathNavigate.setSwimSpeedFactor(CITIZEN_SWIM_BONUS);
            this.pathNavigate.getPathingOptions().setEnterDoors(true); //Do they fit...
            this.pathNavigate.getPathingOptions().setCanOpenDoors(true);
            this.pathNavigate.setStuckHandler(PathingStuckHandler.createStuckHandler().withTeleportOnFullStuck().withTeleportSteps(5));
        }
        return pathNavigate;
    }

    public static AttributeSupplier.Builder getDefaultAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, Attributes.ATTACK_DAMAGE.value().getDefaultValue())
                .add(Attributes.FOLLOW_RANGE, 100.0);
    }
}
