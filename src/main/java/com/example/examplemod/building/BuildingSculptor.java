package com.example.examplemod.building;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.coremod.colony.buildings.AbstractBuilding;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildingSculptor extends AbstractBuilding {

    private List<LivingEntity> entities = new ArrayList<>();
    private List<UUID> entityUUID = new ArrayList<>();

    protected BuildingSculptor(@NotNull IColony colony, BlockPos pos) {
        super(colony, pos);
    }

    @Override
    public String getSchematicName() {
        return "sculptor";
    }

    public void addGolem(LivingEntity entity) {
        entities.add(entity);
        entityUUID.add(entity.getUUID());
    }


    public List<LivingEntity> getGolems() {
        for (LivingEntity entity: entities) {
            if (entity.isDeadOrDying()) {
                entityUUID.remove(entity.getUUID());
            }
        }
        List<LivingEntity> entityList = getColony().getWorld().getEntitiesOfClass(LivingEntity.class, new AABB(getPosition()).inflate(10), (e) -> entityUUID.contains(e.getUUID()));
        entities = entityList;
        return entityList;
    }

    public List<UUID> getEntityUUID() {
        return entityUUID;
    }

    public void removeGolem(Entity golem) {
        entities.remove(golem);
        entityUUID.remove(golem.getUUID());
    }

    public List<ItemStack> getGolemItems() {
        return List.of(new ItemStack(Blocks.STONE, 6), new ItemStack(Items.COPPER_INGOT,2));
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        super.deserializeNBT(compound);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        return tag;

    }
}
