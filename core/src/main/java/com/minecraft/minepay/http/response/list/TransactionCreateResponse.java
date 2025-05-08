package com.minecraft.minepay.http.response.list;

import com.google.gson.JsonObject;
import com.minecraft.minepay.http.data.transaction.TransactionCreatedData;
import com.minecraft.minepay.http.request.HttpRequest;
import com.minecraft.minepay.http.response.HttpResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionCreateResponse extends HttpResponse {

    private boolean success;

    private String message;

    private TransactionCreatedData transactionCreateData;

    @Override
    public TransactionCreateResponse parse(HttpRequest request) {
        setSuccess(request.isSuccess());
        setMessage(!request.isSuccess() ? request.getErrorMessage() : request.getMessage());

        if (isSuccess()) {
            JsonObject object = request.getJsonObject().get("data").getAsJsonObject();

            String qrCode = object.get("qr_code").getAsString();

            String qrCodeBase64 = object.get("qr_code_base64").getAsString();

            String ticketUrl = object.get("ticket_url").getAsString();

            setTransactionCreateData(new TransactionCreatedData(qrCode, qrCodeBase64, ticketUrl));
        }

        return this;
    }
}
