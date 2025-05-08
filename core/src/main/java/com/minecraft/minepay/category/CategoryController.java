package com.minecraft.minepay.category;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.HttpHandlerBuilder;
import com.minecraft.minepay.http.data.category.CategoryData;
import com.minecraft.minepay.http.params.HttpParams;
import com.minecraft.minepay.http.response.list.CategoryListResponse;
import com.minecraft.minepay.http.type.HttpType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class CategoryController {

    private HashMap<Integer, List<CategoryData>> categories = new HashMap<>();

    public void loadCategories(int storeId, int shopId) {
        HttpHandlerBuilder builder = HttpHandlerBuilder.builder()
                .type(HttpType.GET)
                .path("/categories")
                .minepay(true)
                .params(HttpParams.create().add("shopId", shopId))
                .tokenFinder("store:" + storeId)
                .build();

        Core.getHttpHandler().execute(builder, request -> {

            CategoryListResponse response = new CategoryListResponse().parse(request);

            if (!response.isSuccess()) {
                Core.getLogger().log("Ocorreu um erro ao buscar as categorias cadastradas. Mais informações: %s", response.getMessage());
                return;
            }

            getCategories().getOrDefault(storeId, new ArrayList<>()).clear();
            getCategories().put(storeId, response.getCategories());

            Core.getLogger().log("Categorias obtidas com sucesso para o shop #%d e a loja #%d. Total: %d", shopId, storeId, getCategories().size());
        });
    }

    public List<CategoryData> getCategories(int storeId, int shopId) {
        return getCategories().get(storeId).stream().filter(category -> category.getShopData().getId() == shopId).toList();
    }

    public CategoryData getCategory(int categoryId) {
        try {
            for (List<CategoryData> list : getCategories().values()) {
                for (CategoryData category : list) {
                    if (category.getId() == categoryId) {
                        return category;
                    }
                }
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }


    public CategoryData getCategory(String name) {
        try {
            for (List<CategoryData> list : getCategories().values()) {
                for (CategoryData category : list) {
                    if (category.getName().equalsIgnoreCase(name)) {
                        return category;
                    }
                }
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
