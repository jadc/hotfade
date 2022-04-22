package red.jad.hotfade;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class Hotfade implements ClientModInitializer {

    // Config
    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    public static final String MOD_ID = "hotfade";

    // Fading
    private static long lastInteractionTime;
    private static final long FADE_OUT_DELAY = 5000L; // 5000
    private static final long FADE_OUT_DURATION = 2000L; // 2000
    public static float MAX_ALPHA = 1.0f;
    public static float MIN_ALPHA = 0.0f;
    public static float PREVIEW_ALPHA = -1;

    public static float getAlpha(){
        if(PREVIEW_ALPHA != -1) return PREVIEW_ALPHA;
        long l = lastInteractionTime - Util.getMeasuringTimeMs() + FADE_OUT_DELAY;
        return MathHelper.clamp((float) l / FADE_OUT_DURATION, MIN_ALPHA, MAX_ALPHA);
    }

    public static void showHUD(){
        lastInteractionTime = Util.getMeasuringTimeMs();
    }

    public static boolean shouldUnfadeablesBeHidden(){
        return getAlpha() <= 0.1f;
    }

    @Override
    public void onInitializeClient() {

    }
}
