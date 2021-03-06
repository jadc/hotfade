package red.jad.hotfade.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jad.hotfade.Hotfade;

public class TestMixins {
    @Mixin(ItemRenderer.class)
    public static class ItemRendererMixin {
        @Inject(
                method = "renderGuiItemModel",
                at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;getModelViewStack()Lnet/minecraft/client/util/math/MatrixStack;")
        )
        private void sea(ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci){
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Hotfade.getAlpha());
        }
    }

    @Mixin(InGameHud.class)
    public static class InGameHudTestMixin {

        @Inject( method = "renderHotbarItem", at = @At(value = "HEAD") )
        private void test(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci){
            RenderSystem.disableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Hotfade.getAlpha());
        }
    }
}
