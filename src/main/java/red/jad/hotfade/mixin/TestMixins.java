package red.jad.hotfade.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jad.hotfade.Hotfade;

public class TestMixins {
    @Mixin(ItemRenderer.class)
    public static class ItemRendererMixin {
        /*
        @Inject(
                method = "renderGuiItemModel",
                at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;getModelViewStack()Lnet/minecraft/client/util/math/MatrixStack;")
        )
        private void sea(ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci){
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Hotfade.getAlpha());
        }

         */

        @ModifyArg(
                method = "renderGuiQuad",
                at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;color(IIII)Lnet/minecraft/client/render/VertexConsumer;"),
                index = 3
        )
        private int quad(int var1){
            return (int) (Hotfade.getAlpha()*255);
        }
    }

    @Mixin(InGameHud.class)
    public static class InGameHudTestMixin {

        @Inject( method = "renderHealthBar", at = @At(value = "TAIL") )
        private void test(MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci){

        }
    }
}
