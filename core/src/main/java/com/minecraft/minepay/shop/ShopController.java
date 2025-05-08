package com.minecraft.minepay.shop;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.HttpHandlerBuilder;
import com.minecraft.minepay.http.data.shop.ShopData;
import com.minecraft.minepay.http.response.list.ShopListResponse;
import com.minecraft.minepay.http.type.HttpType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ShopController {

    private List<ShopData> shops = new ArrayList<>();

    public void loadShops(int storeId) {
        HttpHandlerBuilder builder = HttpHandlerBuilder.builder()
                .type(HttpType.GET)
                .path("/shops")
                .minepay(true)
                .tokenFinder("store:" + storeId)
                .build();

        Core.getLogger().log("Obtendo informações da loja #%d", storeId);

        Core.getHttpHandler().execute(builder, request -> {

            ShopListResponse response = new ShopListResponse().parse(request);

            if (!response.isSuccess()) {
                Core.getLogger().log("Ocorreu um erro ao buscar os shops cadastrados. Mais informações: %s", response.getMessage());
                return;
            }

            getShops().clear();
            setShops(response.getShops());

            Core.getLogger().log("Shops obtidos com sucesso para a Loja #%d. Total: %d", storeId, response.getTotal());

            Core.getLogger().log("Obtendo as categorias para os shops...");

            for (ShopData shop : getShops()) {
                Core.getCategoryController().loadCategories(storeId, shop.getId());
            }
        });
    }

    public List<ShopData> getShopsByStoreId(int storeId) {
        return new ArrayList<>(getShops().stream().filter(shop -> shop.isEnabled() && shop.getStoreData().getId() == storeId).toList());
    }

    public ShopData getShopByName(String name) {
        return getShops().stream().filter(shop -> shop.isEnabled() && shop.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
