package edu.uidaho.electricblocks.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class PlayerUtils {

    public static void sendMessage(PlayerEntity player, String message, Object... args) {
        if (player.getEntityWorld().isRemote()) {
            return;
        }

        player.sendMessage(new TranslationTextComponent(message, args));
    }

    public static void warn(PlayerEntity player, String warning, Object... args) {
        if (player.getEntityWorld().isRemote()) {
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

    public static void error(PlayerEntity player, String err, Object... args) {
        if (player.getEntityWorld().isRemote()) {
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
    
}
