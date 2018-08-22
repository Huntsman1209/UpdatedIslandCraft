package current;

import java.lang.reflect.Field;
import java.util.*;

import api.ICBiome;
import com.google.common.collect.Sets;
import net.minecraft.server.v1_13_R1.*;
import nms.BiomeGenerator;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.block.Biome;

import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.block.CraftBlock;

import javax.annotation.Nullable;

public class CustomWorldChunkManager extends WorldChunkManager{
    private static final Map<ICBiome, BiomeBase> biomeMap = new EnumMap(ICBiome.class);
    private final BiomeCache biomeCache;
    private final BiomeGenerator biomeGenerator;
    private final AreaFactory<AreaLazy> areaFactory;


    static
    {
        biomeMap.put(ICBiome.BEACH, CraftBlock.biomeToBiomeBase(Biome.BEACH));
        biomeMap.put(ICBiome.BIRCH_FOREST, CraftBlock.biomeToBiomeBase(Biome.BIRCH_FOREST));
        biomeMap.put(ICBiome.BIRCH_FOREST_HILLS, CraftBlock.biomeToBiomeBase(Biome.BIRCH_FOREST_HILLS));
        biomeMap.put(ICBiome.BIRCH_FOREST_HILLS_M, CraftBlock.biomeToBiomeBase(Biome.TALL_BIRCH_HILLS));
        biomeMap.put(ICBiome.BIRCH_FOREST_M, CraftBlock.biomeToBiomeBase(Biome.TALL_BIRCH_FOREST));
        biomeMap.put(ICBiome.COLD_BEACH, CraftBlock.biomeToBiomeBase(Biome.SNOWY_BEACH));
        biomeMap.put(ICBiome.COLD_TAIGA, CraftBlock.biomeToBiomeBase(Biome.SNOWY_TAIGA));
        biomeMap.put(ICBiome.COLD_TAIGA_HILLS, CraftBlock.biomeToBiomeBase(Biome.SNOWY_TAIGA_HILLS));
        biomeMap.put(ICBiome.COLD_TAIGA_M, CraftBlock.biomeToBiomeBase(Biome.SNOWY_TAIGA_MOUNTAINS));
        biomeMap.put(ICBiome.DEEP_OCEAN, CraftBlock.biomeToBiomeBase(Biome.DEEP_OCEAN));
        biomeMap.put(ICBiome.DESERT, CraftBlock.biomeToBiomeBase(Biome.DESERT));
        biomeMap.put(ICBiome.DESERT_HILLS, CraftBlock.biomeToBiomeBase(Biome.DESERT_HILLS));
        biomeMap.put(ICBiome.DESERT_M, CraftBlock.biomeToBiomeBase(Biome.DESERT_LAKES));
        biomeMap.put(ICBiome.END, CraftBlock.biomeToBiomeBase(Biome.THE_END));
        biomeMap.put(ICBiome.EXTREME_HILLS, CraftBlock.biomeToBiomeBase(Biome.MOUNTAINS));
        biomeMap.put(ICBiome.EXTREME_HILLS_EDGE, CraftBlock.biomeToBiomeBase(Biome.MOUNTAIN_EDGE));
        biomeMap.put(ICBiome.EXTREME_HILLS_M, CraftBlock.biomeToBiomeBase(Biome.GRAVELLY_MOUNTAINS));
        biomeMap.put(ICBiome.EXTREME_HILLS_PLUS, CraftBlock.biomeToBiomeBase(Biome.MODIFIED_GRAVELLY_MOUNTAINS));
        biomeMap.put(ICBiome.EXTREME_HILLS_PLUS_M, CraftBlock.biomeToBiomeBase(Biome.SNOWY_MOUNTAINS));
        biomeMap.put(ICBiome.FLOWER_FOREST, CraftBlock.biomeToBiomeBase(Biome.FLOWER_FOREST));
        biomeMap.put(ICBiome.FOREST, CraftBlock.biomeToBiomeBase(Biome.FOREST));
        biomeMap.put(ICBiome.FOREST_HILLS, CraftBlock.biomeToBiomeBase(Biome.BIRCH_FOREST_HILLS));
        biomeMap.put(ICBiome.FROZEN_OCEAN, CraftBlock.biomeToBiomeBase(Biome.FROZEN_OCEAN));
        biomeMap.put(ICBiome.FROZEN_RIVER, CraftBlock.biomeToBiomeBase(Biome.FROZEN_RIVER));
        biomeMap.put(ICBiome.ICE_MOUNTAINS, CraftBlock.biomeToBiomeBase(Biome.ICE_SPIKES));
        biomeMap.put(ICBiome.ICE_PLAINS, CraftBlock.biomeToBiomeBase(Biome.ICE_SPIKES));
        biomeMap.put(ICBiome.ICE_PLAINS_SPIKES, CraftBlock.biomeToBiomeBase(Biome.ICE_SPIKES));
        biomeMap.put(ICBiome.JUNGLE, CraftBlock.biomeToBiomeBase(Biome.JUNGLE));
        biomeMap.put(ICBiome.JUNGLE_EDGE, CraftBlock.biomeToBiomeBase(Biome.JUNGLE_EDGE));
        biomeMap.put(ICBiome.JUNGLE_HILLS, CraftBlock.biomeToBiomeBase(Biome.JUNGLE_HILLS));
        biomeMap.put(ICBiome.JUNGLE_M, CraftBlock.biomeToBiomeBase(Biome.MODIFIED_JUNGLE));
        biomeMap.put(ICBiome.JUNGLE_EDGE_M, CraftBlock.biomeToBiomeBase(Biome.MODIFIED_JUNGLE_EDGE));
        biomeMap.put(ICBiome.MEGA_SPRUCE_TAIGA, CraftBlock.biomeToBiomeBase(Biome.GIANT_SPRUCE_TAIGA));
        biomeMap.put(ICBiome.MEGA_SPRUCE_TAIGA_HILLS, CraftBlock.biomeToBiomeBase(Biome.GIANT_SPRUCE_TAIGA_HILLS));
        biomeMap.put(ICBiome.MEGA_TAIGA, CraftBlock.biomeToBiomeBase(Biome.GIANT_TREE_TAIGA));
        biomeMap.put(ICBiome.MEGA_TAIGA_HILLS, CraftBlock.biomeToBiomeBase(Biome.GIANT_TREE_TAIGA_HILLS));
        biomeMap.put(ICBiome.MESA, CraftBlock.biomeToBiomeBase(Biome.BADLANDS));
        biomeMap.put(ICBiome.MESA_BRYCE, CraftBlock.biomeToBiomeBase(Biome.ERODED_BADLANDS));
        biomeMap.put(ICBiome.MESA_PLATEAU, CraftBlock.biomeToBiomeBase(Biome.BADLANDS_PLATEAU));
        biomeMap.put(ICBiome.MESA_PLATEAU_F, CraftBlock.biomeToBiomeBase(Biome.MODIFIED_BADLANDS_PLATEAU));
        biomeMap.put(ICBiome.MESA_PLATEAU_F_M, CraftBlock.biomeToBiomeBase(Biome.MODIFIED_BADLANDS_PLATEAU));
        biomeMap.put(ICBiome.MESA_PLATEAU_M, CraftBlock.biomeToBiomeBase(Biome.MODIFIED_WOODED_BADLANDS_PLATEAU));
        biomeMap.put(ICBiome.MUSHROOM_ISLAND, CraftBlock.biomeToBiomeBase(Biome.MUSHROOM_FIELDS));
        biomeMap.put(ICBiome.MUSHROOM_ISLAND_SHORE, CraftBlock.biomeToBiomeBase(Biome.MUSHROOM_FIELD_SHORE));
        biomeMap.put(ICBiome.NETHER, CraftBlock.biomeToBiomeBase(Biome.NETHER));
        biomeMap.put(ICBiome.OCEAN, CraftBlock.biomeToBiomeBase(Biome.OCEAN));
        biomeMap.put(ICBiome.PLAINS, CraftBlock.biomeToBiomeBase(Biome.PLAINS));
        biomeMap.put(ICBiome.RIVER, CraftBlock.biomeToBiomeBase(Biome.RIVER));
        biomeMap.put(ICBiome.ROOFED_FOREST, CraftBlock.biomeToBiomeBase(Biome.DARK_FOREST));
        biomeMap.put(ICBiome.ROOFED_FOREST_M, CraftBlock.biomeToBiomeBase(Biome.DARK_FOREST_HILLS));
        biomeMap.put(ICBiome.SAVANNA, CraftBlock.biomeToBiomeBase(Biome.SAVANNA));
        biomeMap.put(ICBiome.SAVANNA_M, CraftBlock.biomeToBiomeBase(Biome.SHATTERED_SAVANNA));
        biomeMap.put(ICBiome.SAVANNA_PLATEAU, CraftBlock.biomeToBiomeBase(Biome.SHATTERED_SAVANNA_PLATEAU));
        biomeMap.put(ICBiome.SAVANNA_PLATEAU_M, CraftBlock.biomeToBiomeBase(Biome.SHATTERED_SAVANNA_PLATEAU));
        biomeMap.put(ICBiome.STONE_BEACH, CraftBlock.biomeToBiomeBase(Biome.STONE_SHORE));
        biomeMap.put(ICBiome.SUNFLOWER_PLAINS, CraftBlock.biomeToBiomeBase(Biome.SUNFLOWER_PLAINS));
        biomeMap.put(ICBiome.SWAMPLAND, CraftBlock.biomeToBiomeBase(Biome.SWAMP));
        biomeMap.put(ICBiome.SWAMPLAND_M, CraftBlock.biomeToBiomeBase(Biome.SWAMP_HILLS));
        biomeMap.put(ICBiome.TAIGA, CraftBlock.biomeToBiomeBase(Biome.TAIGA));
        biomeMap.put(ICBiome.TAIGA_HILLS, CraftBlock.biomeToBiomeBase(Biome.TAIGA_HILLS));
        biomeMap.put(ICBiome.TAIGA_M, CraftBlock.biomeToBiomeBase(Biome.TAIGA_MOUNTAINS));
    }

