package dev.ferriarnus.golemsculptor.ai;

import com.minecolonies.api.entity.ai.statemachine.states.IAIState;

public enum ModWorkStates implements IAIState {
    SCULPTOR_WORK(true),
    GOLEM_REPAIR(true);

    boolean isOkayToEat;

    ModWorkStates(boolean okayToEat) {
        this.isOkayToEat = okayToEat;
    }

    @Override
    public boolean isOkayToEat() {
        return isOkayToEat;
    }
}
