package x170.all_items.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public interface PlayerInventoryChangedCallback {
    Event<PlayerInventoryChangedCallback> EVENT = EventFactory.createArrayBacked(PlayerInventoryChangedCallback.class, (listeners) -> (player, itemStack) -> {
        for (PlayerInventoryChangedCallback listener : listeners) {
            listener.interact(player, itemStack);
        }
    });

    void interact(ServerPlayer player, ItemStack itemStack);
}
