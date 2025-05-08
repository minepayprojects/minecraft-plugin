package com.minecraft.minepay.http.config;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.config.object.HttpConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class HttpConfiguration {

    private final String baseUrl;

    private final String userToken;

    private final boolean multiStore;

    private final int storeId;

    private final List<HttpConfig> configs;

    @Setter
    private String path;

    public static HttpConfiguration load(String path) {
        HttpConfiguration configuration = new HttpConfiguration("https://minepay.com.br/api", "", true,-1, new ArrayList<>());

        configuration.setPath(path);

        File file = new File(configuration.getPath());

        try {
            if (file.exists()) {
                FileReader reader = null;

                try {
                    reader = new FileReader(file);
                    configuration = Core.GSON.fromJson(reader, HttpConfiguration.class);
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

    public void save() {
        File file = new File(getPath());

        try {
            if (file.exists()) {
                file.delete();
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(Core.GSON.toJson(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
