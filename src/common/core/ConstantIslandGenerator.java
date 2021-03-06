package common.core;

import api.ICBiome;
import api.IslandGenerator;
import common.util.StringUtils;

import java.util.Arrays;



public class ConstantIslandGenerator implements IslandGenerator  {
    private final ICBiome biome;

    public ConstantIslandGenerator(final String[] args) {
        ICLogger.logger.info("Creating ConstantIslandGenerator with args: " + StringUtils.join(args, " "));
        if (args.length != 1) {
            ICLogger.logger.error("ConstantIslandGenerator requrires 1 parameter, " + args.length + " given");
            throw new IllegalArgumentException("ConstantIslandGenerator requrires 1 parameter, " + args.length + " given");
        }
        biome = ICBiome.valueOf(args[0]);
    }

    @Override
    public ICBiome[] generate(final int xSize, final int zSize, final long islandSeed) {
        ICLogger.logger.info(String.format("Generating island from ConstantIslandGenerator with xSize: %d, zSize: %d, islandSeed: %d, biome: %s", xSize, zSize, islandSeed, biome));
        final ICBiome[] result = new ICBiome[xSize * zSize];
        Arrays.fill(result, biome);
        return result;
    }
}
