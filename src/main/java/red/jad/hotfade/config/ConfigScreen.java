package red.jad.hotfade.config;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
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

    private TextFieldWidget fadeOutDelayField;
    private TextFieldWidget fadeOutDurationField;

    @Override
    protected void init() {
        ConfigManager.load();
        this.lastInteractionTime = Util.getMeasuringTimeMs();

        // Max alpha slider
        this.addDrawableChild(new AlphaSliderWidget(ConfigManager.MAX_ALPHA, this.width / 2 - 100, 140,"hotfade.config.max") {
            @Override
            protected void applyValue() {
                ConfigManager.MAX_ALPHA = (float)MathHelper.clamp(this.value, 0.0, 1.0);
            }
        });
        // Min alpha slider
        this.addDrawableChild(new AlphaSliderWidget(ConfigManager.MIN_ALPHA, this.width / 2 - 100, 165,"hotfade.config.min") {
            @Override
            protected void applyValue() {
                ConfigManager.MIN_ALPHA = (float)MathHelper.clamp(this.value, 0.0, 1.0);
            }
        });
        // Fade out delay input
        this.fadeOutDelayField = this.addDrawableChild(new TextFieldWidget(this.textRenderer, this.width / 2 + 100 - 42, 190, 42, 20, new TranslatableText("hotfade.config.fade_out_delay")));
        this.fadeOutDelayField.setText("" + ConfigManager.FADE_OUT_DELAY);
        this.fadeOutDelayField.setChangedListener(text -> {
            try {
                ConfigManager.FADE_OUT_DELAY = Long.parseLong(text);
            }
            catch (NumberFormatException ignored){}
        });
        // Fade out duration input
        this.fadeOutDurationField = this.addDrawableChild(new TextFieldWidget(this.textRenderer, this.width / 2 + 100 - 42, 215, 42, 20, new TranslatableText("hotfade.config.fade_out_duration")));
        this.fadeOutDurationField.setText("" + ConfigManager.FADE_OUT_DURATION);
        this.fadeOutDurationField.setChangedListener(text -> {
            try {
                ConfigManager.FADE_OUT_DURATION = Long.parseLong(text);
            }
            catch (NumberFormatException ignored){}
        });

        initFooter();
    }

    private void initFooter() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> close()));
    }

    private long lastInteractionTime;
    private void drawHUDPreview(MatrixStack matrices){
        if(Util.getMeasuringTimeMs() % (ConfigManager.FADE_OUT_DURATION + ConfigManager.FADE_OUT_DELAY + 2000)/1000 == 0) lastInteractionTime = Util.getMeasuringTimeMs();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Hotfade.getAlpha(lastInteractionTime));
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, HUD_PREVIEW_TEXTURE);
        this.drawTexture(matrices, this.width / 2 - 128, 75, 0, 0, 256, 256);
        RenderSystem.disableBlend();
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
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 50, 0xFFFFFF);

        this.textRenderer.drawWithShadow(matrices, new TranslatableText("hotfade.config.fade_out_delay"), this.width / 2 - 100, 190+5, 0xFFFFFF);
        this.textRenderer.drawWithShadow(matrices, new TranslatableText("hotfade.config.fade_out_duration"), this.width / 2 - 100, 215+5, 0xFFFFFF);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
