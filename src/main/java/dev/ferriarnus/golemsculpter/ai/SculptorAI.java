package dev.ferriarnus.golemsculpter.ai;

import dev.ferriarnus.golemsculpter.building.BuildingSculptor;
import dev.ferriarnus.golemsculpter.entity.EntityRegistry;
import dev.ferriarnus.golemsculpter.entity.SculptedGolem;
import dev.ferriarnus.golemsculpter.job.JobSculptor;
import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.coremod.entity.ai.basic.AbstractEntityAISkill;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        List<ItemStack> golemItems = building.getGolemItems();
        if (!building.canMakeGolem()) {
            return AIWorkerState.IDLE;
        }
        boolean hasItems = true;
        for (ItemStack itemStack : golemItems) {
            if (InventoryUtils.getItemCountInItemHandler((worker.getInventoryCitizen()),
                    (ItemStack stack) -> ItemStack.matches(stack, itemStack)) < itemStack.getCount()) {
                hasItems = false;
                break;
            }
        }
        if (!hasItems) {
            checkIfRequestForItemExistOrCreateAsync(golemItems);
        } else {
            return ModWorkStates.SCULPTER_WORK;
        }
        return AIWorkerState.DECIDE;
    }

    private IAIState createGolem() {
        boolean hasItems = true;
        for (ItemStack itemStack : building.getGolemItems()) {
            if (InventoryUtils.getItemCountInItemHandler((worker.getInventoryCitizen()),
                    (ItemStack stack) -> ItemStack.matches(stack, itemStack)) < itemStack.getCount()) {
                hasItems = false;
                break;
            }
        }
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
