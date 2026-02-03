package x170.all_items.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.serialization.JsonOps;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import x170.all_items.AllItems;
import x170.all_items.game.GameManager;

import java.util.ArrayList;

public class AllItemsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        dispatcher.register(Commands.literal("all-items")
                .executes(context -> execute(context.getSource()))
        );
    }

    private static int execute(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        // Send error message if the command was called by a non-player
        if (player == null) {
            source.sendFailure(Component.literal("Only players can use this command"));
            return 0;
        }

        String dialogJSON = buildItemDialogJson(AllItems.CONFIG.obtainedItems);

        try {
            JsonElement json = JsonParser.parseString(dialogJSON);
            Holder<Dialog> dialog = Dialog.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
            player.openDialog(dialog);
        } catch (Exception e) {
            source.sendFailure(Component.literal("Failed to open dialog: " + e.getMessage()));
            return 0;
        }

        return 1;
    }

    private static String buildItemDialogJson(ArrayList<Item> items) {
        StringBuilder bodyBuilder = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);

            bodyBuilder.append("""
                    {
                      "type": "minecraft:item",
                      "item": {
                        "id": "%s"
                      },
                      "description": {
                        "translate": "%s",
                        "hover_event": {
                            "action": "show_text",
                            "value": "Item %d"
                        }
                      }
                    },
                    
                    """.formatted(
                    item.toString(),
                    item.getDescriptionId(),
                    i + 1
            ));
        }

        String body = bodyBuilder.toString();
        if (!body.isEmpty()) body = body.substring(0, body.length() - 3); // Remove last comma and newline

        return """
                {
                  "type": "minecraft:notice",
                  "title": "All-Items: Obtained Items [%d/%d]",
                  "body": [
                    %s
                  ]
                }
                """.formatted(
                AllItems.CONFIG.obtainedItems.size(),
                GameManager.getTotalItemCount(),
                body
        );
    }
}
