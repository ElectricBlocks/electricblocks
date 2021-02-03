package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.tileentities.TransformerTileEntity;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class TransformerScreen extends AbstractScreen {

    // Constants
    private static final int MAX_SCROLL = 200;
    private static final int MIN_SCROLL = 0;

    // Layout elements
    TextFieldWidget textFieldRatedApparentPower; // sn_mva
    Button inServiceButton;

    // Instance specific info
    private TransformerTileEntity tileEntity;
    private boolean inService;
    private int scrollPosition = 0;

    public TransformerScreen(TransformerTileEntity tileEntity, PlayerEntity player) {
        super(new TranslationTextComponent("gui.electricblocks.transformerscreen")
        .appendText(" ")
        .appendSibling(new TranslationTextComponent("gui.electricblocks.scrollable"))
        );
        this.tileEntity = tileEntity;
        this.player = player;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollAmount) {
        int scroll = scrollAmount >= 0 ? 5 : -5;
        if (scrollPosition + scroll >= MIN_SCROLL && scrollPosition + scroll <= MAX_SCROLL) {
            scrollPosition += scroll;
            for (Widget w : this.buttons) {
                if (!(w instanceof Button)) {
                    w.y += scroll;
                }
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollAmount);
    }

    @Override
    protected void init() {
        inService = tileEntity.getInService();

        textFieldRatedApparentPower = new TextFieldWidget(font, (this.width - TEXT_INPUT_WIDTH) / 2 + (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, 25, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT, "");
        textFieldRatedApparentPower.setText(String.format("%f", tileEntity.getRatedApparentPower().getMegaWatts()));
        textFieldRatedApparentPower.setFocused2(true);
        textFieldRatedApparentPower.setVisible(true);
        addButton(textFieldRatedApparentPower);
        setFocused(textFieldRatedApparentPower);

        inServiceButton = new Button(
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
                });
        addButton(inServiceButton);
        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        this.drawString(this.font, "Apparent Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Result Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 60 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Reactive Power", (this.width - TEXT_INPUT_WIDTH) / 2 - (BUTTON_WIDTH - TEXT_INPUT_WIDTH) / 2, scrollPosition + 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw mw label
        this.drawString(this.font, "Mva", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 25 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "MW", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 60 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        this.drawString(this.font, "Mvar", (this.width / 2) + (TEXT_INPUT_WIDTH / 2) + 55, scrollPosition + 90 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);
        // Draw separator
        this.drawCenteredString(this.font, "- - - - - - - - - - - - - - - - - - - -", this.width / 2, scrollPosition + 45 + (this.font.FONT_HEIGHT / 2), 0xFFFFFF);

        // Re-render buttons to ensure correct ordering
        this.doneButton.render(mouseX, mouseY, partialTicks);
        this.inServiceButton.render(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void submitChanges() {

    }
    
}
