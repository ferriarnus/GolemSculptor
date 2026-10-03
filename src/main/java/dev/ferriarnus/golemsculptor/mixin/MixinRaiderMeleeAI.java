package dev.ferriarnus.golemsculptor.mixin;

import com.minecolonies.core.entity.mobs.aitasks.RaiderMeleeAI;
import dev.ferriarnus.golemsculptor.entity.SculptedGolemEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RaiderMeleeAI.class)
public class MixinRaiderMeleeAI {

    @Inject(method = "isAttackableTarget", at = @At("RETURN"), cancellable = true)
    private void targetGolem(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof SculptedGolemEntity) {
            cir.setReturnValue(true);
        }
    }
}
