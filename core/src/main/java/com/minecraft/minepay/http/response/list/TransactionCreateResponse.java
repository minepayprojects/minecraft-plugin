package com.minecraft.minepay.http.response.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.data.transaction.TransactionCreateData;
import com.minecraft.minepay.http.data.transaction.TransactionData;
import com.minecraft.minepay.http.request.HttpRequest;
import com.minecraft.minepay.http.response.HttpResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TransactionCreateResponse extends HttpResponse {

    private boolean success;

    private String message;

    private TransactionCreateData transactionCreateData;

    @Override
    public TransactionCreateResponse parse(HttpRequest request) {
        Core.getLogger().log("%s: %s", request.isSuccess(), !request.isSuccess() ? request.getErrorMessage() : request.getMessage());

        setSuccess(request.isSuccess());
        setMessage(!request.isSuccess() ? request.getErrorMessage() : request.getMessage());

        if (isSuccess()) {
            JsonObject object = request.getJsonObject().get("data").getAsJsonObject();

            String qrCode = object.get("qr_code").getAsString();

            String qrCodeBase64 = object.get("qr_code_base64").getAsString();

            String ticketUrl = object.get("ticket_url").getAsString();

            setTransactionCreateData(new TransactionCreateData(qrCode, qrCodeBase64, ticketUrl));
        }

        return this;
    }
}
