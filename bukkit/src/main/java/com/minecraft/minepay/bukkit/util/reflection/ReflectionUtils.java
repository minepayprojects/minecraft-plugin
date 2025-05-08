package com.minecraft.minepay.bukkit.util.reflection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class ReflectionUtils {

    public static Object toEntityHuman(Player player) {
        try {
            Class<?> craftPlayer = getClassByName(getOBCPackageName() + ".entity.CraftPlayer");
            assert craftPlayer != null;
            Method method = craftPlayer.getDeclaredMethod("getHandle");
            method.setAccessible(true);
            return method.invoke(player);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getClassByName(String name) {
        try {
            return Class.forName(name);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getNMSPackageName() {
        return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static String getOBCPackageName() {
        return "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static String getOBCPackageName(Class<?> _class) {
        return "org.bukkit.craftbukkit." + _class.getName().split("\\.")[3];
    }

}
