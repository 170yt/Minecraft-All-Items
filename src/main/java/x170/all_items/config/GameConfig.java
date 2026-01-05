package x170.all_items.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import x170.all_items.AllItems;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GameConfig {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(AllItems.MOD_ID + ".json");

    // Configuration fields
    @Expose
    public boolean showIcons = false;
    @Expose
    public int timerSeconds = 0;
    @Expose
    public Item activeItem = null;
    @Expose
    public ArrayList<Item> obtainedItems = new ArrayList<>();
    @Expose
    public ArrayList<Item> unobtainableItems = new ArrayList<>() {{
        add(Items.AIR);
        add(Items.BARRIER);
        add(Items.BEDROCK);
        add(Items.BUDDING_AMETHYST);
        add(Items.CHAIN_COMMAND_BLOCK);
        add(Items.CHORUS_PLANT);
        add(Items.COMMAND_BLOCK_MINECART);
        add(Items.COMMAND_BLOCK);
        add(Items.DEBUG_STICK);
        add(Items.END_PORTAL_FRAME);
        add(Items.FARMLAND);
        add(Items.FROGSPAWN);
        add(Items.INFESTED_CHISELED_STONE_BRICKS);
        add(Items.INFESTED_COBBLESTONE);
        add(Items.INFESTED_CRACKED_STONE_BRICKS);
        add(Items.INFESTED_DEEPSLATE);
        add(Items.INFESTED_MOSSY_STONE_BRICKS);
        add(Items.INFESTED_STONE_BRICKS);
        add(Items.INFESTED_STONE);
        add(Items.JIGSAW);
        add(Items.KNOWLEDGE_BOOK);
        add(Items.LIGHT);
        add(Items.PETRIFIED_OAK_SLAB);
        add(Items.PLAYER_HEAD);
        add(Items.REINFORCED_DEEPSLATE);
        add(Items.REPEATING_COMMAND_BLOCK);
        add(Items.SPAWNER);
        add(Items.STRUCTURE_BLOCK);
        add(Items.STRUCTURE_VOID);
        add(Items.TEST_BLOCK);
        add(Items.TEST_INSTANCE_BLOCK);
        add(Items.TRIAL_SPAWNER);
        add(Items.VAULT);

        add(Items.ARMADILLO_SPAWN_EGG);
        add(Items.ALLAY_SPAWN_EGG);
        add(Items.AXOLOTL_SPAWN_EGG);
        add(Items.BAT_SPAWN_EGG);
        add(Items.BEE_SPAWN_EGG);
        add(Items.BLAZE_SPAWN_EGG);
        add(Items.BOGGED_SPAWN_EGG);
        add(Items.BREEZE_SPAWN_EGG);
        add(Items.CAT_SPAWN_EGG);
        add(Items.CAMEL_SPAWN_EGG);
        add(Items.CAMEL_HUSK_SPAWN_EGG);
        add(Items.CAVE_SPIDER_SPAWN_EGG);
        add(Items.CHICKEN_SPAWN_EGG);
        add(Items.COD_SPAWN_EGG);
        add(Items.COPPER_GOLEM_SPAWN_EGG);
        add(Items.COW_SPAWN_EGG);
        add(Items.CREEPER_SPAWN_EGG);
        add(Items.DOLPHIN_SPAWN_EGG);
        add(Items.DONKEY_SPAWN_EGG);
        add(Items.DROWNED_SPAWN_EGG);
        add(Items.ELDER_GUARDIAN_SPAWN_EGG);
        add(Items.ENDER_DRAGON_SPAWN_EGG);
        add(Items.ENDERMAN_SPAWN_EGG);
        add(Items.ENDERMITE_SPAWN_EGG);
        add(Items.EVOKER_SPAWN_EGG);
        add(Items.FOX_SPAWN_EGG);
        add(Items.FROG_SPAWN_EGG);
        add(Items.GHAST_SPAWN_EGG);
        add(Items.HAPPY_GHAST_SPAWN_EGG);
        add(Items.GLOW_SQUID_SPAWN_EGG);
        add(Items.GOAT_SPAWN_EGG);
        add(Items.GUARDIAN_SPAWN_EGG);
        add(Items.HOGLIN_SPAWN_EGG);
        add(Items.HORSE_SPAWN_EGG);
        add(Items.HUSK_SPAWN_EGG);
        add(Items.IRON_GOLEM_SPAWN_EGG);
        add(Items.LLAMA_SPAWN_EGG);
        add(Items.MAGMA_CUBE_SPAWN_EGG);
        add(Items.MOOSHROOM_SPAWN_EGG);
        add(Items.MULE_SPAWN_EGG);
        add(Items.NAUTILUS_SPAWN_EGG);
        add(Items.OCELOT_SPAWN_EGG);
        add(Items.PANDA_SPAWN_EGG);
        add(Items.PARCHED_SPAWN_EGG);
        add(Items.PARROT_SPAWN_EGG);
        add(Items.PHANTOM_SPAWN_EGG);
        add(Items.PIG_SPAWN_EGG);
        add(Items.PIGLIN_SPAWN_EGG);
        add(Items.PIGLIN_BRUTE_SPAWN_EGG);
        add(Items.PILLAGER_SPAWN_EGG);
        add(Items.POLAR_BEAR_SPAWN_EGG);
        add(Items.PUFFERFISH_SPAWN_EGG);
        add(Items.RABBIT_SPAWN_EGG);
        add(Items.RAVAGER_SPAWN_EGG);
        add(Items.SALMON_SPAWN_EGG);
        add(Items.SHEEP_SPAWN_EGG);
        add(Items.SHULKER_SPAWN_EGG);
        add(Items.SILVERFISH_SPAWN_EGG);
        add(Items.SKELETON_SPAWN_EGG);
        add(Items.SKELETON_HORSE_SPAWN_EGG);
        add(Items.SLIME_SPAWN_EGG);
        add(Items.SNIFFER_SPAWN_EGG);
        add(Items.SNOW_GOLEM_SPAWN_EGG);
        add(Items.SPIDER_SPAWN_EGG);
        add(Items.SQUID_SPAWN_EGG);
        add(Items.STRAY_SPAWN_EGG);
        add(Items.STRIDER_SPAWN_EGG);
        add(Items.TADPOLE_SPAWN_EGG);
        add(Items.TRADER_LLAMA_SPAWN_EGG);
        add(Items.TROPICAL_FISH_SPAWN_EGG);
        add(Items.TURTLE_SPAWN_EGG);
        add(Items.VEX_SPAWN_EGG);
        add(Items.VILLAGER_SPAWN_EGG);
        add(Items.VINDICATOR_SPAWN_EGG);
        add(Items.WANDERING_TRADER_SPAWN_EGG);
        add(Items.WARDEN_SPAWN_EGG);
        add(Items.WITCH_SPAWN_EGG);
        add(Items.WITHER_SPAWN_EGG);
        add(Items.WITHER_SKELETON_SPAWN_EGG);
        add(Items.WOLF_SPAWN_EGG);
        add(Items.ZOGLIN_SPAWN_EGG);
        add(Items.CREAKING_SPAWN_EGG);
        add(Items.ZOMBIE_SPAWN_EGG);
        add(Items.ZOMBIE_HORSE_SPAWN_EGG);
        add(Items.ZOMBIE_NAUTILUS_SPAWN_EGG);
        add(Items.ZOMBIE_VILLAGER_SPAWN_EGG);
        add(Items.ZOMBIFIED_PIGLIN_SPAWN_EGG);
    }};

    public void load() {
        // Load the config file
        if (!Files.exists(PATH)) {
            save();
            return;
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemTypeAdapter())
                .create();
        try {
            GameConfig config = gson.fromJson(Files.readString(PATH), this.getClass());
            this.showIcons = config.showIcons;
            this.timerSeconds = config.timerSeconds;
            this.activeItem = config.activeItem;
            this.obtainedItems = config.obtainedItems;
            this.unobtainableItems = config.unobtainableItems;
        } catch (Exception e) {
            AllItems.LOGGER.error("Failed to load config file: {}", e.getMessage());
        }
    }

    public void save() {
        // Save the config file
        try {
            Files.createDirectories(PATH.getParent());
        } catch (Exception e) {
            AllItems.LOGGER.error("Failed to create directories for config file: {}", e.getMessage());
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemTypeAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        try {
            String json = gson.toJson(this);
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(PATH))) {
                writer.println(json);
                writer.flush();
            }
        } catch (Exception e) {
            AllItems.LOGGER.error("Failed to save config file: {}", e.getMessage());
        }
    }
}
