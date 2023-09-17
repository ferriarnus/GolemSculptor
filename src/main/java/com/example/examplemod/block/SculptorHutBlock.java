package com.example.examplemod.block;

import com.example.examplemod.blockentity.BlockEntityRegistry;
import com.example.examplemod.building.BuildingsRegistry;
import com.minecolonies.api.blocks.AbstractBlockHut;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.tileentities.TileEntityColonyBuilding;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SculptorHutBlock extends AbstractBlockHut<SculptorHutBlock> {
    @Override
    public String getHutName() {
        return "sculptor";
    }

    @Override
    public BuildingEntry getBuildingEntry() {
        return BuildingsRegistry.SCULPTOR.get();
    }

    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        TileEntityColonyBuilding building = BlockEntityRegistry.SCULPTOR.get().create(blockPos, blockState);
        building.registryName = this.getBuildingEntry().getRegistryName();
        return building;
    }

}
