package dev.ferriarnus.golemsculpter.blockentity;

import com.minecolonies.api.tileentities.AbstractTileEntityColonyBuilding;
import com.minecolonies.api.tileentities.TileEntityColonyBuilding;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SculptorBlockEntity extends TileEntityColonyBuilding {
    public SculptorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SCULPTOR.get(), pos, state);
    }

    public SculptorBlockEntity(BlockEntityType<? extends AbstractTileEntityColonyBuilding> type, BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.SCULPTOR.get(), pos, state);
    }
}
