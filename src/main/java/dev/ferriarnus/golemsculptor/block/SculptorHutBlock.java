package dev.ferriarnus.golemsculptor.block;

import com.minecolonies.core.tileentities.TileEntityColonyBuilding;
import dev.ferriarnus.golemsculptor.blockentity.BlockEntityRegistry;
import dev.ferriarnus.golemsculptor.building.BuildingRegistry;
import com.minecolonies.api.blocks.AbstractBlockHut;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SculptorHutBlock extends AbstractBlockHut<SculptorHutBlock> {
    @Override
    public String getHutName() {
        return "golem_sculptor";
    }

    @Override
    public BuildingEntry getBuildingEntry() {
        return BuildingRegistry.GOLEM_SCULPTOR.get();
    }

    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        TileEntityColonyBuilding building = BlockEntityRegistry.GOLEM_SCULPTOR.get().create(blockPos, blockState);
        building.registryName = this.getBuildingEntry().getRegistryName();
        return building;
    }

}
