package com.minecraft.minepay.bukkit.util.image;

import com.minecraft.minepay.bukkit.util.map.MapCreator;
import com.minecraft.minepay.util.qrcode.QrcodeGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.image.BufferedImage;

@Builder
@Getter
public class ImageCreator {

    private final String data;

    public ItemStack generate(Player player) {
        BufferedImage bufferedImage = QrcodeGenerator.generateQrcode(getData());

        return MapCreator.getInstance().generateMap(bufferedImage, player);
    }
}
