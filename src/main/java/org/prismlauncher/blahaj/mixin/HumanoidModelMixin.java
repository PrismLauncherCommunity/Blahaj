package org.prismlauncher.blahaj.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.prismlauncher.blahaj.CuddlyItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> {

	@Shadow
	public @Final ModelPart rightArm;

	@Shadow
	public @Final ModelPart leftArm;

	@Inject(
		method = {"poseRightArm", "poseLeftArm"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/model/AnimationUtils;animateCrossbowHold(Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;Z)V",
			shift = Shift.AFTER
		),
		cancellable = true
	)
	public void poseArms(T livingEntity, CallbackInfo ci) {
		if(livingEntity.getMainHandItem().getItem() instanceof CuddlyItem || livingEntity.getOffhandItem().getItem() instanceof CuddlyItem) {
			this.rightArm.xRot = -0.95F;
			this.rightArm.yRot = (float) (-Math.PI / 8);
			this.leftArm.xRot = -0.90F;
			this.leftArm.yRot = (float) (Math.PI / 8);
			ci.cancel();
		}
	}
}
