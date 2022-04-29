package red.jad.hotfade.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jad.hotfade.Hotfade;

public class InjectMixins {
    @Mixin(InGameHud.class)
    public static class InGameHudAlphaMixin {
        // Simple
        @ModifyArg(
                method = "renderHotbar",
                at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"),
                index = 3
        )
        private float hotfade$setHotbarAlpha(float alpha){
            return Hotfade.getAlpha();
        }

        @Inject(
                method = "renderStatusBars",
                at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getMeasuringTimeMs()J")
        )
        private void hotfade$setStatusBarsAlpha(MatrixStack matrices, CallbackInfo ci){
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Hotfade.getAlpha());
        }

        // Tricky
        @Inject(
                method = "renderExperienceBar",
                at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getNextLevelExperience()I")
        )
        private void hotfade$setExperienceBarAlpha(MatrixStack matrices, int x, CallbackInfo ci){
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Hotfade.getAlpha());
        }

        @ModifyArg(
                method = "renderExperienceBar",
                at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"),
                index = 4
        )
        private int hotfade$setExperienceNumberAlpha(int color){
            int alpha = MathHelper.clamp((int)(Hotfade.getAlpha()*255), 4, 255);
            return (color & 0xFFFFFF) | alpha << 24;
        }

        @Inject(
                method = "renderMountJumpBar",
                at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V")
        )
        private void hotfade$setMountJumpBarAlpha(MatrixStack matrices, int x, CallbackInfo ci){
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Hotfade.getAlpha());
        }

        // How
        @Inject(
                method = "renderHotbarItem",
                at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"),
                cancellable = true
        )
        private void hotfade$hideItems(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci){
            if(Hotfade.shouldUnfadeablesBeHidden()) ci.cancel();
        }
    }

    @Mixin(ItemRenderer.class)
    public static class ItemRendererMixin {
        @Inject(
                method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
                at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V")
        )
        private void hotfade$blendDurability(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci){
            RenderSystem.enableBlend();
        }

        @ModifyArg(
                method = "renderGuiQuad",
                at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;color(IIII)Lnet/minecraft/client/render/VertexConsumer;"),
                index = 3
        )
        private int hotfade$setDurabilityAlpha(int var1){
            return (int) (Hotfade.getAlpha()*255);
        }
    }
}
