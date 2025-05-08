package com.minecraft.minepay.bukkit.config;

import com.minecraft.minepay.Core;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@RequiredArgsConstructor
@Getter
public class BukkitConfiguration {

    private final int fetchStoresTimer;

    public static BukkitConfiguration load(File file) {
        BukkitConfiguration configuration = new BukkitConfiguration(6000);

        try {
            if (file.exists()) {
                FileReader reader = null;

                try {
                    reader = new FileReader(file);
                    configuration = Core.GSON.fromJson(reader, BukkitConfiguration.class);
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }

                return configuration;
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(Core.GSON.toJson(configuration));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return configuration;
    }

}
