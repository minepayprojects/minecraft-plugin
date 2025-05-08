package com.minecraft.minepay.store;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.HttpHandlerBuilder;
import com.minecraft.minepay.http.data.store.StoreData;
import com.minecraft.minepay.http.response.list.StoreListResponse;
import com.minecraft.minepay.http.type.HttpType;
import com.minecraft.minepay.store.executor.StoreExecutor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class StoreController {

    private final StoreExecutor executor;

    private List<StoreData> stores = new ArrayList<>();

    public void loadStores() {
        HttpHandlerBuilder builder = HttpHandlerBuilder.builder()
                .type(HttpType.GET)
                .path("/stores")
                .minepay(true)
                .token(Core.getHttpHandler().getConfiguration().getUserToken())
                .build();

        Core.getHttpHandler().execute(builder, request -> {

            StoreListResponse response = new StoreListResponse().parse(request);

            if (!response.isSuccess()) {
                Core.getLogger().log("Ocorreu um erro ao obter as lojas cadastradas para o usuário. Mais informações: %s", response.getMessage());
                return;
            }

            getStores().clear();
            setStores(response.getStores());

            Core.getLogger().log("Lojas obtidas com sucesso. Total: %d", response.getStores().size());

            Core.getLogger().log("Obtendo os shops para as lojas criadas...");

            for (StoreData store : getStores()) {
                Core.getShopController().loadShops(store.getId());
            }
        });
    }

    public StoreData getStoreByName(String name) {
        return getStores().stream().filter(store -> store.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public StoreData getStoreById(int id) {
        return getStores().stream().filter(store -> store.getId() == id).findFirst().orElse(null);
    }

}
