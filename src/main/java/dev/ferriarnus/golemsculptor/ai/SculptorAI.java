package dev.ferriarnus.golemsculptor.ai;

import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIInteract;
import dev.ferriarnus.golemsculptor.building.BuildingSculptor;
import dev.ferriarnus.golemsculptor.entity.EntityRegistry;
import dev.ferriarnus.golemsculptor.entity.GolemType;
import dev.ferriarnus.golemsculptor.entity.SculptedGolemEntity;
import dev.ferriarnus.golemsculptor.job.JobSculptor;
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
                new AITarget<IAIState>(ModWorkStates.SCULPTOR_WORK, this::createGolem, 20),
                new AITarget<IAIState>(ModWorkStates.GOLEM_REPAIR, this::repair, 20));
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
            GolemType repair = building.canRepair();
            if (repair != null) {
                if (checkIfRequestForItemExistOrCreate(new ItemStack(repair.repair, 1))){
                    Predicate<ItemStack> predicate = (ItemStack s) -> ItemStackUtils.compareItemStacksIgnoreStackSize(s, new ItemStack(repair.repair));
                    //Missing items found, gather them
                    if (InventoryUtils.getItemCountInItemHandler(worker.getInventoryCitizen(), predicate) < 1) {
                        needsCurrently = new Tuple<>(predicate, 1);
                        return AIWorkerState.GATHERING_REQUIRED_MATERIALS;
                    }
                }

                return ModWorkStates.GOLEM_REPAIR;
            }
            return AIWorkerState.IDLE;
        }

        //items are in the inv or building, or a request is made
        if (checkIfRequestForItemExistOrCreate(building.getGolemItems(type))) {
            for (ItemStack item : building.getGolemItems(type)) {
                Predicate<ItemStack> predicate = (ItemStack s) -> ItemStackUtils.compareItemStacksIgnoreStackSize(s, item);
                //Missing items found, gather them
                if (InventoryUtils.getItemCountInItemHandler(worker.getInventoryCitizen(), predicate) < item.getCount()) {
                    needsCurrently = new Tuple<>(predicate, item.getCount());
                    return AIWorkerState.GATHERING_REQUIRED_MATERIALS;
                }
            }

            return ModWorkStates.SCULPTOR_WORK;
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

        SculptedGolemEntity entity = EntityRegistry.SCULPTED_GOLEM.get().create(worker.level());
        entity.setGolemType(type);
        entity.setBuilding(building);
        building.addGolem(entity);
        entity.setPos(building.getPosition(entity).getCenter());
        worker.level().addFreshEntity(entity);
        return AIWorkerState.IDLE;
    }

    private IAIState repair() {
        GolemType type = building.canRepair();
        if (type == null) {
            return AIWorkerState.DECIDE;
        }

        Predicate<ItemStack> predicate = (ItemStack s) -> ItemStackUtils.compareItemStacksIgnoreStackSize(s, new ItemStack(type.repair));
        //Shouldn't happen, but if there are not enough items stop
        if (InventoryUtils.getItemCountInItemHandler(getInventory(), predicate) < 1) {
            return AIWorkerState.IDLE;
        }

        final int slot = worker.getCitizenInventoryHandler().findFirstSlotInInventoryWith(type.repair);
        getInventory().extractItem(slot, 1, false);

        SculptedGolemEntity golem = building.getGolem(type);
        if (golem == null) {
            return AIWorkerState.IDLE;
        }

        golem.heal(golem.getMaxHealth() * 0.25f);
        float hp = golem.getHealth()  / golem.getMaxHealth();
        if (hp >= 1.0 - building.getBuildingLevel() * 0.05) {
            golem.setHealth(golem.getMaxHealth());
        }

        return AIWorkerState.IDLE;
    }

    @Override
    public Class<BuildingSculptor> getExpectedBuildingClass() {
        return BuildingSculptor.class;
    }
}
