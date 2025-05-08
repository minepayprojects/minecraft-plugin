package com.minecraft.minepay.http.data.store;

import com.minecraft.minepay.http.data.gateway.GatewayData;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StoreData {

    private final int id;

    private final String name;

    private final boolean enabled;

    private final String createdAt, updatedAt, deletedAt;

    private List<GatewayData> gateways;

    public GatewayData getGatewayByName(String name) {
        return getGateways().stream().filter(gatewayData -> gatewayData.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
