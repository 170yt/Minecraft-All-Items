package x170.all_items.game;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import x170.all_items.AllItems;

import java.util.ArrayList;

public abstract class GameManager {
    private static final ServerBossEvent bossBar = new ServerBossEvent(
            Component.literal("All Items"),
            ServerBossEvent.BossBarColor.WHITE,
            BossEvent.BossBarOverlay.PROGRESS
    );
    public static ArrayList<Item> unobtainedItems = new ArrayList<>();

    public static void onServerTick(MinecraftServer server) {
        int ticks = AllItems.SERVER.getTickCount();
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

    public static void onPlayerJoin(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
        bossBar.addPlayer(handler.player);

        // Show a warning message if the timer is paused
        if (Timer.isPaused()) handler.player.displayClientMessage(
                Component.literal("The timer is paused!\n Use ").withStyle(ChatFormatting.RED)
                        .append(Component.literal("/timer").withStyle(ChatFormatting.YELLOW))  // .setStyle(Style.EMPTY.withClickEvent(new ClickEvent.SuggestCommand("/timer"))))
                        .append(Component.literal(" to unpause it").withStyle(ChatFormatting.RED)),
                false
        );
    }

    public static void onItemObtained(Item obtainedItem) {
        AllItems.CONFIG.obtainedItems.add(AllItems.CONFIG.activeItem);
        AllItems.CONFIG.activeItem = null;
        setActiveItemIfNull();

        playSoundToAllPlayers(SoundEvents.PLAYER_LEVELUP);
        AllItems.SERVER.getPlayerList().broadcastSystemMessage(
                Component.literal("+ ").append(Component.translatable(obtainedItem.getDescriptionId())).withStyle(ChatFormatting.GREEN),
                false
        );

        if (unobtainedItems.isEmpty()) onGameFinished();
    }

    public static void onGameFinished() {
        Timer.pause();

        bossBar.setProgress(1.0f);
        bossBar.setName(Component.literal("All Items - Completed!").withStyle(ChatFormatting.GOLD));

        playSoundToAllPlayers(SoundEvents.ENDER_DRAGON_DEATH);

        MutableComponent chatMsg = Component.literal("\n\n\n\n\n\n\n\n\n\n\n §lAll Items completed!\n").withStyle(ChatFormatting.GOLD)
                .append(Component.literal("It took you " + Timer.formatTime(Timer.getTimeSeconds(), false) + " to collect every single item in this game!\n").withStyle(ChatFormatting.WHITE))
                .append(Component.literal("Congratulations on completing this crazy challenge!\n").withStyle(ChatFormatting.GOLD))
                .append(Component.literal("Now go out and touch some grass!\n\n").withStyle(ChatFormatting.GREEN));
        AllItems.SERVER.getPlayerList().broadcastSystemMessage(chatMsg, false);
    }

    public static void checkItem(ServerPlayer player, ItemStack itemStack) {
        if (AllItems.CONFIG.activeItem != null && itemStack.is(AllItems.CONFIG.activeItem)) {
            onItemObtained(itemStack.getItem());
        }
    }

    public static void setActiveItemIfNull() {
        if (AllItems.CONFIG.activeItem == null && !unobtainedItems.isEmpty()) {
            // Set the active item to a random unobtained item
            AllItems.CONFIG.activeItem = unobtainedItems.remove(
                    AllItems.SERVER.overworld().random.nextInt(unobtainedItems.size())
            );
            AllItems.CONFIG.save();
        }
        updateBossBar();
    }

    private static void updateBossBar() {
        if (AllItems.CONFIG.activeItem == null) return;

        // Additional info for undescriptive item names
        String additionalInfo = "";
        String activeItemId = AllItems.CONFIG.activeItem.toString();
        if (activeItemId.startsWith("minecraft:music_disc_")) {
            additionalInfo = " (" + activeItemId.replace("minecraft:music_disc_", "").replace("_", " ") + ")";
        }

        bossBar.setProgress((float) AllItems.CONFIG.obtainedItems.size() / (AllItems.CONFIG.obtainedItems.size() + unobtainedItems.size()));
        bossBar.setName(
                Component.translatable(AllItems.CONFIG.activeItem.getDescriptionId())
                        .append(Component.literal(additionalInfo))
                        .append(Component.literal(" [" + (AllItems.CONFIG.obtainedItems.size()) + "/" + (AllItems.CONFIG.obtainedItems.size() + unobtainedItems.size()) + "]"))
//                        .append(Component.object(new AtlasSprite(AtlasIds.BLOCKS, Identifier.parse("block/fire_0"))))  // Display icon
//                        .append(Component.object(new AtlasSprite(AtlasIds.ITEMS, Identifier.parse("item/pink_bundle"))))  // Display icon
//                        .append(Component.literal(" "))
        );
    }

    private static void playSoundToAllPlayers(SoundEvent soundEvent) {
        AllItems.SERVER.getPlayerList().getPlayers().forEach(player -> {
            player.level().playSound(null, player.blockPosition(), soundEvent, SoundSource.MASTER);
        });
    }

    private static void showTimerToAllPlayers() {
        // Show the timer in every player's actionbar
        Component text = Component.literal(Timer.getTimeString()).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD);
        AllItems.SERVER.getPlayerList().getPlayers().forEach(
                player -> player.displayClientMessage(text, true)
        );
    }
}
