package elecvrsn.GeneticBettas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TankGlassBlock extends Block implements SimpleWaterloggedBlock {
    protected static final VoxelShape FACE_NORTH = Block.box(0, 0, 14, 16, 16, 16);
    protected static final VoxelShape FACE_SOUTH = Block.box(0, 0, 0, 16, 16, 2);
    protected static final VoxelShape FACE_EAST = Block.box(0, 0, 0, 2, 16, 16);
    protected static final VoxelShape FACE_WEST = Block.box(14, 0, 0, 16, 16, 16);

    protected static final VoxelShape CORNER_SOUTHEAST = Block.box(0, 0, 0, 2, 16, 2);
    protected static final VoxelShape CORNER_SOUTHWEST = Block.box(14, 0, 0, 16, 16, 2);
    protected static final VoxelShape CORNER_NORTHEAST = Block.box(0, 0, 14, 2, 16, 16);
    protected static final VoxelShape CORNER_NORTHWEST = Block.box(14, 0, 14, 16, 16, 16);

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final BlockState baseState;

//    private static VoxelShape[] makeShapes(VoxelShape p_56934_, VoxelShape p_56935_, VoxelShape p_56936_, VoxelShape p_56937_, VoxelShape p_56938_) {
//        return IntStream.range(0, 16).mapToObj((p_56945_) -> {
//            return makeStairShape(p_56945_, p_56934_, p_56935_, p_56936_, p_56937_, p_56938_);
//        }).toArray((p_56949_) -> {
//            return new VoxelShape[p_56949_];
//        });
//    }

    private static VoxelShape makeStairShape(int p_56865_, VoxelShape p_56866_, VoxelShape p_56867_, VoxelShape p_56868_, VoxelShape p_56869_, VoxelShape p_56870_) {
        VoxelShape voxelshape = p_56866_;
        if ((p_56865_ & 1) != 0) {
            voxelshape = Shapes.or(p_56866_, p_56867_);
        }

        if ((p_56865_ & 2) != 0) {
            voxelshape = Shapes.or(voxelshape, p_56868_);
        }

        if ((p_56865_ & 4) != 0) {
            voxelshape = Shapes.or(voxelshape, p_56869_);
        }

        if ((p_56865_ & 8) != 0) {
            voxelshape = Shapes.or(voxelshape, p_56870_);
        }

        return voxelshape;
    }

    public TankGlassBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SHAPE, StairsShape.STRAIGHT).setValue(WATERLOGGED, Boolean.valueOf(false)));
        this.baseState = Blocks.AIR.defaultBlockState();
    }

    public boolean useShapeForLightOcclusion(BlockState p_56967_) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

        if (state.getValue(SHAPE) == StairsShape.OUTER_LEFT) {
            return switch (state.getValue(FACING)) {
                default -> Shapes.or(FACE_NORTH, FACE_WEST);
                case SOUTH -> Shapes.or(FACE_SOUTH, FACE_EAST);
                case EAST -> Shapes.or(FACE_EAST, FACE_NORTH);
                case WEST -> Shapes.or(FACE_WEST, FACE_SOUTH);
            };
        }
        else if (state.getValue(SHAPE) == StairsShape.OUTER_RIGHT) {
            return switch (state.getValue(FACING)) {
                default -> Shapes.or(FACE_NORTH, FACE_EAST);
                case SOUTH -> Shapes.or(FACE_SOUTH, FACE_WEST);
                case EAST -> Shapes.or(FACE_EAST, FACE_SOUTH);
                case WEST -> Shapes.or(FACE_WEST, FACE_NORTH);
            };
        }
        else if (state.getValue(SHAPE) == StairsShape.INNER_LEFT) {
            return switch (state.getValue(FACING)) {
                default -> CORNER_NORTHWEST;
                case SOUTH -> CORNER_SOUTHEAST;
                case EAST -> CORNER_NORTHEAST;
                case WEST -> CORNER_SOUTHWEST;
            };
        }
        else if (state.getValue(SHAPE) == StairsShape.INNER_RIGHT) {
            return switch (state.getValue(FACING)) {
                default -> CORNER_NORTHEAST;
                case SOUTH -> CORNER_SOUTHWEST;
                case EAST -> CORNER_SOUTHEAST;
                case WEST -> CORNER_NORTHWEST;
            };
        }
        return switch (state.getValue(FACING)) {
            case NORTH -> FACE_NORTH;
            case SOUTH -> FACE_SOUTH;
            case EAST -> FACE_EAST;
            case WEST -> FACE_WEST;
            default -> FACE_NORTH;
        };

    }


    public void onPlace(BlockState p_56961_, Level p_56962_, BlockPos p_56963_, BlockState p_56964_, boolean p_56965_) {
        if (!p_56961_.is(p_56961_.getBlock())) {
            this.baseState.neighborChanged(p_56962_, p_56963_, Blocks.AIR, p_56963_, false);
        }
    }

    public void onRemove(BlockState p_56908_, Level p_56909_, BlockPos p_56910_, BlockState p_56911_, boolean p_56912_) {
        if (!p_56908_.is(p_56911_.getBlock())) {
            this.baseState.onRemove(p_56909_, p_56910_, p_56911_, p_56912_);
        }
    }


    public InteractionResult use(BlockState p_56901_, Level p_56902_, BlockPos p_56903_, Player p_56904_, InteractionHand p_56905_, BlockHitResult p_56906_) {
        return this.baseState.use(p_56902_, p_56904_, p_56905_, p_56906_);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_56872_) {
        Direction direction = p_56872_.getClickedFace();
        BlockPos blockpos = p_56872_.getClickedPos();
        FluidState fluidstate = p_56872_.getLevel().getFluidState(blockpos);
        BlockState blockstate = this.defaultBlockState().setValue(FACING, p_56872_.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
        return blockstate.setValue(SHAPE, getStairsShape(blockstate, p_56872_.getLevel(), blockpos));
    }

    public BlockState updateShape(BlockState p_56925_, Direction p_56926_, BlockState p_56927_, LevelAccessor p_56928_, BlockPos p_56929_, BlockPos p_56930_) {
        if (p_56925_.getValue(WATERLOGGED)) {
            p_56928_.scheduleTick(p_56929_, Fluids.WATER, Fluids.WATER.getTickDelay(p_56928_));
        }

        return p_56926_.getAxis().isHorizontal() ? p_56925_.setValue(SHAPE, getStairsShape(p_56925_, p_56928_, p_56929_)) : super.updateShape(p_56925_, p_56926_, p_56927_, p_56928_, p_56929_, p_56930_);
    }

    private static StairsShape getStairsShape(BlockState p_56977_, BlockGetter p_56978_, BlockPos p_56979_) {
        Direction direction = p_56977_.getValue(FACING);
        BlockState blockstate = p_56978_.getBlockState(p_56979_.relative(direction));
        if (isTankGlass(blockstate)) {
            Direction direction1 = blockstate.getValue(FACING);
            if (direction1.getAxis() != p_56977_.getValue(FACING).getAxis() && canTakeShape(p_56977_, p_56978_, p_56979_, direction1.getOpposite())) {
                if (direction1 == direction.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }

                return StairsShape.OUTER_RIGHT;
            }
        }

        BlockState blockstate1 = p_56978_.getBlockState(p_56979_.relative(direction.getOpposite()));
        if (isTankGlass(blockstate1)) {
            Direction direction2 = blockstate1.getValue(FACING);
            if (direction2.getAxis() != p_56977_.getValue(FACING).getAxis() && canTakeShape(p_56977_, p_56978_, p_56979_, direction2)) {
                if (direction2 == direction.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }

                return StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState p_56971_, BlockGetter p_56972_, BlockPos p_56973_, Direction p_56974_) {
        BlockState blockstate = p_56972_.getBlockState(p_56973_.relative(p_56974_));
        return !isTankGlass(blockstate) || blockstate.getValue(FACING) != p_56971_.getValue(FACING);
    }

    public static boolean isTankGlass(BlockState p_56981_) {
        return p_56981_.getBlock() instanceof TankGlassBlock;
    }

    public BlockState rotate(BlockState p_56922_, Rotation p_56923_) {
        return p_56922_.setValue(FACING, p_56923_.rotate(p_56922_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_56919_, Mirror p_56920_) {
        Direction direction = p_56919_.getValue(FACING);
        StairsShape stairsshape = p_56919_.getValue(SHAPE);
        switch(p_56920_) {
            case LEFT_RIGHT:
                if (direction.getAxis() == Direction.Axis.Z) {
                    switch(stairsshape) {
                        case INNER_LEFT:
                            return p_56919_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_RIGHT:
                            return p_56919_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_LEFT:
                            return p_56919_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return p_56919_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        default:
                            return p_56919_.rotate(Rotation.CLOCKWISE_180);
                    }
                }
                break;
            case FRONT_BACK:
                if (direction.getAxis() == Direction.Axis.X) {
                    switch(stairsshape) {
                        case INNER_LEFT:
                            return p_56919_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT:
                            return p_56919_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT:
                            return p_56919_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return p_56919_.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT:
                            return p_56919_.rotate(Rotation.CLOCKWISE_180);
                    }
                }
        }

        return super.mirror(p_56919_, p_56920_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_56932_) {
        p_56932_.add(FACING, SHAPE, WATERLOGGED);
    }

    public FluidState getFluidState(BlockState p_56969_) {
        return p_56969_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_56969_);
    }

    public boolean isPathfindable(BlockState p_56891_, BlockGetter p_56892_, BlockPos p_56893_, PathComputationType p_56894_) {
        return false;
    }
}
