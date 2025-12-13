package x170.all_items.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import x170.all_items.AllItems;
import x170.all_items.game.Timer;

public class TimerCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("timer")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> executeTimer(context.getSource()))
        );
    }

    private static int executeTimer(ServerCommandSource source) {
        boolean isPaused = Timer.toggle();
        source.sendFeedback(() -> Text.literal("Timer " + (isPaused ? "paused" : "resumed")).formatted(isPaused ? Formatting.RED : Formatting.GREEN), true);

        ServerPlayerEntity player = source.getPlayer();
        if (player != null) player.playSoundToPlayer(SoundEvents.UI_TOAST_IN, SoundCategory.MASTER, 1.0F, 1.0F);

        AllItems.CONFIG.save();
        return 1;
    }
}
