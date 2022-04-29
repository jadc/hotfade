package red.jad.hotfade.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jad.hotfade.Hotfade;

public class TriggerMixins {

    @Mixin(MinecraftClient.class)
    public static class MinecraftClientMixin {
        // If slot changes via key press
        @Inject( method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getInventory()Lnet/minecraft/entity/player/PlayerInventory;") )
        private void hotfade$slotChangeViaKey(CallbackInfo ci){
            Hotfade.showHUD();
        }
    }

    @Mixin(PlayerInventory.class)
    public static class PlayerInventoryMixin {
        // If slot changes via scrolling
        @Inject( method = "scrollInHotbar", at = @At(value = "HEAD"))
        private void hotfade$slotChangeViaScroll(double scrollAmount, CallbackInfo ci){
            Hotfade.showHUD();
        }
    }

    @Mixin(ClientPlayerEntity.class)
    public static class ClientPlayerEntityMixin {
        @Shadow public Input input;

        // If experience increases or decreases
        @Inject( method = "setExperience", at = @At(value = "HEAD") )
        private void hotfade$expChange(float progress, int total, int level, CallbackInfo ci){
            Hotfade.showHUD();
        }

        // If jump bar changes
        @Inject( method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getVehicle()Lnet/minecraft/entity/Entity;") )
        private void hotfade$jumpBarChange(CallbackInfo ci){
            if(this.input.jumping) Hotfade.showHUD();
        }
    }

    @Mixin(InGameHud.class)
    public static abstract class InGameHudMixin {

        @Shadow protected abstract LivingEntity getRiddenEntity();
        @Shadow protected abstract PlayerEntity getCameraPlayer();

        @Shadow @Final private MinecraftClient client;

        @Inject( method = "renderStatusBars", at = @At(value = "HEAD") )
        private void hotfade$statusChange(MatrixStack matrices, CallbackInfo ci){
            // General status change
            if(Hotfade.isNoticeablyEffected(getCameraPlayer())) Hotfade.showHUD();

            System.out.println(this.client.currentScreen);

            // Mount health change
            if(this.getRiddenEntity() != null) {
                if (this.getRiddenEntity().timeUntilRegen > 0) Hotfade.showHUD();
            }
        }


        @Inject( method = "renderHealthBar", at = @At(value = "HEAD") )
        private void hotfade$healthChange(MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci){
            if(blinking) Hotfade.showHUD(); // If health changes
            if(lastHealth + absorption <= 4) Hotfade.showHUD(); // If health dangerously low (wiggling)
        }



    }
}
