package com.example.examplemod.ai;

import com.example.examplemod.building.BuildingSculptor;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.entity.SculptedGolem;
import com.example.examplemod.job.JobSculptor;
import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.coremod.entity.ai.basic.AbstractEntityAISkill;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SculptorAI extends AbstractEntityAISkill<JobSculptor, BuildingSculptor> {

    public SculptorAI(@NotNull JobSculptor job) {
        super(job);
        super.registerTargets(
                new AITarget<>(AIWorkerState.IDLE, AIWorkerState.START_WORKING, 1),
                new AITarget<IAIState>(AIWorkerState.START_WORKING, this::startWorkingAtOwnBuilding, 20),
                new AITarget<IAIState>(AIWorkerState.DECIDE, this::prepare, 40),
                new AITarget<IAIState>(ModWorkStates.SCULPTER_WORK, this::createGolem, 20));
    }

    private IAIState startWorkingAtOwnBuilding() {
        return this.walkToBuilding() ? this.getState() : AIWorkerState.DECIDE;
    }

    private IAIState prepare() {
        if (!building.getGolems().isEmpty()) {
            return AIWorkerState.IDLE;
        }
        final boolean hasItems =
                InventoryUtils.getItemCountInItemHandler((worker.getInventoryCitizen()),
                        (ItemStack stack) -> ItemStackUtils.compareItemStackListIgnoreStackSize(building.getGolemItems(), stack)) > 1;
        if (!hasItems) {
            checkIfRequestForItemExistOrCreateAsync(building.getGolemItems());
        } else {
            return ModWorkStates.SCULPTER_WORK;
        }
        return AIWorkerState.DECIDE;
    }

    private IAIState createGolem() {
        final boolean hasItems =
                InventoryUtils.getItemCountInItemHandler((worker.getInventoryCitizen()),
                        (ItemStack stack) -> ItemStackUtils.compareItemStackListIgnoreStackSize(building.getGolemItems(), stack)) > 1;
        if (hasItems && building.canMakeGolem()) {
            SculptedGolem entity = EntityRegistry.GOLEM.get().create(worker.level());
            building.addGolem(entity);
            entity.setBuilding(building);
            entity.setPos(building.getPosition().above().getCenter());
            worker.level().addFreshEntity(entity);
        }
        return AIWorkerState.DECIDE;
    }

    @Override
    public Class<BuildingSculptor> getExpectedBuildingClass() {
        return BuildingSculptor.class;
    }
}
