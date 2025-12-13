package x170.all_items.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import x170.all_items.AllItems;
import x170.all_items.game.Timer;

public class TimerCommand {
    public static final PermissionCheck PERMISSION_CHECK;

    static {
        PERMISSION_CHECK = new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        dispatcher.register(Commands.literal("timer")
                .requires(Commands.hasPermission(PERMISSION_CHECK))
                .executes(context -> executeTimer(context.getSource()))
        );
    }

    private static int executeTimer(CommandSourceStack source) {
        boolean isPaused = Timer.toggle();
        source.sendSuccess(() -> Component.literal("Timer " + (isPaused ? "paused" : "resumed")).withStyle(isPaused ? ChatFormatting.RED : ChatFormatting.GREEN), true);

        ServerPlayer player = source.getPlayer();
        if (player != null) player.level().playSound(null, player.blockPosition(), SoundEvents.UI_TOAST_IN, SoundSource.MASTER);

        AllItems.CONFIG.save();
        return 1;
    }
}
