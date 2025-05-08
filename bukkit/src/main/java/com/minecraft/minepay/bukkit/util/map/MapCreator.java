package com.minecraft.minepay.bukkit.util.map;

import com.minecraft.minepay.bukkit.util.item.ItemBuilder;
import com.minecraft.minepay.util.color.ColorUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class MapCreator {

    @Getter
    public static MapCreator instance = new MapCreator();

    @Getter
    public static String mapName = ColorUtil.GREEN + "QRCode Pagamento";

    public ItemStack generateMap(BufferedImage image, Player player) {
        ItemStack itemStack = new ItemStack(Material.MAP);

        MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();

        mapMeta.setDisplayName(mapName);

        MapView mapView = Bukkit.createMap(player.getWorld());
        mapView.setScale(MapView.Scale.CLOSEST);
        mapView.setUnlimitedTracking(true);
        mapView.getRenderers().clear();

        mapView.addRenderer(new MapRenderer() {
            @Override
            public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
                int mapWidth = 128, mapHeight = 128;

                BufferedImage resizedImage = new BufferedImage(mapWidth, mapHeight, 2);

                resizedImage.getGraphics().drawImage(image, 0, 0, mapWidth, mapHeight, null);

                canvas.drawImage(0, 0, resizedImage);
            }
        });

        mapMeta.setMapView(mapView);
        itemStack.setItemMeta(mapMeta);

        return itemStack;
    }

}
