package dev.ferriarnus.golemsculpter.building;

import dev.ferriarnus.golemsculpter.entity.GolemType;
import dev.ferriarnus.golemsculpter.entity.SculptedGolem;
import com.google.common.collect.EnumHashBiMap;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.coremod.colony.buildings.AbstractBuilding;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BuildingSculptor extends AbstractBuilding {

    private final EnumHashBiMap<GolemType, UUID> entitymap = EnumHashBiMap.create(GolemType.class);
    private final EnumHashBiMap<GolemType, BlockPos> posmap = EnumHashBiMap.create(GolemType.class);

    protected BuildingSculptor(@NotNull IColony colony, BlockPos pos) {
        super(colony, pos);
    }

    @Override
    public String getSchematicName() {
        return "sculptor";
    }

    public void addGolem(SculptedGolem entity) {
        if (!entitymap.containsValue(entity.getUUID())) {
            for (GolemType type : GolemType.values()) {
                if (!entitymap.containsKey(type)){
                    entitymap.put(type, entity.getUUID());
                    break;
                }
            }
        }
    }


    public List<SculptedGolem> getGolems() {
        List<SculptedGolem> golems = new ArrayList<>();
        if (colony.getWorld() instanceof ServerLevel serverLevel) {
            for (UUID uuid: entitymap.values()) {
                Entity entity = serverLevel.getEntity(uuid);
                if (entity instanceof SculptedGolem golem) {
                    golems.add(golem);
                } else {
                    entitymap.inverse().remove(uuid);
                }
            }
        }
        return golems;
    }

    public Collection<UUID> getEntityUUID() {
        return entitymap.values();
    }

    public void removeGolem(SculptedGolem golem) {
        if (entitymap.containsValue(golem.getUUID())) {
            GolemType type = entitymap.inverse().remove(golem.getUUID());
            entitymap.remove(type);
        }
    }

    public List<ItemStack> getGolemItems() {
        for (int i = 0; i < getBuildingLevel(); i++) {
            GolemType type = GolemType.values()[i];
            if (!entitymap.containsKey(type)) {
                return List.of(new ItemStack(type.main, 6), new ItemStack(type.repair, 6));
            }
        }
        return List.of();
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        super.deserializeNBT(compound);
        CompoundTag golems = compound.getCompound("Golems");
        int size = golems.getInt("Size");
        for (int i = 0; i < size; i++) {
            CompoundTag golemsCompound = golems.getCompound(i + "");
            GolemType type = GolemType.values()[golemsCompound.getInt("Type")];
            UUID uuid = golemsCompound.getUUID("UUID");
            entitymap.put(type, uuid);
        }
        for (int i = 0; i < getBuildingLevel(); i++) {
            GolemType type = GolemType.values()[i];
            posmap.put(type, BlockPosUtil.readOrNull(compound, type.name()));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag golems = new CompoundTag();
        CompoundTag tag = super.serializeNBT();
        golems.putInt("Size", entitymap.size());
        int i = 0;
        for (Map.Entry<GolemType, UUID> entry: entitymap.entrySet()) {
            CompoundTag uuid = new CompoundTag();
            uuid.putInt("Type", entry.getKey().ordinal());
            uuid.putUUID("UUID", entry.getValue());
            golems.put(i + "", uuid);
            i++;
        }
        tag.put("Golems", golems);
        for (int j = 0; j < getBuildingLevel(); j++) {
            GolemType type = GolemType.values()[i];
            tag.putLong(type.name(), posmap.get(type).asLong());
        }
        return tag;

    }

    public boolean canMakeGolem() {
        return posmap.size() < getBuildingLevel();
    }
}
