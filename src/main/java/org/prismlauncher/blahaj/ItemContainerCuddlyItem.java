package org.prismlauncher.blahaj;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemContainerCuddlyItem extends CuddlyItem {

	public static final String STORED_ITEM_KEY = "Item";

	public ItemContainerCuddlyItem(Properties properties, String subtitle) {
		super(properties, subtitle);
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
		if (action != ClickAction.SECONDARY)
			return false;

		ItemStack otherStack = slot.getItem();
		CompoundTag storedItemNbt = stack.getTagElement(STORED_ITEM_KEY);
		if (storedItemNbt != null) {
			if (!otherStack.isEmpty())
				return false;

			ItemStack storedStack = ItemStack.of(storedItemNbt);
			if (!slot.mayPlace(storedStack))
				return false;

			slot.safeInsert(storedStack, MAX_STACK_SIZE);
			ItemContainerCuddlyItem.storeItemStack(stack, null);
        } else {
			if (otherStack.isEmpty())
				return false;

			if (!ItemContainerCuddlyItem.canHold(otherStack))
				return false;

			ItemContainerCuddlyItem.storeItemStack(stack, otherStack);
        }
        return true;
    }

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		if (action != ClickAction.SECONDARY || other.isEmpty())
			return false;

		CompoundTag storedItemNbt = stack.getTagElement(STORED_ITEM_KEY);
		if (storedItemNbt != null) {
			return false;
		} else {
			if (!ItemContainerCuddlyItem.canHold(other))
				return false;

			ItemContainerCuddlyItem.storeItemStack(stack, other);
			return true;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
		CompoundTag itemsNbt = stack.getTagElement(STORED_ITEM_KEY);
		if (itemsNbt == null)
			return;

		ItemStack storedStack = ItemStack.of(itemsNbt);
		MutableComponent text = storedStack.getHoverName().copy();
		text.append(" x").append(String.valueOf(storedStack.getCount()));
		tooltipComponents.add(text);
	}

	protected static boolean canHold(ItemStack otherStack) {
        return otherStack.getItem().canFitInsideContainerItems()
                && !(otherStack.getItem() instanceof ItemContainerCuddlyItem)
                && !(otherStack.getItem() instanceof BundleItem);
    }

	protected static void storeItemStack(ItemStack thisStack, @Nullable ItemStack otherStack) {
		CompoundTag nbt = thisStack.getOrCreateTag();
		if (otherStack == null || otherStack.isEmpty()) {
			nbt.remove(STORED_ITEM_KEY);
		} else {
			thisStack.getOrCreateTag().put(STORED_ITEM_KEY, otherStack.save(new CompoundTag()));
			otherStack.setCount(0);
		}
	}

	protected static boolean mergeStacks(ItemStack dest, ItemStack source) {
		if (!ItemStack.isSameItemSameTags(dest, source)) {
			return false;
		}
		int destCount = dest.getCount();
		int sourceCount = source.getCount();
		int destMax = dest.getMaxStackSize();
		dest.grow(destCount + sourceCount);
		int surplus = destCount + sourceCount - destMax;
		source.setCount(surplus);
		return source.isEmpty();
	}
}
