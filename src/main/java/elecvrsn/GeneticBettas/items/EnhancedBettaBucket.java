package elecvrsn.GeneticBettas.items;

import elecvrsn.GeneticBettas.block.DisplayTankBlock;
import elecvrsn.GeneticBettas.block.FilledDisplayTankBlock;
import elecvrsn.GeneticBettas.block.entity.FilledDisplayTankBlockEntity;
import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import mokiyoki.enhancedanimals.util.Genes;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;
import static elecvrsn.GeneticBettas.util.AddonReference.BETTA_AUTOSOMAL_GENES_LENGTH;

public class EnhancedBettaBucket extends MobBucketItem {

    public EnhancedBettaBucket(Properties properties, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        super(entitySupplier, fluidSupplier, soundSupplier, properties);
    }

    public static void setEquipment(ItemStack stack, ItemStack equipment) {
        if (equipment != ItemStack.EMPTY) {
            stack.getOrCreateTagElement("display").put("collar", equipment.save(new CompoundTag()));
        }
    }

    public static void setGenes(ItemStack stack, Genes genes) {
        if (genes != null) {
            stack.getOrCreateTagElement("Genetics").putIntArray("SGenes", genes.getSexlinkedGenes());
            stack.getOrCreateTagElement("Genetics").putIntArray("AGenes", genes.getAutosomalGenes());
        }
    }

    public static Optional<Boolean> getIsFemale(ItemStack stack) {
        String uuid = stack.getOrCreateTagElement("display").getString("UUID");
        return uuid.isEmpty() ? Optional.empty() : Optional.of(uuid.toCharArray()[0] - 48 < 8);
    }

    public Genes getGenes(ItemStack stack) {
        CompoundTag genetics = stack.getOrCreateTagElement("Genetics");
        return new Genes(genetics.getIntArray("SGenes"), genetics.getIntArray("AGenes"));
    }

    public static void setParentNames(ItemStack stack, String sireName, String damName) {
        stack.getOrCreateTagElement("display").putString("SireName", sireName);
        stack.getOrCreateTagElement("display").putString("DamName", damName);
    }

    public static void setMateGenes(ItemStack stack, Genes genes, boolean isFemale) {
        if (genes != null) {
            stack.getOrCreateTagElement("MateGenetics").putIntArray("SGenes", genes.getSexlinkedGenes());
            stack.getOrCreateTagElement("MateGenetics").putIntArray("AGenes", genes.getAutosomalGenes());
            stack.getOrCreateTagElement("MateGenetics").putBoolean("MateIsFemale", isFemale);
        }
    }

    private Genes getMateGenes(ItemStack stack) {
        CompoundTag genetics = stack.getOrCreateTagElement("MateGenetics");
        return new Genes(genetics.getIntArray("SGenes"), genetics.getIntArray("AGenes"));
    }

    public static void setBettaUUID(ItemStack stack, String uuid) {
        stack.getOrCreateTagElement("display").putString("UUID", uuid);
    }

    private boolean getMateIsFemale(ItemStack stack) {
        return stack.getOrCreateTagElement("MateGenetics").getBoolean("MateIsFemale");
    }

    public static void setBirthTime(ItemStack stack, String birthTime) {
        stack.getOrCreateTagElement("display").putString("BirthTime", birthTime);
    }

