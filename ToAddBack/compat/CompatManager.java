package techreborn.compat;

import net.minecraftforge.fml.common.Loader;
import ic2.api.info.IC2Classic;
import techreborn.compat.ee3.EmcValues;
import techreborn.compat.minetweaker.MinetweakerCompat;
import techreborn.compat.recipes.*;
import techreborn.compat.waila.CompatModuleWaila;

import java.util.ArrayList;

public class CompatManager {

    public ArrayList<ICompatModule> compatModules = new ArrayList<ICompatModule>();

    public static CompatManager INSTANCE = new CompatManager();

    public static boolean isIC2Loaded = false;
    public static boolean isIC2ClassicLoaded = false;
    public static boolean isClassicEnet = false;
    public static boolean isGregTechLoaded  = false;

    public CompatManager() {
        isIC2Loaded = Loader.isModLoaded("IC2");
        isIC2ClassicLoaded = IC2Classic.isIc2ClassicLoaded();
        if(isIC2ClassicLoaded){
            isClassicEnet = true;
        }
        if(Loader.isModLoaded("Uncomplication")){
            isClassicEnet = true;
        }
        if(Loader.isModLoaded("gregtech")){
            isGregTechLoaded = true;
        }

        registerCompact(CompatModuleWaila.class, "Waila");
        registerCompact(RecipesIC2.class, "IC2");
        registerCompact(RecipesBuildcraft.class, "BuildCraft|Core", "IC2");
        registerCompact(RecipesThermalExpansion.class, "ThermalExpansion");
        registerCompact(EmcValues.class, "EE3");
        registerCompact(RecipesNatura.class, "Natura");
        registerCompact(RecipesBiomesOPlenty.class, "BiomesOPlenty");
        registerCompact(RecipesThaumcraft.class, "Thaumcraft");
        registerCompact(RecipesForestry.class, "Forestry", isForestry4());
        registerCompact(MinetweakerCompat.class, "MineTweaker3");
    }

    public void registerCompact(Class<?> moduleClass, Object... objs) {
        for (Object obj : objs) {
            if (obj instanceof String) {
                String modid = (String) obj;
                if (modid.startsWith("!")) {
                    if (Loader.isModLoaded(modid.replaceAll("!", ""))) {
                        return;
                    }
                } else {
                    if (!Loader.isModLoaded(modid)) {
                        return;
                    }
                }
            } else if (obj instanceof Boolean) {
                Boolean boo = (Boolean) obj;
                if (boo == false) {
                }
                return;
            }
        }
        try {
            compatModules.add((ICompatModule) moduleClass.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean isForestry4() {
        try {
            Class.forName("forestry.api.arboriculture.EnumWoodType");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}