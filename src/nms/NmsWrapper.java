package nms;


import current.NmsHandler;
import net.minecraft.server.v1_13_R1.World;
import org.bukkit.Server;


import java.lang.reflect.InvocationTargetException;

public abstract class NmsWrapper {
    public static NmsWrapper getInstance(final Server server) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> myClass = NmsHandler.class;
        return (NmsWrapper) myClass.getConstructor().newInstance();
    }

    public abstract boolean installBiomeGenerator(World world, BiomeGenerator biomeGenerator);
}
