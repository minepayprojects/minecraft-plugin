package com.minecraft.minepay.util;

import com.minecraft.minepay.util.date.DateUtils;

import java.lang.management.ManagementFactory;
import java.util.UUID;

public class Util {

    public static long getTotalMemory(Runtime runtime) {
        return runtime.totalMemory() / 1048576L;
    }

    public static long getUsedMemory(Runtime runtime) {
        return (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
    }

    public static long getMaxMemory(Runtime runtime) {
        return runtime.maxMemory() / 1048576L;
    }

    public static String getUptime() {
        long time = ManagementFactory.getRuntimeMXBean().getUptime() / 1000L;
        return DateUtils.getDifferenceFormat(time);
    }

    public static String formatMillis(long millis) {
        return (System.currentTimeMillis() - millis) + "ms";
    }

    public static String formatUniqueId(UUID uniqueId) {
        return formatUniqueId(uniqueId, false);
    }

    public static String formatUniqueId(UUID uniqueId, boolean replace) {
        return replace ? uniqueId.toString().replace("-", "") : uniqueId.toString();
    }

}
