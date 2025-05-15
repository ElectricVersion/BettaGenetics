package elecvrsn.GeneticBettas.items;

import elecvrsn.GeneticBettas.config.BettasCommonConfig;
import elecvrsn.GeneticBettas.entity.EnhancedBettaEgg;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

import static elecvrsn.GeneticBettas.init.AddonEntities.ENHANCED_BETTA_EGG;

public class EnhancedBettaEggBucket extends MobBucketItem {

    public EnhancedBettaEggBucket(Properties properties, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        super(entitySupplier, fluidSupplier, soundSupplier, properties);
    }

    @Override
    public void checkExtraContent(Player player, Level level, ItemStack stack, BlockPos pos) {
        if (level instanceof ServerLevel) {
            this.spawnBettaEgg((ServerLevel)level, stack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    public static void setData(ItemStack stack, String sireName, String damName, String genes, int hatchTime) {
        CompoundTag data = stack.getOrCreateTagElement("display");
        data.putString("SireName", sireName);
        data.putString("DamName", damName);
        data.putInt("HatchTime", hatchTime);
        data.putString("Genetics", genes);
    }

    private void spawnBettaEgg(ServerLevel level, ItemStack stack, BlockPos pos) {
        EnhancedBettaEgg bettaEgg = ENHANCED_BETTA_EGG.get().create(level);
        if (bettaEgg != null) {
            CompoundTag data = stack.getOrCreateTagElement("display");
            bettaEgg.setParentNames(data.getString("SireName"), data.getString("DamName"));
            bettaEgg.setGenes(data.getString("Genetics"));
            if (stack.hasCustomHoverName()) {
                bettaEgg.setCustomName(stack.getHoverName());
            }
            bettaEgg.setHatchTime(data.contains("HatchTime") ? data.getInt("HatchTime") : BettasCommonConfig.COMMON.bettaHatchTime.get());
            bettaEgg.moveTo((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
            level.addFreshEntity(bettaEgg);
        }
    }
}
