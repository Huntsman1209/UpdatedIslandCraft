package current;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.server.v1_13_R1.*;

import javax.annotation.Nullable;

public class CustomWorldProvider extends WorldProvider {

    private ChunkGenerator generator;
    public CustomWorldProvider(ChunkGenerator generator) {
        this.generator = generator;
    }

    public DimensionManager getDimensionManager() {
        return DimensionManager.OVERWORLD;
    }

    public boolean a(int var1, int var2) {
        return !generator.getWorld().g(var1, var2);
    }

    protected void m() {
        System.out.println("m");
        this.e = true;
    }

    public ChunkGenerator<?> getChunkGenerator() {
        System.out.println("chunk");
        return generator;
    }

    @Nullable
    public BlockPosition a(ChunkCoordIntPair var1, boolean var2) {
        System.out.println("DEBUG: FIRED A1");
        for(int var3 = var1.d(); var3 <= var1.f(); ++var3) {
            for(int var4 = var1.e(); var4 <= var1.g(); ++var4) {
                BlockPosition var5 = this.a(var3, var4, var2);
                if (var5 != null) {
                    System.out.println("DEBUG: done");
                    return var5;
                }
            }
        }
        System.out.println("DEBUG: ERROR");
        return null;
    }

    @Nullable
    public BlockPosition a(int var1, int var2, boolean var3) {
        System.out.println("DEBUG: FIRED AAAA");
        BlockPosition.MutableBlockPosition var4 = new BlockPosition.MutableBlockPosition(var1, 0, var2);
        BiomeBase var5 = this.b.getBiome(var4);
        IBlockData var6 = var5.r().a();
        if (var3 && !var6.getBlock().a(TagsBlock.I)) {
            System.out.println("Error at AAA");
            return null;
        } else {
            Chunk var7 = this.b.getChunkAt(var1 >> 4, var2 >> 4);
            int var8 = var7.a(HeightMap.Type.MOTION_BLOCKING, var1 & 15, var2 & 15);
            if (var8 < 0) {
                System.out.println("Error at AAA");
                return null;
            } else if (var7.a(HeightMap.Type.WORLD_SURFACE, var1 & 15, var2 & 15) > var7.a(HeightMap.Type.OCEAN_FLOOR, var1 & 15, var2 & 15)) {
                System.out.println("Error at AAA");
                return null;
            } else {
                for(int var9 = var8 + 1; var9 >= 0; --var9) {
                    var4.c(var1, var9, var2);
                    IBlockData var10 = this.b.getType(var4);
                    if (!var10.s().e()) {
                        System.out.println("Error at AAA");
                        break;
                    }

                    if (var10.equals(var6)) {
                        System.out.println("Done at AAA");
                        return var4.up().h();
                    }
                }

                System.out.println("Error at AAA");
                return null;
            }
        }
    }

    public float a(long var1, float var3) {
        int var4 = (int)(var1 % 24000L);
        float var5 = ((float)var4 + var3) / 24000.0F - 0.25F;
        if (var5 < 0.0F) {
            ++var5;
        }

        if (var5 > 1.0F) {
            --var5;
        }

        float var6 = var5;
        var5 = 1.0F - (float)((Math.cos((double)var5 * 3.141592653589793D) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5;
    }

    public boolean o() {
        System.out.println("O");
        return true;
    }

    public boolean p(){
        System.out.println("B");
        return true;
    }
}