    @Override
    public void checkExtraContent(@Nullable Player player, Level level, ItemStack stack, BlockPos pos) {
        if (level instanceof ServerLevel) {
            this.spawnBetta((ServerLevel)level, stack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    private void spawnBetta(ServerLevel level, ItemStack stack, BlockPos pos) {
        EnhancedBetta betta = ENHANCED_BETTA.get().create(level);
        if (betta == null) {
            return;
        }
        betta.setFromBucket(true);
        CompoundTag data = stack.getOrCreateTagElement("display");
        if (!data.getString("UUID").isEmpty() && level.getEntity(UUID.fromString(data.getString("UUID"))) == null) {
            betta.setUUID(UUID.fromString(data.getString("UUID")));
        }
        betta.setSireName(data.getString("SireName"));
        betta.setDamName(data.getString("DamName"));
        if (this.getGenes(stack) != null) {
            Genes genes = this.getGenes(stack);
            if (genes.getNumberOfAutosomalGenes() == 0) { // No existing genes? let's just use a breed preset
                genes = betta.createInitialBreedGenes(betta.getCommandSenderWorld(), betta.blockPosition(), "WanderingTrader");
                betta.setGenes(genes);
                betta.setSharedGenes(genes);
            }
            else if (genes.getNumberOfAutosomalGenes() != BETTA_AUTOSOMAL_GENES_LENGTH) {
                int[] newAGenes = new int[BETTA_AUTOSOMAL_GENES_LENGTH];
                System.arraycopy(genes.getAutosomalGenes(), 0, newAGenes, 0, genes.getNumberOfAutosomalGenes());
                genes.setGenes(newAGenes);
            }

            if (!genes.isValid() && genes.getNumberOfAutosomalGenes() != 0) {
                genes.fixGenes(1);
            }
            betta.setGenes(genes);
            betta.setSharedGenes(genes);

            genes = this.getMateGenes(stack);
            if (genes.isValid() && genes.getSexlinkedGenes().length > 0 && genes.getAutosomalGenes().length > 0) {
                betta.setMateGender(this.getMateIsFemale(stack));
                betta.setMateGenes(this.getMateGenes(stack));
                if (getIsFemale(stack).orElse(false)) betta.setHasEgg(true);
            }
        }
        if (data.contains("collar")) {
            CompoundTag collar = data.getCompound("collar");
            betta.getEnhancedInventory().setItem(1, ItemStack.of(collar));
        }
        if (stack.hasCustomHoverName()) {
            betta.setCustomName(stack.getHoverName());
        }
        betta.setBirthTime(data.getString("BirthTime"));
        betta.initilizeAnimalSize();
        betta.loadFromBucketTag(stack.getOrCreateTag());
        betta.moveTo((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
        level.addFreshEntity(betta);
    }

    public static EnhancedBetta spawnBettaFromTag(Level level, CompoundTag tag, BlockPos pos) {
        EnhancedBetta betta = ENHANCED_BETTA.get().create(level);
        if (betta == null) {
            return null;
        }
        betta.setFromBucket(true);
        CompoundTag data = tag.getCompound("display");
        betta.setUUID(UUID.fromString(data.getString("UUID")));
        betta.setSireName(data.getString("SireName"));
        betta.setDamName(data.getString("DamName"));
        CompoundTag genetics = tag.getCompound("Genetics");
        Genes genes = new Genes(genetics.getIntArray("SGenes"), genetics.getIntArray("AGenes"));
        if (genes.getNumberOfAutosomalGenes() != BETTA_AUTOSOMAL_GENES_LENGTH) {
            int[] newAGenes = new int[BETTA_AUTOSOMAL_GENES_LENGTH];
            System.arraycopy(genes.getAutosomalGenes(), 0, newAGenes, 0, genes.getNumberOfAutosomalGenes());
            genes.setGenes(newAGenes);
        }
        if (!genes.isValid() && genes.getNumberOfAutosomalGenes() != 0) {
            genes.fixGenes(1);
        }
        betta.setGenes(genes);
        betta.setSharedGenes(genes);

        CompoundTag mateGenetics = tag.getCompound("Genetics");
        Genes mateGenes = new Genes(genetics.getIntArray("SGenes"), genetics.getIntArray("AGenes"));
        if (mateGenes.isValid() && mateGenes.getSexlinkedGenes().length > 0 && mateGenes.getAutosomalGenes().length > 0) {
            betta.setMateGender(mateGenetics.getBoolean("MateIsFemale"));
            betta.setMateGenes(mateGenes);
            betta.setHasEgg(true);
        }
        if (data.contains("collar")) {
            CompoundTag collar = data.getCompound("collar");
            betta.getEnhancedInventory().setItem(1, ItemStack.of(collar));
        }
        if (data.contains("Name", 8)) {
            betta.setCustomName(Component.Serializer.fromJson(data.getString("Name")));
        }
        betta.setBirthTime(data.getString("BirthTime"));
        betta.initilizeAnimalSize();
        betta.loadFromBucketTag(tag);
        betta.moveTo((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
        level.addFreshEntity(betta);
        return betta;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> componentList, TooltipFlag flag) {
        if (getIsFemale(stack).isPresent()) {
            componentList.add(new TranslatableComponent((EnhancedBetta.speciesTranslationKey + (getIsFemale(stack).get() ? ".female" : ".male"))).withStyle(new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY}));
        }
    }

    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        //If we're starting with an empty tank, consume the water for filling it. Otherwise you get to keep the water
        Item successItemStack = level.getBlockState(blockPos).is(AddonBlocks.DISPLAY_TANK.get()) ? Items.BUCKET : Items.WATER_BUCKET;
        BlockState blockState = DisplayTankBlock.fill(level.getBlockState(blockPos), level, blockPos);
        if (blockState.is(AddonBlocks.FILLED_DISPLAY_TANK.get()) &&
                level.getBlockEntity(blockPos) instanceof FilledDisplayTankBlockEntity &&
                !((FilledDisplayTankBlockEntity)level.getBlockEntity(blockPos)).hasEntityTag()) {
            FilledDisplayTankBlock.fillWithEntityTag(level, blockPos, blockState, context.getItemInHand());
            if (player != null) {
                //Since we're filling a tank with water already in it, we want to keep the water in the bucket
                player.setItemInHand(context.getHand(), !player.getAbilities().instabuild ? new ItemStack(successItemStack) : context.getItemInHand());
                this.playEmptySound(player, level, blockPos);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static <T extends LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickupAllowEmpty(Player player, InteractionHand hand, T entity) {
        //Functionally almost identical to the normal bucket pickup method but it allows for the use of empty buckets.
        //Needed so that you can retrieve bettas from display tanks without water in bucket
        ItemStack itemstack = player.getItemInHand(hand);
        if ((itemstack.getItem() == Items.WATER_BUCKET || itemstack.getItem() == Items.BUCKET) && entity.isAlive()) {
            entity.playSound(entity.getPickupSound(), 1.0F, 1.0F);
            ItemStack itemstack1 = entity.getBucketItemStack();
            entity.saveToBucketTag(itemstack1);
            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, player, itemstack1, false);
            player.setItemInHand(hand, itemstack2);
            Level level = entity.level;
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemstack1);
            }

            entity.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();
        }
    }

}
