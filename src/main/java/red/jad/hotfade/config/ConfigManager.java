package red.jad.hotfade.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import red.jad.hotfade.Hotfade;

import java.io.*;

public class ConfigManager {
    public static long FADE_OUT_DELAY = 5000L; // 5000
    public static long FADE_OUT_DURATION = 2000L; // 2000
    public static int MAX_ALPHA = 100;
    public static int MIN_ALPHA = 0;

    private static File file;
    private static final String fileName = Hotfade.MOD_ID + ".json";
    private static void createConfigFile() {
        if (file == null) {
            file = new File(FabricLoader.getInstance().getConfigDir().toFile(), fileName);
        }
    }

    public static void load(){
        createConfigFile();
        if(!file.exists()) save();
        if(file.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                JsonObject json = JsonParser.parseReader(br).getAsJsonObject();

                MAX_ALPHA = json.get("max_alpha").getAsInt();
                MIN_ALPHA = json.get("min_alpha").getAsInt();
                FADE_OUT_DELAY = json.get("fade_out_delay").getAsLong();
                FADE_OUT_DURATION = json.get("fade_out_duration").getAsLong();

            } catch (FileNotFoundException e) {
                System.err.println("Configuration file '" + fileName + "' was not found; using defaults...");
                e.printStackTrace();
            }
        }
    }

    public static void save(){
        createConfigFile();
        JsonObject config = new JsonObject();
        config.addProperty("max_alpha", MAX_ALPHA);
        config.addProperty("min_alpha", MIN_ALPHA);
        config.addProperty("fade_out_delay", FADE_OUT_DELAY);
        config.addProperty("fade_out_duration", FADE_OUT_DURATION);

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(Hotfade.GSON.toJson(config));
        } catch (IOException e) {
            System.err.println("Failed to save configuration file '"+ fileName +"'");
            e.printStackTrace();
        }
    }
}
