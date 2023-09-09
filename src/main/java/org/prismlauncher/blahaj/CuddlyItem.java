package org.prismlauncher.blahaj;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CuddlyItem extends Item {

	public static final String OWNER_KEY = "Owner";

	private final Component subtitle;

	public CuddlyItem(Properties properties, String subtitle) {
		super(properties);
		this.subtitle = subtitle == null? null: Component.translatable(subtitle).withStyle(ChatFormatting.GRAY);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		if(this.subtitle != null) {
			tooltipComponents.add(this.subtitle);
		}
		if(stack.hasTag()) {
			CompoundTag nbt = stack.getTag();
			if (nbt == null)
				return;
			String owner = nbt.getString(OWNER_KEY);
			if (owner.isEmpty())
				return;
			if (stack.hasCustomHoverName())
				tooltipComponents.add(Component.translatable("tooltip.blahaj.owner.rename", this.getDescription(), Component.literal(owner)).withStyle(ChatFormatting.GRAY));
			else
				tooltipComponents.add(Component.translatable("tooltip.blahaj.owner.craft", Component.literal(owner)).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		if(player != null) { // compensate for auto-crafter mods
			stack.addTagElement(OWNER_KEY, StringTag.valueOf(player.getName().getString()));
		}
		super.onCraftedBy(stack, world, player);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return 0.25f;
	}
}
