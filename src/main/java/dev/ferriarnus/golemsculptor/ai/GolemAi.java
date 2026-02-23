package dev.ferriarnus.golemsculptor.ai;

import com.minecolonies.api.entity.ai.combat.threat.IThreatTableEntity;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.TickRateStateMachine;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.TickingTransition;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.api.util.Log;
import com.minecolonies.core.entity.pathfinding.navigation.EntityNavigationUtils;
import com.minecolonies.core.entity.pathfinding.pathresults.PathResult;
import dev.ferriarnus.golemsculptor.entity.SculptedGolemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.Random;

public class GolemAi extends Goal {

    private final SculptedGolemEntity golem;
    private final TickRateStateMachine<State> stateMachine = new TickRateStateMachine<>(State.INIT, this::handleAIException);

    //TODO is this ever not null?
    private PathResult attackPath;
    private int attacktimer = 0;
    private final Random randomGenerator = new Random();

    public GolemAi(SculptedGolemEntity golem) {
        this.golem = golem;
        this.stateMachine.addTransition(new TickingTransition<>(State.INIT, this::initialize, () -> State.RESTING, 10));
        this.stateMachine.addTransition(new TickingTransition<>(State.RESTING, this::hasTarget, () -> State.FIGHTING, 5));
        this.stateMachine.addTransition(new TickingTransition<>(State.RESTING, this::rally, () -> State.RESTING, 5));
        this.stateMachine.addTransition(new TickingTransition<>(State.RESTING, this::resting, () -> State.RESTING, 10));
        this.stateMachine.addTransition(new TickingTransition<>(State.FIGHTING, this::fighting, () -> State.RESTING, 5));
    }

    private void handleAIException(RuntimeException e) {
        Log.getLogger().error("GolemAI threw an exception:", e);
    }

    private boolean initialize() {
        return this.golem.getBuilding() != null;
    }

    private boolean hasTarget() {
        if (!golem.getBuilding().getColony().getRaiderManager().isRaided()) {
            return false;
        }
        //Don't attack allies
        if (this.golem.getTarget() != null && this.golem.getTarget().isAlive()) {
            this.golem.getTarget().setLastHurtByMob(this.golem);
            return true;
        } else {
            return false;
        }
    }

    private boolean fighting() {
        if (!golem.getBuilding().getColony().getRaiderManager().isRaided()) {
            return true;
        }
        if (this.golem.getTarget() != null && this.golem.getTarget().isAlive()) {
            if (this.attacktimer > 0) {
                --this.attacktimer;
            }

            if (this.attackPath == null || !this.attackPath.isInProgress()) {
                EntityNavigationUtils.walkToPos(this.golem, this.golem.getTarget().blockPosition(), false);
                this.golem.getLookControl().setLookAt(this.golem.getTarget());
            }

            int distance = BlockPosUtil.getMaxDistance2D(this.golem.blockPosition(), this.golem.getTarget().blockPosition());
            if (distance < 2 && this.attacktimer == 0) {
                this.golem.swing(InteractionHand.MAIN_HAND);
                this.golem.playSound(SoundEvents.IRON_GOLEM_ATTACK, 0.55F, 1.0F);
                this.golem.getTarget().hurt(this.golem.level().damageSources().mobAttack(this.golem), this.golem.getBuilding().getAttackDamage());
                //Taunt
                if (this.golem.getTarget() instanceof Mob mob) {
                    mob.setTarget(this.golem);
                    if (mob instanceof IThreatTableEntity threatTableEntity) {
                        threatTableEntity.getThreatTable().addThreat(this.golem, 5);
                    }
                }
                this.attacktimer = 5;
            } else if (distance > 50) {
                this.golem.setTarget(null);
                this.golem.getNavigation().stop();
                this.attackPath = null;
                return true;
            }

            return false;
        } else {
            this.golem.getNavigation().stop();
            this.attackPath = null;
            return true;
        }
    }

    private boolean rally() {
        if (!golem.getBuilding().getColony().getRaiderManager().isRaided()) {
            return true;
        }
        if (golem.getBuilding().getRallyLocation() != null) {
            EntityNavigationUtils.walkToPos(golem, golem.getBuilding().getRallyLocation().getInDimensionLocation().
                    offset(this.randomGenerator.nextInt(6) - 3, 0, this.randomGenerator.nextInt(6) - 3), 6, false, 1);
            golem.getBuilding().getPositionToFollow();
            return true;
        }
        return true;
    }

    private boolean resting() {
        if (golem.getBuilding().getColony().getRaiderManager().isRaided()) {
            return true;
        }
        BlockPos position = this.golem.getBuilding().getPosition(golem);
        if (position != null) {
            EntityNavigationUtils.walkToPos(this.golem, position, 1, true, 0.7);
        }
        return true;
    }

    @Override
    public boolean canUse() {
        return this.golem != null && this.golem.isAlive() && !this.golem.isInvisible() && this.golem.getBuilding() != null && this.golem.getState() == State.ALIVE;
    }

    @Override
    public boolean canContinueToUse() {
        this.stateMachine.tick();
        return this.golem != null && this.golem.isAlive() && !this.golem.isInvisible() && this.golem.getBuilding() != null && this.golem.getState() == State.ALIVE;
    }

    public enum State implements IState {
        INIT,
        RESTING,
        FIGHTING,
        FOLLOW,
        ALIVE,
        DEAD;
    }
}
