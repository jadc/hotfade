package red.jad.hotfade.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import red.jad.hotfade.Hotfade;

@Environment(value = EnvType.CLIENT)
public abstract class AlphaSliderWidget extends SliderWidget {
    private final String translatableText;

    public AlphaSliderWidget(double value, int x, int y, String text) {
        super(x, y, 200, 20, new TranslatableText(text), value);
        this.translatableText = text;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(new TranslatableText( translatableText + ".slider", (int)(value*100)+"%") );
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        Hotfade.PREVIEW_ALPHA = -1;
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        super.onDrag(mouseX, mouseY, deltaX, deltaY);
        Hotfade.PREVIEW_ALPHA = (float) this.value;
    }
}
