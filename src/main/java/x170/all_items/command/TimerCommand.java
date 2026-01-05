package x170.all_items.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import x170.all_items.AllItems;
import x170.all_items.game.GameManager;
import x170.all_items.game.Timer;

public class TimerCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        dispatcher.register(Commands.literal("timer")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(context -> executeTimer(context.getSource()))
        );
    }

    private static int executeTimer(CommandSourceStack source) {
        boolean isPaused = Timer.toggle();
        source.sendSuccess(() -> Component.literal("Timer " + (isPaused ? "paused" : "resumed")).withStyle(isPaused ? ChatFormatting.RED : ChatFormatting.GREEN), true);

        ServerPlayer player = source.getPlayer();
        if (player != null)
            GameManager.playSoundToPlayer(player, SoundEvents.UI_TOAST_IN);

        AllItems.CONFIG.save();
        return 1;
    }
}
