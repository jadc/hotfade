package red.jad.hotfade.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import red.jad.hotfade.Hotfade;

public class HotfadeConfigScreen extends Screen {
    private final Screen parent;

    public HotfadeConfigScreen(Screen parent) {
        super(new TranslatableText("hotfade.config.title"));
        this.parent = parent;
    }


    @Override
    protected void init() {
        // Max alpha slider
        this.addDrawableChild(new AlphaSliderWidget(Hotfade.MAX_ALPHA, this.width / 2 - 100, 80,"hotfade.config.max") {
            @Override
            protected void applyValue() {
                Hotfade.MAX_ALPHA = (float)MathHelper.clamp(this.value, 0.0, 1.0);
            }
        });
        // Min alpha slider
        this.addDrawableChild(new AlphaSliderWidget(Hotfade.MIN_ALPHA, this.width / 2 - 100, 120,"hotfade.config.min") {
            @Override
            protected void applyValue() {
                Hotfade.MIN_ALPHA = (float)MathHelper.clamp(this.value, 0.0, 1.0);
            }
        });

        // Done button
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, 200, 200, 20, ScreenTexts.DONE, button -> {
            this.close();
        }));
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void renderBackground(MatrixStack matrices, int vOffset) {
        if (this.client.world != null) {
            this.fillGradient(matrices, 0, 0, this.width, (this.height * 2) / 3, -1072689136, -804253680);
        } else {
            this.renderBackgroundTexture(vOffset);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 50, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
