package org.prismlauncher.blahaj.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.prismlauncher.blahaj.CuddlyItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
	@Inject(
		method = "getArmPose",
		at = @At("TAIL"),
		cancellable = true
	)
	private static void cuddleBlahaj(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
		ItemStack lv = player.getItemInHand(hand);
		if(lv.getItem() instanceof CuddlyItem) {
			cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
			cir.cancel();
		}
	}
}
