package x170.all_items.game;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import x170.all_items.AllItems;

import java.util.ArrayList;

public abstract class GameManager {
    public static ArrayList<Item> unobtainedItems = new ArrayList<>();
    private static final ServerBossBar bossBar = new ServerBossBar(
            Text.literal("All Items"),
            ServerBossBar.Color.WHITE,
            ServerBossBar.Style.PROGRESS
    );

    public static void onServerTick(MinecraftServer server) {
        int ticks = AllItems.SERVER.getTicks();
        boolean onceASecond = ticks % 20 == 0;
        boolean onceEveryFiveMinutes = ticks % (20 * 60 * 5) == 0;

        // Update every second
        if (onceASecond) {
            Timer.tick();
            showTimerToAllPlayers();
        }

        // Auto-save every 5 minutes
        if (onceEveryFiveMinutes) {
            AllItems.CONFIG.save();
            // Aoc.LOGGER.info("Auto-saved the config file");
        }
    }

    public static void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        bossBar.addPlayer(handler.player);

        // Show a warning message if the timer is paused
        if (Timer.isPaused()) handler.player.sendMessage(
                Text.literal("The timer is paused!\n Use ").formatted(Formatting.RED)
                        .append(Text.literal("/timer").formatted(Formatting.YELLOW))  // .setStyle(Style.EMPTY.withClickEvent(new ClickEvent.SuggestCommand("/timer"))))
                        .append(Text.literal(" to unpause it").formatted(Formatting.RED)),
                false
        );
    }

    public static void onItemObtained(Item obtainedItem) {
        AllItems.CONFIG.obtainedItems.add(AllItems.CONFIG.activeItem);
        AllItems.CONFIG.activeItem = null;
        setActiveItemIfNull();

        playSoundToAllPlayers(SoundEvents.ENTITY_PLAYER_LEVELUP);
        AllItems.SERVER.getPlayerManager().broadcast(
                Text.literal("+ ")
                        .append(Text.translatable(obtainedItem.getTranslationKey()))
                        .formatted(Formatting.GREEN),
                false
        );

        if (unobtainedItems.isEmpty()) onGameFinished();
    }

    public static void onGameFinished() {
        Timer.pause();

        bossBar.setPercent(1.0f);
        bossBar.setName(Text.literal("All Items - Completed!").formatted(Formatting.GOLD));

        playSoundToAllPlayers(SoundEvents.ENTITY_ENDER_DRAGON_DEATH);

        MutableText chatMsg = Text.literal("\n\n\n\n\n\n\n\n\n\n\n §lAll Items completed!\n").formatted(Formatting.GOLD)
                .append(Text.literal("It took you " + Timer.formatTime(Timer.getTimeSeconds(), false) + " to collect every single item in this game!\n").formatted(Formatting.WHITE))
                .append(Text.literal("Congratulations on completing this crazy challenge!\n").formatted(Formatting.GOLD))
                .append(Text.literal("Now go out and touch some grass!\n\n").formatted(Formatting.GREEN));
        AllItems.SERVER.getPlayerManager().broadcast(chatMsg, false);
    }

    public static void checkItem(ServerPlayerEntity player, ItemStack itemStack) {
        if (AllItems.CONFIG.activeItem != null && itemStack.isOf(AllItems.CONFIG.activeItem)) {
            onItemObtained(itemStack.getItem());
        }
    }

    public static void setActiveItemIfNull() {
        if (AllItems.CONFIG.activeItem == null && !unobtainedItems.isEmpty()) {
            // Set the active item to a random unobtained item
            AllItems.CONFIG.activeItem = unobtainedItems.remove(
                    AllItems.SERVER.getOverworld().random.nextInt(unobtainedItems.size())
            );
            AllItems.CONFIG.save();
        }
        updateBossBar();
    }

    private static void updateBossBar() {
        if (AllItems.CONFIG.activeItem == null) return;

        bossBar.setPercent((float) AllItems.CONFIG.obtainedItems.size() / (AllItems.CONFIG.obtainedItems.size() + unobtainedItems.size()));
        bossBar.setName(
                Text.translatable(AllItems.CONFIG.activeItem.getTranslationKey())
                        .append(Text.literal(" [" + (AllItems.CONFIG.obtainedItems.size()) + "/" + (AllItems.CONFIG.obtainedItems.size() + unobtainedItems.size()) + "]"))
//                        .append(Text.object(new AtlasTextObjectContents(Atlases.BLOCKS, Identifier.of("block/fire_0"))))  // Display icon
//                        .append(Text.literal(" "))
        );
    }

    private static void playSoundToAllPlayers(SoundEvent soundEvent) {
        AllItems.SERVER.getPlayerManager().getPlayerList().forEach(serverPlayer -> {
            serverPlayer.playSoundToPlayer(soundEvent, SoundCategory.MASTER, 1.0F, 1.0F);
        });
    }

    private static void showTimerToAllPlayers() {
        // Show the timer in every player's actionbar
        Text text = Text.literal(Timer.getTimeString()).formatted(Formatting.GOLD, Formatting.BOLD);
        AllItems.SERVER.getPlayerManager().getPlayerList().forEach(
                player -> player.sendMessage(text, true)
        );
    }
}
