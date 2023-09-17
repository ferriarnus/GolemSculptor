package com.example.examplemod.ai;

import com.minecolonies.api.entity.ai.statemachine.states.IAIState;

public enum ModWorkStates implements IAIState {
    SCULPTER_WORK(true);

    boolean isOkayToEat;

    ModWorkStates(boolean okayToEat) {
        this.isOkayToEat = okayToEat;
    }

    @Override
    public boolean isOkayToEat() {
        return isOkayToEat;
    }
}
