package com.minecraft.minepay.http.data.gateway;

import lombok.Data;

@Data
public class GatewayData {

    private final int id;

    private final String name;

    private final boolean enabled, bank;

    private final String createdAt, updatedAt, deletedAt;

}
