package elecvrsn.GeneticBettas.datagen;

import elecvrsn.GeneticBettas.block.BubbleNestBlock;
import elecvrsn.GeneticBettas.block.FilledDisplayTankBlock;
import elecvrsn.GeneticBettas.block.LargeTankPlant;
import elecvrsn.GeneticBettas.init.AddonBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

public class AddonBlockLootTables extends BlockLoot {

    @Override
    protected void addTables() {
        for (Block i: getKnownBlocks()
        ) {
            if (i instanceof FilledDisplayTankBlock) {
                this.add(i, createFilledTankDrop(i));
            }
            else if (i instanceof BubbleNestBlock) {
                this.add(i, noDrop());
            }
            else if (i instanceof LargeTankPlant) {
                this.add(i, p-> createSinglePropConditionTable(p, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
            }
            else {
                this.dropSelf(i);
            }

        }
    }

    protected static LootTable.Builder createFilledTankDrop(Block block) {
        return LootTable.lootTable().withPool((LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(
                LootItem.lootTableItem(block)
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                        .copy("EntityData", "BlockEntityTag.EntityData")
                        .copy("EntityData.display.Name", "display.Lore", CopyNbtFunction.MergeStrategy.APPEND)
                        ))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return AddonBlocks.BLOCKS_DEFERRED_REGISTRY.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}