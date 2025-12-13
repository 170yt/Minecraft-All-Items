package x170.all_items;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import x170.all_items.command.TimerCommand;
import x170.all_items.config.GameConfig;
import x170.all_items.game.GameManager;
import x170.all_items.event.PlayerInventoryChangedCallback;

public class AllItems implements ModInitializer {
	public static final String MOD_ID = "all-items";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static MinecraftServer SERVER;
    public static GameConfig CONFIG = new GameConfig();

    @Override
    public void onInitialize() {
        CONFIG.load();

        GameManager.unobtainedItems.addAll(
                Registries.ITEM.stream()
                        .filter(item -> !CONFIG.obtainedItems.contains(item))
                        .filter(item -> !CONFIG.unobtainableItems.contains(item))
                        .toList()
        );

        CommandRegistrationCallback.EVENT.register(TimerCommand::register);
        PlayerInventoryChangedCallback.EVENT.register(GameManager::checkItem);

        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);
        ServerTickEvents.START_SERVER_TICK.register(GameManager::onServerTick);
        ServerPlayConnectionEvents.JOIN.register(GameManager::onPlayerJoin);

        LOGGER.info("All Items mod loaded");
    }

    private void onServerStarted(MinecraftServer server) {
        SERVER = server;
        GameManager.setActiveItemIfNull();
    }

    private void onServerStopped(MinecraftServer minecraftServer) {
        CONFIG.save();
        LOGGER.info("All Items mod unloaded");
    }
}
