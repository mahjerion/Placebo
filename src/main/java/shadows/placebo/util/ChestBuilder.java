package shadows.placebo.util;

import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.functions.EnchantRandomly;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import shadows.placebo.loot.StackLootEntry;

/**
 * Utils for loot chests. Uses the Placebo loot system.
 * @author Shadows
 *
 */
public class ChestBuilder {

	protected Random random;
	protected ChestTileEntity chest;
	protected boolean isValid;
	protected BlockPos position;
	protected IWorld iWorld;

	public ChestBuilder(IWorld world, Random rand, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof ChestTileEntity) {
			this.random = rand;
			this.chest = (ChestTileEntity) tileEntity;
			this.isValid = true;
			this.position = pos;
			this.iWorld = world;
		}
	}

	public void fill(ResourceLocation loot) {
		if (this.iWorld != null) {
			LockableLootTileEntity.setLootTable(this.iWorld, this.random, this.position, loot);
		} else {
			this.chest.setLootTable(loot, this.random.nextLong());
		}
	}

	public static LootEntry loot(Item item, int min, int max, int weight, int quality) {
		return loot(new ItemStack(item), min, max, weight, quality);
	}

	public static LootEntry loot(Block block, int min, int max, int weight, int quality) {
		return loot(new ItemStack(block), min, max, weight, quality);
	}

	public static LootEntry loot(ItemStack item, int min, int max, int weight, int quality) {
		return new StackLootEntry(item, min, max, weight, quality);
	}

	public static void place(IWorld world, Random random, BlockPos pos, ResourceLocation loot) {
		world.setBlockState(pos, Blocks.CHEST.getDefaultState(), 2);
		ChestBuilder chest = new ChestBuilder(world, random, pos);
		if (chest.isValid) {
			chest.fill(loot);
		}
	}

	public static void placeTrapped(IWorld world, Random random, BlockPos pos, ResourceLocation loot) {
		world.setBlockState(pos, Blocks.TRAPPED_CHEST.getDefaultState(), 2);
		ChestBuilder chest = new ChestBuilder(world, random, pos);
		if (chest.isValid) {
			chest.fill(loot);
		}
	}

	public static class EnchantedEntry extends StackLootEntry {

		protected final ILootFunction func = EnchantRandomly.func_215900_c().build();
		protected Item i;

		public EnchantedEntry(Item i, int weight) {
			super(i, 1, 1, weight, 5);
			this.i = i;
		}

		@Override
		protected void func_216154_a(Consumer<ItemStack> list, LootContext ctx) {
			list.accept(this.func.apply(new ItemStack(this.i), ctx));
		}

	}
}