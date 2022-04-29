package red.jad.hotfade;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import red.jad.hotfade.config.ConfigManager;

@Environment(EnvType.CLIENT)
public class Hotfade implements ClientModInitializer {

    // Config
    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    public static final String MOD_ID = "hotfade";

    // Fading
    private static long lastInteractionTime;

    public static float getAlpha(){
        return getAlpha(lastInteractionTime);
    }

    public static float getAlpha(long lastInteract){
        long l = lastInteract - Util.getMeasuringTimeMs() + ConfigManager.FADE_OUT_DELAY + ConfigManager.FADE_OUT_DURATION;
        return MathHelper.clamp((float) l / ConfigManager.FADE_OUT_DURATION, ((float)ConfigManager.MIN_ALPHA)/100, ((float)ConfigManager.MAX_ALPHA)/100);
    }

    public static int getAlphaColor(int color){
        int alpha = MathHelper.clamp((int)(Hotfade.getAlpha()*255), 4, 255);
        // return (color & 0xFFFFFF) | alpha << 24;
        return color + (alpha << 24);
    }

    public static void showHUD(){
        lastInteractionTime = Util.getMeasuringTimeMs();
    }

    public static boolean shouldUnfadeablesBeHidden(){
        //return getAlpha() <= 0.1f;
        return false;
    }

    public static boolean isNoticeablyEffected(PlayerEntity p){
        // If underwater and can drown
        if (p.isSubmergedIn(FluidTags.WATER)){
            if(p.hasStatusEffect(StatusEffects.WATER_BREATHING)) return false;
            if(p.hasStatusEffect(StatusEffects.CONDUIT_POWER)) return false;
            return true;
        }

        // If hunger is low
        if(p.getHungerManager().getSaturationLevel() <= 0){
            if(p.getHungerManager().getFoodLevel() <= 6) return true;
        }

        return false;
    }

    @Override
    public void onInitializeClient() {
        ConfigManager.load();
    }
}
