package common.core;

import api.ICBiome;
import api.IslandGenerator;
import common.util.StringUtils;

public class EmptyIslandGenerator implements IslandGenerator {
    public EmptyIslandGenerator(final String[] args) {
        ICLogger.logger.info("Creating EmptyIslandGenerator with args: " + StringUtils.join(args, " "));
        if (args.length != 0) {
            ICLogger.logger.error("EmptyIslandGenerator requrires 0 parameters, " + args.length + " given");
            throw new IllegalArgumentException("EmptyIslandGenerator requrires 0 parameters, " + args.length + " given");
        }
    }

    @Override
    public ICBiome[] generate(final int xSize, final int zSize, final long islandSeed) {
        ICLogger.logger.info(String.format("Generating island from EmptyIslandGenerator with xSize: %d, zSize: %d, islandSeed: %d", xSize, zSize, islandSeed));
        return new ICBiome[xSize * zSize];
    }
}
