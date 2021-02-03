package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.tileentities.TransformerTileEntity;
import edu.uidaho.electricblocks.utils.MathUtils;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class TransformerScreen extends AbstractScreen {

    private TransformerTileEntity tileEntity;
    private boolean inService;
    private double scrollPosition = 0;

    public TransformerScreen(TransformerTileEntity tileEntity, PlayerEntity player) {
        super(new TranslationTextComponent("gui.electricblocks.transformerscreen")
        .appendText(" ")
        .appendSibling(new TranslationTextComponent("gui.electricblocks.scrollable"))
        );
        this.tileEntity = tileEntity;
        this.player = player;
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        ElectricBlocksMod.LOGGER.debug("1: {}, 2: {}, 3: {}", p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
        scrollPosition = MathUtils.clamp(scrollPosition + p_mouseScrolled_1_, 0, 100);
        for (Widget w : this.buttons) {
            
        }
        return super.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
    }

    @Override
    protected void init() {
        this.addButton(new Button(
            (this.width - BUTTON_WIDTH) / 2,
            this.height - 2 * DONE_BUTTON_TOP_OFFSET,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            inService ? "In Service: true" : "In Service: false",
            button -> {
                if (inService) {
                    button.setMessage("In Service: false");
                    inService = false;
                } else {
                    button.setMessage("In Service: true");
                    inService = true;
                }
                onChange();
            }));
        super.init();
    }

    @Override
    protected void submitChanges() {

    }
    
}
