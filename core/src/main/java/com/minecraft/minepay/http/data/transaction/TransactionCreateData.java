package com.minecraft.minepay.http.data.transaction;

import lombok.Data;

@Data
public class TransactionCreateData {

    private final String qrCode, qrCodeBase64, ticketUrl;

}
