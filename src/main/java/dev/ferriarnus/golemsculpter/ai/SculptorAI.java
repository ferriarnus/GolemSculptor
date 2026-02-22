package dev.ferriarnus.golemsculpter.ai;

import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIInteract;
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

import java.util.function.Predicate;

public class SculptorAI extends AbstractEntityAIInteract<JobSculptor, BuildingSculptor> {

    public SculptorAI(@NotNull JobSculptor job) {
        super(job);
        super.registerTargets(
                new AITarget<>(AIWorkerState.IDLE, AIWorkerState.START_WORKING, 1),
                new AITarget<IAIState>(AIWorkerState.START_WORKING, this::startWorkingAtOwnBuilding, 20),
                new AITarget<IAIState>(AIWorkerState.DECIDE, this::prepare, 40),
                new AITarget<IAIState>(ModWorkStates.SCULPTER_WORK, this::createGolem, 20));
    }

    private IAIState startWorkingAtOwnBuilding() {
        if (!walkToBuilding()) {
            return getState();
        }
        return AIWorkerState.DECIDE;
    }

    private IAIState prepare() {
        GolemType type = building.canMakeGolem();
        if (type == null) {
            return AIWorkerState.IDLE;
        }

        boolean found = true;
        //items are in the inv or building, or a request is made
        for (ItemStack item : building.getGolemItems(type)) {
            found = found && checkIfRequestForItemExistOrCreateAsync(item);
        }

        //All items are in the inv
        if (found) {
            //Check the inv
            for (ItemStack item : building.getGolemItems(type)) {
                Predicate<ItemStack> predicate = (ItemStack s) -> ItemStackUtils.compareItemStacksIgnoreStackSize(s, item);
                //Missing items found, gather them
                if (InventoryUtils.getItemCountInItemHandler(worker.getInventoryCitizen(), predicate) < item.getCount()) {
                    needsCurrently = new Tuple<>(predicate, item.getCount());
                    return AIWorkerState.GATHERING_REQUIRED_MATERIALS;
                }
            }
            return ModWorkStates.SCULPTER_WORK;
        }

        //Items aren't ready, request is made to get them
        return AIWorkerState.IDLE;
    }

    @Override
    public IAIState getStateAfterPickUp() {
        return AIWorkerState.DECIDE;
    }

    private IAIState createGolem() {
        GolemType type = building.canMakeGolem();
        if (type == null) {
            return AIWorkerState.DECIDE;
        }
        for (ItemStack item : building.getGolemItems(type)) {
            //Consume all needed items
            int count = item.getCount();
            Predicate<ItemStack> predicate = (ItemStack s) -> ItemStackUtils.compareItemStacksIgnoreStackSize(s, item);
            //Shouldn't happen, but if there are not enough items stop
            if (InventoryUtils.getItemCountInItemHandler(getInventory(), predicate) < count) {
                return AIWorkerState.IDLE;
            }
            while (count > 0) {
                final int slot = worker.getCitizenInventoryHandler().findFirstSlotInInventoryWith(item.getItem());
                ItemStack result = getInventory().extractItem(slot, item.getCount(), false);
                count -= result.getCount();
            }
        }
        SculptedGolemEntity entity = EntityRegistry.GOLEM.get().create(worker.level());
        entity.setGolemType(type);
        entity.setBuilding(building);
        building.addGolem(entity);
        entity.setPos(building.getPosition(entity).getCenter());
        worker.level().addFreshEntity(entity);
        return AIWorkerState.DECIDE;
    }

    @Override
    public Class<BuildingSculptor> getExpectedBuildingClass() {
        return BuildingSculptor.class;
    }
}
