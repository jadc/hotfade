package red.jad.hotfade.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import red.jad.hotfade.Hotfade;

@Environment(value = EnvType.CLIENT)
public abstract class AlphaSliderWidget extends SliderWidget {
    private final String translatableText;

    public AlphaSliderWidget(double value, int x, int y, String text) {
        super(x, y, 200, 20, new TranslatableText(text, (int)value+"%"), value/100);
        this.translatableText = text;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(new TranslatableText( translatableText, MathHelper.floor(MathHelper.clampedLerp(0.0, 100.0, this.value))+"%") );
    }
}
