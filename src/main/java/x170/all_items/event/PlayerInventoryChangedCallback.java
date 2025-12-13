package x170.all_items.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerInventoryChangedCallback {
    Event<PlayerInventoryChangedCallback> EVENT = EventFactory.createArrayBacked(PlayerInventoryChangedCallback.class, (listeners) -> (player, itemStack) -> {
        for (PlayerInventoryChangedCallback listener : listeners) {
            listener.interact(player, itemStack);
        }
    });

    void interact(ServerPlayerEntity player, ItemStack itemStack);
}
