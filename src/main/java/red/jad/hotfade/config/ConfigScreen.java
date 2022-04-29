package red.jad.hotfade.config;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import red.jad.hotfade.Hotfade;

public class ConfigScreen extends Screen {
    private final Screen parent;
    public static final Identifier HUD_PREVIEW_TEXTURE = new Identifier(Hotfade.MOD_ID, "hud_preview.png");

    public ConfigScreen(Screen parent) {
        super(new TranslatableText("hotfade.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        ConfigManager.load();
        this.lastInteractionTime = Util.getMeasuringTimeMs();

        // Max alpha slider
        this.addDrawableChild(new AlphaSliderWidget(ConfigManager.MAX_ALPHA, this.width / 2 - 100, 120,"hotfade.config.max") {
            @Override
            protected void applyValue() {
                showHUD();
                ConfigManager.MAX_ALPHA = MathHelper.floor(MathHelper.clampedLerp(0.0, 100.0, this.value));
            }
        });
        // Min alpha slider
        this.addDrawableChild(new AlphaSliderWidget(ConfigManager.MIN_ALPHA, this.width / 2 - 100, 145,"hotfade.config.min") {
            @Override
            protected void applyValue() {
                showHUD();
                ConfigManager.MIN_ALPHA = MathHelper.floor(MathHelper.clampedLerp(0.0, 100.0, this.value));
            }
        });
        // Fade out delay input
        TextFieldWidget fadeOutDelayField = this.addDrawableChild(new TextFieldWidget(this.textRenderer, this.width / 2 + 100 - 42, 170, 42, 20, new TranslatableText("hotfade.config.fade_out_delay")));
        fadeOutDelayField.setText("" + ConfigManager.FADE_OUT_DELAY);
        fadeOutDelayField.setChangedListener(text -> {
            showHUD();
            try {
                ConfigManager.FADE_OUT_DELAY = Long.parseLong(text);
            }
            catch (NumberFormatException ignored){}
        });
        // Fade out duration input
        TextFieldWidget fadeOutDurationField = this.addDrawableChild(new TextFieldWidget(this.textRenderer, this.width / 2 + 100 - 42, 195, 42, 20, new TranslatableText("hotfade.config.fade_out_duration")));
        fadeOutDurationField.setText("" + ConfigManager.FADE_OUT_DURATION);
        fadeOutDurationField.setChangedListener(text -> {
            showHUD();
            try {
                ConfigManager.FADE_OUT_DURATION = Long.parseLong(text);
            }
            catch (NumberFormatException ignored){}
        });

        initFooter();
    }

    private void initFooter() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 100, 20, ScreenTexts.DONE, button -> close()));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 1, this.height - 27, 100, 20, new TranslatableText("hotfade.config.more"), button -> Util.getOperatingSystem().open(ConfigManager.file)));

    }

    private long lastInteractionTime;
    private void drawHUDPreview(MatrixStack matrices){
        if(Util.getMeasuringTimeMs() - lastInteractionTime  > ConfigManager.FADE_OUT_DURATION + ConfigManager.FADE_OUT_DELAY + 1000) showHUD();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Hotfade.getAlpha(lastInteractionTime));
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, HUD_PREVIEW_TEXTURE);
        this.drawTexture(matrices, this.width / 2 - 128, 55, 0, 0, 256, 256);
        RenderSystem.disableBlend();
    }

    private void showHUD(){
        lastInteractionTime = Util.getMeasuringTimeMs();
    }

    @Override
    public void close() {
        ConfigManager.save();
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.drawHUDPreview(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 25, 0xFFFFFF);

        this.textRenderer.drawWithShadow(matrices, new TranslatableText("hotfade.config.fade_out_delay"), this.width / 2 - 100, 170+6, 0xFFFFFF);
        this.textRenderer.drawWithShadow(matrices, new TranslatableText("hotfade.config.fade_out_duration"), this.width / 2 - 100, 195+6, 0xFFFFFF);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
