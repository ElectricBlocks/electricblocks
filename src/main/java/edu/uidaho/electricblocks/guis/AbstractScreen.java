package edu.uidaho.electricblocks.guis;

import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

public abstract class AbstractScreen extends Screen {
    // Layout constants 
    // Width of a button
    protected static final int BUTTON_WIDTH = 200;
    // Height of a button
    protected static final int BUTTON_HEIGHT = 20;
    // Width of text input
    protected static final int TEXT_INPUT_WIDTH = 100;
    // Height of text input
    protected static final int TEXT_INPUT_HEIGHT = 20;
    // Distance from bottom of the screen to the "Done" button's top
    protected static final int DONE_BUTTON_TOP_OFFSET = 26;
    // Distance from top of the screen to this GUI's title
    protected static final int TITLE_HEIGHT = 8;
    protected int enabledColor = 14737632;

    // Layout elements
    private boolean donePressed = false;
    protected Button doneButton;

    // Info needed to preload the form with data
    protected boolean changed = false;
    protected PlayerEntity player;

    protected AbstractScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    /**
     * Called whenever a new screen is initialized. This function adds a "Done" button at the bottom of the screen by
     * default. Override in subclasses to perform additional actions
     */
    @Override
    protected void init() {
        // Add the "Done" button
        doneButton = new Button(
            (this.width - BUTTON_WIDTH) / 2,
            this.height - DONE_BUTTON_TOP_OFFSET,
            BUTTON_WIDTH, BUTTON_HEIGHT,
            // Text shown on the button
            I18n.format("gui.done"),
            // Action performed when the button is pressed
            button -> {
                donePressed = true;
                onClose();
            }
        );
        this.addButton(doneButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // res will be false for all characters that update the display except for backspace
        boolean res = super.keyPressed(keyCode, scanCode, modifiers); 
        boolean updateKeyPressed = !res || keyCode == 259; // Check if backspace is pressed or other update key is pressed
        boolean focusIsOnEditableTextFieldWidget = this.getFocused() instanceof TextFieldWidget && ((TextFieldWidget) this.getFocused()).canWrite();
        if (updateKeyPressed && focusIsOnEditableTextFieldWidget) {
            onChange();
        }
        return res;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        // First draw the background of the screen
        this.renderBackground();
        // Draw the title
        this.drawCenteredString(this.font, this.title.getFormattedText(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.render(mouseX, mouseY, partialTicks);
    }

    public void onChange() {
        if (!changed) {
            changed = true;
            doneButton.setMessage("* " + I18n.format("gui.done") + " *");
        }
    }

    @Override
    public void onClose() {
        if (changed) {
            if (donePressed) {
                submitChanges();
            } else {
                PlayerUtils.warn(player, "gui.electricblocks.warn_escape");
            }
        }
        super.onClose();
    }

    /**
     * This function is called when a player pressed the done button after
     * making changes. Subclasses should implement form validation, tile entity
     * updates, and simulation requests in this function.
     */
    protected abstract void submitChanges();
    
}
