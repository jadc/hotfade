package red.jad.hotfade.hotfade.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import red.jad.hotfade.hotfade.Hotfade;

public class ConfigScreen extends Screen {
    public ConfigScreen() {
        super(new TranslatableText("hotfade.config.title"));
    }


    @Override
    protected void init() {
        // Max alpha slider
        this.addDrawableChild(new AlphaSliderWidget(this.width / 2 - 100, 80, "hotfade.config.max", Hotfade.MAX_ALPHA) {
            @Override
            protected void applyValue() {
                Hotfade.MAX_ALPHA = (float)MathHelper.clamp(this.value, 0.0, 1.0);
            }

            @Override
            protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
                super.onDrag(mouseX, mouseY, deltaX, deltaY);
                Hotfade.PREVIEW_ALPHA = Hotfade.MAX_ALPHA;
            }
        });
        // Min alpha slider
        this.addDrawableChild(new AlphaSliderWidget(this.width / 2 - 100, 120, "hotfade.config.min", Hotfade.MIN_ALPHA) {
            @Override
            protected void applyValue() {
                Hotfade.MIN_ALPHA = (float)MathHelper.clamp(this.value, 0.0, 1.0);
            }

            @Override
            protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
                super.onDrag(mouseX, mouseY, deltaX, deltaY);
                Hotfade.PREVIEW_ALPHA = Hotfade.MIN_ALPHA;
            }
        });
    }

    @Override
    public void close() {
        super.close();

        if(client.player != null) client.player.sendMessage(new TranslatableText("hotfade.config.save"), true);
    }

    @Override
    public void renderBackground(MatrixStack matrices, int vOffset) {
        if (this.client.world != null) {
            this.fillGradient(matrices, 0, 0, this.width, (this.height*2)/3, -1072689136, -804253680);
        } else {
            this.renderBackgroundTexture(vOffset);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        ConfigScreen.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 50, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
