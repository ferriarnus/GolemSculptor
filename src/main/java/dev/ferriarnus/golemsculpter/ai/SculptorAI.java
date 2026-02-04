package dev.ferriarnus.golemsculpter.ai;

import com.minecolonies.core.entity.ai.workers.AbstractEntityAISkill;
import dev.ferriarnus.golemsculpter.building.BuildingSculptor;
import dev.ferriarnus.golemsculpter.entity.EntityRegistry;
import dev.ferriarnus.golemsculpter.entity.GolemType;
import dev.ferriarnus.golemsculpter.entity.SculptedGolemEntity;
import dev.ferriarnus.golemsculpter.job.JobSculptor;
import com.minecolonies.api.entity.ai.statemachine.AITarget;
import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.util.InventoryUtils;
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
        GolemType type = building.canMakeGolem();
        if (type == null) {
            return AIWorkerState.IDLE;
        }
        List<ItemStack> golemItems = building.getGolemItems(type);
        ItemStack missingItem = ItemStack.EMPTY;
        for (ItemStack itemStack : golemItems) {
            if (InventoryUtils.getItemCountInItemHandler((worker.getInventoryCitizen()),
                    (ItemStack stack) -> ItemStack.isSameItemSameComponents(stack, itemStack)) >= itemStack.getCount()) {
                break;
            }
            missingItem = itemStack;
        }
        if (!missingItem.isEmpty()) {
            checkIfRequestForItemExistOrCreateAsync(missingItem);
        } else {
            return ModWorkStates.SCULPTER_WORK;
        }
        return AIWorkerState.DECIDE;
    }

    private IAIState createGolem() {
        boolean missingItem = false;
        GolemType type = building.canMakeGolem();
        if (type == null) {
            return AIWorkerState.DECIDE;
        }
        for (ItemStack itemStack : building.getGolemItems(type)) {
            if (InventoryUtils.getItemCountInItemHandler((worker.getInventoryCitizen()),
                    (ItemStack stack) -> ItemStack.isSameItemSameComponents(stack, itemStack)) >= itemStack.getCount()) {
                break;
            }
            missingItem = true;
        }
        if (!missingItem) {
            SculptedGolemEntity entity = EntityRegistry.GOLEM.get().create(worker.level());
            entity.setBuilding(building);
            entity.setPos(building.getPosition().above().getCenter());
            entity.setGolemType(type);
            building.addGolem(entity);
            worker.level().addFreshEntity(entity);
        }
        return AIWorkerState.DECIDE;
    }

    @Override
    public Class<BuildingSculptor> getExpectedBuildingClass() {
        return BuildingSculptor.class;
    }
}
