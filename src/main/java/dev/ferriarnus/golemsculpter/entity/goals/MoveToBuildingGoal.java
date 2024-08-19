package dev.ferriarnus.golemsculpter.entity.goals;

import dev.ferriarnus.golemsculpter.entity.SculptedGolem;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class MoveToBuildingGoal extends RandomStrollGoal {

    private final SculptedGolem golem;

    public MoveToBuildingGoal(SculptedGolem pMob, double pSpeedModifier, boolean pCheckNoActionTime) {
        super(pMob, pSpeedModifier, 10, pCheckNoActionTime);
        golem = pMob;
    }

    public boolean canUse() {
        return !golem.building.getColony().getRaiderManager().isRaided() && super.canUse();
    }

    @Nullable
    protected Vec3 getPosition() {
        return golem.building.getPosition().above().getCenter();
    }
}
