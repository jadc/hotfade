package red.jad.hotfade.config;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
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

        initFooter();
    }

    private void initFooter() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> close()));
    }

    private void drawHUDPreview(MatrixStack matrices){
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.5f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, HUD_PREVIEW_TEXTURE);
        this.drawTexture(matrices, this.width / 2 - 128, 75, 0, 0, 256, 256);
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

        super.render(matrices, mouseX, mouseY, delta);
    }
}
