package com.example.examplemod.building;

import com.example.examplemod.entity.SculptedGolem;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.coremod.colony.buildings.AbstractBuilding;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import org.codehaus.plexus.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BuildingSculptor extends AbstractBuilding {

    private final Map<UUID, BlockPos> entityUUID = new HashMap<>();
    private final List<BlockPos> allowedPositions = new ArrayList<>();

    protected BuildingSculptor(@NotNull IColony colony, BlockPos pos) {
        super(colony, pos);
        allowedPositions.add(getPosition());
    }

    @Override
    public String getSchematicName() {
        return "sculptor";
    }

    public void addGolem(SculptedGolem entity) {
        if (!entityUUID.containsKey(entity.getUUID())) {
            Optional<BlockPos> pos = CollectionUtils.subtract(allowedPositions, entityUUID.values()).stream().findFirst();
            pos.ifPresent(blockPos -> entityUUID.put(entity.getUUID(), blockPos));
        }
    }


    public List<SculptedGolem> getGolems() {
        List<SculptedGolem> golems = new ArrayList<>();
        if (colony.getWorld() instanceof ServerLevel serverLevel) {
            for (UUID uuid: entityUUID.keySet()) {
                Entity entity = serverLevel.getEntity(uuid);
                if (entity instanceof SculptedGolem golem) {
                    golems.add(golem);
                } else {
                    entityUUID.remove(uuid);
                }
            }
        }
        return golems;
    }

    public Set<UUID> getEntityUUID() {
        return entityUUID.keySet();
    }

    public void removeGolem(SculptedGolem golem) {
        entityUUID.remove(golem.getUUID());
    }

    public List<ItemStack> getGolemItems() {
        return List.of(new ItemStack(Blocks.STONE, 6), new ItemStack(Items.COPPER_INGOT,2));
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        super.deserializeNBT(compound);
        CompoundTag golems = compound.getCompound("Golems");
        int size = golems.getInt("Size");
        for (int i = 0; i < size; i++) {
            CompoundTag golemsCompound = golems.getCompound(i + "");
            UUID uuid = golemsCompound.getUUID("UUID");
            long pos = golemsCompound.getLong("Pos");
            entityUUID.put(uuid, BlockPos.of(pos));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag golems = new CompoundTag();
        CompoundTag tag = super.serializeNBT();
        golems.putInt("Size", entityUUID.size());
        int i = 0;
        for (Map.Entry<UUID, BlockPos> entry: entityUUID.entrySet()) {
            CompoundTag uuid = new CompoundTag();
            uuid.putUUID("UUID", entry.getKey());
            uuid.putLong("Pos", entry.getValue().asLong());
            golems.put(i + "", uuid);
            i++;
        }
        tag.put("Golems", golems);
        return tag;

    }

    public boolean canMakeGolem() {
        return entityUUID.size() < getBuildingLevel();
    }
}
