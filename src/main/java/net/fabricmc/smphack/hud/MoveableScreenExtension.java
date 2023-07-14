package net.fabricmc.smphack.hud;

import com.tanishisherewith.dynamichud.huds.MoveableScreen;
import com.tanishisherewith.dynamichud.util.DynamicUtil;
import com.tanishisherewith.dynamichud.widget.Widget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class MoveableScreenExtension extends MoveableScreen {
    /**
     * Constructs a AbstractMoveableScreen object.
     *
     * @param title
     * @param dynamicutil The DynamicUtil instance used by this screen
     */
    public MoveableScreenExtension(Text title, DynamicUtil dynamicutil) {
        super(title, dynamicutil);
    }

    @Override
    protected void menu(Widget widget, int x, int y) {
        super.menu(widget, x, y);
        if (widget instanceof MuppetWidget)
        {
            contextMenu=null;
            Slider=null;
            colorPicker=null;
            return;
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
    }
}
