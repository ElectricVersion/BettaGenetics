package elecvrsn.GeneticBettas.items;

import elecvrsn.GeneticBettas.entity.EnhancedBetta;
import mokiyoki.enhancedanimals.util.Genes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA;

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
        betta.setFromBucket(true);
        CompoundTag data = stack.getOrCreateTagElement("display");
        if (level.getEntity(UUID.fromString(data.getString("UUID"))) == null) {
            betta.setUUID(UUID.fromString(data.getString("UUID")));
        }
        betta.setSireName(data.getString("SireName"));
        betta.setDamName(data.getString("DamName"));
        if (this.getGenes(stack) != null) {
            Genes genes = this.getGenes(stack);
            if (!genes.isValid() && genes.getNumberOfAutosomalGenes() != 0) {
                genes.fixGenes(1);
            }
            betta.setGenes(genes);
            betta.setSharedGenes(genes);

            genes = this.getMateGenes(stack);
            if (genes.isValid() && genes.getSexlinkedGenes().length > 0 && genes.getAutosomalGenes().length > 0) {
                betta.setMateGender(this.getMateIsFemale(stack));
                betta.setMateGenes(this.getMateGenes(stack));
                betta.setHasEgg(true);
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

    @Override
    public void appendHoverText(ItemStack p_151155_, @Nullable Level p_151156_, List<Component> p_151157_, TooltipFlag p_151158_) { }

}