    public CustomWorldChunkManager(BiomeGenerator biomeGenerator, World world) throws IllegalAccessException {
        this.biomeGenerator = biomeGenerator;
        this.biomeCache = new BiomeCache(this);

        BiomeLayoutOverworldConfiguration overworldConfiguration = new BiomeLayoutOverworldConfiguration();

        GenLayer[] var4 = GenLayers.a(world.getSeed(), world.getWorldData().getType(), overworldConfiguration.b());
        GenLayer layer = var4[0];

        Field field = null;
        try {
           field = layer.getClass().getDeclaredField("a");
           field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        this.areaFactory = (AreaFactory<AreaLazy>) field.get(layer);
    }


    public BiomeBase getBiome(BlockPosition blockPosition, BiomeBase biomeBase) {
        return this.biomeCache.a(blockPosition.getX(), blockPosition.getZ(), biomeBase);
    }

    public boolean a(StructureGenerator<?> var1) {
        return (Boolean)this.a.computeIfAbsent(var1, (var1x) -> {
            List<BiomeBase> var2 = new ArrayList(biomeMap.values());
            int var3 = var2.size();

            for(int var4 = 0; var4 < var3; ++var4) {
                BiomeBase var5 = (BiomeBase)var2.get(var4);
                if (var5.a(var1x)) {
                    return true;
                }
            }

            return false;
        });
    }

    public Set<IBlockData> b() {
        if (this.b.isEmpty()) {
            List<BiomeBase> base = new ArrayList(biomeMap.values());
            Iterator var2 = base.iterator();

            while(var2.hasNext()) {
                BiomeBase var4 = (BiomeBase)var2.next();
                this.b.add(var4.r().a());
            }
        }

        return this.b;
    }

    public BiomeBase[] getBiomes(int xMin, int zMin, int xSize, int zSize) {
        BiomeBase[] var8 = new BiomeBase[xSize * zSize];

        for(int i = 0; i < zSize * xSize; ++i) {
            int x = xMin + i % xSize << 2;
            int z = zMin + i / xSize << 2;

            ICBiome biome = this.biomeGenerator.generateBiome(x, z);
            var8[i] = biomeMap.get(biome);
        }
        return var8;
    }

    public BiomeBase[] a(int xMin, int zMin, int xSize, int zSize, boolean useCache) {
        System.out.println("x min");
        BiomeBase[] result = new BiomeBase[xSize * zSize];

        int i;
        if (xSize == 16 && zSize == 16 && (xMin & 15) == 0 && (zMin & 15) == 0) {
            if (useCache) {
                ICBiome[] temp = this.biomeGenerator.generateChunkBiomes(xMin, zMin);

                for(i = 0; i < xSize * zSize; ++i) {
                    result[i] = biomeMap.get(temp[i]);
                    System.out.println(temp[i]);
                }

                System.out.println("DEBUG[2]: " + result);
                return result;
            } else {
                ICBiome[] temp = this.biomeGenerator.generateChunkBiomes(xMin, zMin);

                AreaDimension var6 = new AreaDimension(xMin, zMin, xSize, zSize);
                AreaLazy var7 = areaFactory.make(var6);

                for(i = 0; i < xSize * zSize; ++i) {
                    result[i] = biomeMap.get(temp[i]);
                    System.out.println(temp[i]);
                }

                System.out.println("DEBUG[2]: " + result);
                return result;
            }
        } else {
            for(int x = 0; x < xSize; ++x) {
                for(i = 0; i < zSize; ++i) {
                    ICBiome temp = this.biomeGenerator.generateBiome(xMin + x, zMin + i);
                    result[x + i * xSize] = biomeMap.get(temp);
                }
            }

            System.out.println("DEBUG[3]: " + result);
            return result;
        }
    }

    public Set<BiomeBase> a(int var1, int var2, int var3) {
        System.out.println("var1");
        int var4 = var1 - var3 >> 2;
        int var5 = var2 - var3 >> 2;
        int var6 = var1 + var3 >> 2;
        int var7 = var2 + var3 >> 2;
        int var8 = var6 - var4 + 1;
        int var9 = var7 - var5 + 1;
        Set set = new HashSet<>();
        Collections.addAll(set, getBiomes(var4, var5, var8, var9));
        System.out.println(set);
        return set;
    }

    @Nullable
    public BlockPosition a(int var1, int var2, int var3, List<BiomeBase> var4, Random var5) {
        System.out.println("1var2");

        int var6 = var1 - var3 >> 2;
        int var7 = var2 - var3 >> 2;
        int var8 = var1 + var3 >> 2;
        int var9 = var2 + var3 >> 2;
        int var10 = var8 - var6 + 1;
        int var11 = var9 - var7 + 1;

        BiomeBase[] var12 = getBiomes(var6, var7, var10, var11);

        BlockPosition var13 = null;

        int var14 = 0;

        for(int var15 = 0; var15 < var10 * var11; ++var15) {
            int var16 = var6 + var15 % var10 << 2;
            int var17 = var7 + var15 / var10 << 2;
            if (var4.contains(var12[var15])) {
                if (var13 == null || var5.nextInt(var14 + 1) == 0) {
                    var13 = new BlockPosition(var16, 0, var17);
                }

                ++var14;
            }
        }

        System.out.println(var13);
        return var13;
    }

    public void Y_() {
        System.out.println("CLEAN");
        this.biomeCache.a();
    }
}