package net.fabricmc.smphack.Hacks.Scaffold;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.stream.Stream;

public enum BlockUtils
{;
    private static final MinecraftClient MC = MinecraftClient.getInstance();

    public static BlockState getState(BlockPos pos)
    {
        assert MC.world != null;
        return MC.world.getBlockState(pos);
    }
    private static VoxelShape getOutlineShape(BlockPos pos)
    {
        return getState(pos).getOutlineShape(MC.world, pos);
    }

    public static boolean canBeClicked(BlockPos pos)
    {
        return getOutlineShape(pos) != VoxelShapes.empty();
    }

    public static Stream<BlockPos> getAllInBoxStream(BlockPos from, BlockPos to)
    {
        BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()),
                Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()),
                Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));

        Stream<BlockPos> stream = Stream.<BlockPos> iterate(min, pos -> {

            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            x++;

            if(x > max.getX())
            {
                x = min.getX();
                y++;
            }

            if(y > max.getY())
            {
                y = min.getY();
                z++;
            }

            if(z > max.getZ())
                throw new IllegalStateException("Stream limit didn't work.");

            return new BlockPos(x, y, z);
        });

        int limit = (max.getX() - min.getX() + 1)
                * (max.getY() - min.getY() + 1) * (max.getZ() - min.getZ() + 1);

        return stream.limit(limit);
    }
}
