package bukkit.plugin;


import api.ICWorld;
import common.core.*;
import net.minecraft.server.v1_13_R1.ChunkGenerator;
import nms.BiomeGenerator;
import nms.NmsWrapper;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldInitEvent;

import java.util.HashSet;
import java.util.Set;



public class BiomeGeneratorListener implements Listener {
    private final Set<String> worldsDone;
    private final IslandCraftPlugin islandCraft;
    private final IslandDatabase database;
    private final NmsWrapper nms;
    private final IslandCache cache;
    private final ICClassLoader classLoader;

    public BiomeGeneratorListener(final IslandCraftPlugin plugin, final IslandDatabase database, final NmsWrapper nms) {
        this.islandCraft = plugin;
        this.database = database;
        this.nms = nms;
        if (!islandCraft.getConfig().contains("worlds") || !islandCraft.getConfig().isConfigurationSection("worlds")) {
            ICLogger.logger.warning("No configuration section for 'worlds' found in config.yml");
            throw new IllegalArgumentException("No configuration section for 'worlds' found in config.yml");
        }
        worldsDone = new HashSet<>();
        cache = new IslandCache();
        classLoader = new ICClassLoader();
    }

    @EventHandler
    public void onWorldInit(final WorldInitEvent event) {
        System.out.println("FIRED1");
        final World world = event.getWorld();
        final String worldName = world.getName();

        islandCraft.reloadConfig();
        final ConfigurationSection worlds = islandCraft.getConfig().getConfigurationSection("worlds");
        if (worlds == null) {
            System.out.println("2");
            return;
        }

        final ConfigurationSection config = worlds.getConfigurationSection(worldName);
        if (config == null) {
            System.out.println("3");
            return;
        }

        ICLogger.logger.info("Installing biome generator in WorldInitEvent for world with name: " + worldName);
        final ICWorld icWorld = new DefaultWorld(worldName, world.getSeed(), database, new BukkitWorldConfig(worldName, config), cache, classLoader);
        final BiomeGenerator biomeGenerator = new IslandCraftBiomeGenerator(icWorld);

        nms.installBiomeGenerator(((CraftWorld) world).getHandle(), biomeGenerator);
        worldsDone.add(worldName);
        islandCraft.getIslandCraft().addWorld(icWorld);
        System.out.println("DONE1");
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        System.out.println("FIRED");
        World world = event.getWorld();
        String worldName = world.getName();

        if (this.worldsDone.contains(worldName)) {
            return;
        }

        ConfigurationSection worlds = this.islandCraft.getConfig().getConfigurationSection("worlds");
        if (worlds == null) {
            return;
        }

        ConfigurationSection config = worlds.getConfigurationSection(worldName);
        if (config == null) {
            return;
        }

        ICLogger.logger.info("Installing biome generator in ChunkLoadEvent for world with name: " + worldName);
        ICWorld icWorld = new DefaultWorld(worldName, world.getSeed(), this.database, new BukkitWorldConfig(worldName, config), this.cache, this.classLoader);
        BiomeGenerator biomeGenerator = new IslandCraftBiomeGenerator(icWorld);
        nms.installBiomeGenerator(((CraftWorld)world).getHandle(), biomeGenerator);

        if (this.database.isEmpty(worldName)) {
            Chunk chunk = event.getChunk();
            ICLogger.logger.info(String.format("Regenerating spawn chunk at x: %d, z: %d", chunk.getX(), chunk.getZ()));
            world.regenerateChunk(chunk.getX(), chunk.getZ());
            System.out.println("FIRED11");
        }

        this.worldsDone.add(worldName);
        this.islandCraft.getIslandCraft().addWorld(icWorld);
        System.out.println("FIRED1111111");

    }
}
