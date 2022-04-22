package red.jad.hotfade.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jad.hotfade.Hotfade;
import red.jad.hotfade.config.HotfadeConfigScreen;

public class TriggerMixins {

    @Mixin(MinecraftClient.class)
    public static class MinecraftClientMixin {
        // If slot changes via key press
        @Inject( method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getInventory()Lnet/minecraft/entity/player/PlayerInventory;") )
        private void slotKeyChange(CallbackInfo ci){
            Hotfade.showHUD();
        }
    }

    @Mixin(PlayerInventory.class)
    public static class PlayerInventoryMixin {
        // If slot changes via scrolling
        @Inject( method = "scrollInHotbar", at = @At(value = "HEAD"))
        private void slotScrollChange(double scrollAmount, CallbackInfo ci){
            Hotfade.showHUD();
        }
    }

    @Mixin(ClientPlayerEntity.class)
    public static class ClientPlayerEntityMixin {
        @Shadow public Input input;

        @Shadow @Final protected MinecraftClient client;

        // If health increases or decreases
        @Inject( method = "updateHealth", at = @At(value = "HEAD") )
        private void healthChange(float health, CallbackInfo ci){
            Hotfade.showHUD();
        }

        // If experience increases or decreases
        @Inject( method = "setExperience", at = @At(value = "HEAD") )
        private void expChange(float progress, int total, int level, CallbackInfo ci){
            Hotfade.showHUD();
        }

        // If jump bar changes
        @Inject( method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getVehicle()Lnet/minecraft/entity/Entity;") )
        private void jumpBarChange(CallbackInfo ci){
            if(this.input.jumping) Hotfade.showHUD();
        }
    }

    @Mixin(InGameHud.class)
    public static abstract class InGameHudMixin {

        @Shadow protected abstract LivingEntity getRiddenEntity();

        @Inject(
                method = "renderStatusBars",
                at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getMeasuringTimeMs()J")
        )
        private void statusChange(MatrixStack matrices, CallbackInfo ci){
            if(this.getRiddenEntity() != null) {
                if (this.getRiddenEntity().timeUntilRegen > 0) Hotfade.showHUD();
            }
        }
    }
}
