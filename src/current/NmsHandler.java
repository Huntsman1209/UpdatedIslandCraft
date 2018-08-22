package current;

import net.minecraft.server.v1_13_R1.World;
import nms.BiomeGenerator;
import nms.NmsWrapper;


public class NmsHandler extends NmsWrapper {

    @Override
    public boolean installBiomeGenerator(final World world, final BiomeGenerator biomeGenerator) {
        try {
            new Utils().register(world, biomeGenerator);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}