package dev.ferriarnus.golemsculptor.blockentity;

import com.minecolonies.api.tileentities.AbstractTileEntityColonyBuilding;
import com.minecolonies.core.tileentities.TileEntityColonyBuilding;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SculptorBlockEntity extends TileEntityColonyBuilding {
    public SculptorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.GOLEM_SCULPTOR.get(), pos, state);
    }

    public SculptorBlockEntity(BlockEntityType<? extends AbstractTileEntityColonyBuilding> type, BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.GOLEM_SCULPTOR.get(), pos, state);
    }
}
