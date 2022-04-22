package red.jad.hotfade;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
        long l = lastInteractionTime - Util.getMeasuringTimeMs() + ConfigManager.FADE_OUT_DELAY;
        return MathHelper.clamp((float) l / ConfigManager.FADE_OUT_DURATION, ConfigManager.MIN_ALPHA, ConfigManager.MAX_ALPHA);
    }

    public static void showHUD(){
        lastInteractionTime = Util.getMeasuringTimeMs();
    }

    public static boolean shouldUnfadeablesBeHidden(){
        return getAlpha() <= 0.1f;
    }

    @Override
    public void onInitializeClient() {
        ConfigManager.load();
    }
}
