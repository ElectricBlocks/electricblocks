package edu.uidaho.electricblocks.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.*;

public class PlayerUtils {

    /**
     * Sends a localized messaged to the player without any formatting.
     * @param player The player to send the message to
     * @param message The translation key for the message you want to send
     * @param args Optional positional arguments used in string formatting
     */
    public static void sendMessage(PlayerEntity player, String message, Object... args) {
        if (playerIsClientSide(player)) {
            return;
        }

        player.sendMessage(new TranslationTextComponent(message, args));
    }

    /**
     * Sends a localized message to the player with a red [WARNING] tag in front of it.
     * @param player The player to send the warning to
     * @param warning The translation key for the warning you want to send
     * @param args Optional positional arguments used in string formatting
     */
    public static void warn(PlayerEntity player, String warning, Object... args) {
        if (playerIsClientSide(player)) {
            return;
        }

        TranslationTextComponent parent = new TranslationTextComponent("command.electricblocks.warn");
        parent.applyTextStyle(TextFormatting.RED);
        parent.appendText(": ");

        TranslationTextComponent sibling = new TranslationTextComponent(warning, args);
        sibling.applyTextStyle(TextFormatting.WHITE);
        parent.appendSibling(sibling);

        player.sendMessage(parent);
    }

    /**
     * Sends a localized message to the player with a dark red [ERROR] tag in front of it.
     * @param player The player to send the error to
     * @param err The translation key for the error you want to send
     * @param args Optional positional arguments used in string formatting
     */
    public static void error(PlayerEntity player, String err, Object... args) {
        if (playerIsClientSide(player)) {
            return;
        }

        TranslationTextComponent parent = new TranslationTextComponent("command.electricblocks.error");
        parent.applyTextStyle(TextFormatting.DARK_RED);
        parent.appendText(": ");

        TranslationTextComponent sibling = new TranslationTextComponent(err, args);
        sibling.applyTextStyle(TextFormatting.WHITE);
        parent.appendSibling(sibling);

        player.sendMessage(parent);
    }

    /**
     * Sends a raw unformatted and non-localized message to the player. This is not recommended, since localization is
     * highly recommended, but I figured this should still be in here.
     * @param player The player to send the raw message to
     * @param message The messsage itself
     */
    @Deprecated
    public static void sendRaw(PlayerEntity player, String message) {
        if (playerIsClientSide(player)) {
            return;
        }

        player.sendMessage(new StringTextComponent(message));
    }

    /**
     * Checks if the world the player is in is remote or not. This is useful for checking if an action is occurring on
     * the client or server.
     * @param player The player instance that is being checked
     * @return True if the player object is client side, false if it is server side
     */
    public static boolean playerIsClientSide(PlayerEntity player) {
        return player.getEntityWorld().isRemote();
    }
    
}
