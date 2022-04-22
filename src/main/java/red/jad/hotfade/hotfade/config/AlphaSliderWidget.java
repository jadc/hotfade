package red.jad.hotfade.hotfade.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import red.jad.hotfade.hotfade.Hotfade;

@Environment(value = EnvType.CLIENT)
public abstract class AlphaSliderWidget extends SliderWidget {
    private final String translatableText;

    public AlphaSliderWidget(int x, int y, String translatableText, double value) {
        super(x, y, 200, 20, LiteralText.EMPTY, value);
        this.translatableText = translatableText;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(new TranslatableText(this.translatableText, (int)(value*100)));
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        Hotfade.PREVIEW_ALPHA = -1;
    }
}
