package org.prismlauncher.blahaj.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import org.prismlauncher.blahaj.CuddlyItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Allay.class)
public class AllayMixin {

	@Inject(
		method = "mobInteract",
		at = @At("HEAD"),
		cancellable = true
	)
	public void preventTakePlush(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		if(player.getItemInHand(hand).getItem() instanceof CuddlyItem) {
			cir.setReturnValue(InteractionResult.PASS);
			cir.cancel();
		}
	}
}
