package current;


import bukkit.plugin.IslandCraftPlugin;
import net.minecraft.server.v1_13_R1.*;
import nms.BiomeGenerator;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Utils {

    private IslandCraftPlugin plugin = JavaPlugin.getPlugin(IslandCraftPlugin.class);

     void register(World world, final BiomeGenerator biome) throws IllegalAccessException {

         ChunkGenerator temp = world.worldProvider.getChunkGenerator();
         ChunkGeneratorAbstract generator = (ChunkGeneratorAbstract) temp;

         WorldServer server = (WorldServer) world;

         ChunkProviderServer chunkProviderServer = null;
         try{
             Method method = server.getClass().getDeclaredMethod( "q");
             method.setAccessible(true);
             chunkProviderServer = (ChunkProviderServer) method.invoke(server);
         } catch (InvocationTargetException | NoSuchMethodException e) {
             e.printStackTrace();
         }

         FieldUtils.writeField(generator, "c", new CustomWorldChunkManager(biome, world), true);

         try {
             Field field = chunkProviderServer.getClass().getDeclaredField("chunkGenerator");
             field.setAccessible(true);
             field.set(chunkProviderServer, temp);
         } catch (NoSuchFieldException e) {
             e.printStackTrace();
         }

         CustomWorldProvider provider = new CustomWorldProvider(temp);

         FieldUtils.writeField(world, "worldProvider", provider, true);
         FieldUtils.writeField(world, "chunkProvider", chunkProviderServer, true);

         if(world.worldProvider.getChunkGenerator().getWorldChunkManager() instanceof CustomWorldChunkManager){
             System.out.println("YES");
             System.out.println(world.worldProvider.getChunkGenerator().getWorldChunkManager());
         }else {
             System.out.println("NO");
             System.out.println(world.worldProvider.getChunkGenerator().getWorldChunkManager());
         }

         if(world.getChunkProvider().getChunkGenerator().getWorldChunkManager() instanceof CustomWorldChunkManager){
             System.out.println("YES1");
             System.out.println(world.worldProvider.getChunkGenerator().getWorldChunkManager());
         }else {
             System.out.println("NO1");
             System.out.println(world.worldProvider.getChunkGenerator().getWorldChunkManager());
         }

         if(((WorldServer) world).getChunkProviderServer().getChunkGenerator().getWorldChunkManager() instanceof CustomWorldChunkManager){
             System.out.println("YES2");
             System.out.println(((WorldServer) world).getChunkProviderServer().getChunkGenerator().getWorldChunkManager());
         }else {
             System.out.println("NO2");
             System.out.println(((WorldServer) world).getChunkProviderServer().getChunkGenerator().getWorldChunkManager());
         }
     }
 }
