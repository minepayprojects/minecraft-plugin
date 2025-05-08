package com.minecraft.minepay.http.response.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.data.gateway.GatewayData;
import com.minecraft.minepay.http.data.product.ProductData;
import com.minecraft.minepay.http.data.store.StoreData;
import com.minecraft.minepay.http.data.transaction.TransactionData;
import com.minecraft.minepay.http.request.HttpRequest;
import com.minecraft.minepay.http.response.HttpResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TransactionListResponse extends HttpResponse {

    private boolean success;

    private String message;

    private int page, limit, total, totalPage;

    private List<TransactionData> transactions = new ArrayList<>();

    @Override
    public TransactionListResponse parse(HttpRequest request) {
        setSuccess(request.isSuccess());
        setMessage(!request.isSuccess() ? request.getErrorMessage() : request.getMessage());

        if (isSuccess()) {

            JsonObject object = request.getJsonObject();

            setPage(object.get("page").getAsInt());
            setLimit(object.get("limit").getAsInt());
            setTotal(object.get("total").getAsInt());
            setTotalPage(object.get("totalPage").getAsInt());

            JsonArray data = object.get("data").getAsJsonArray();

            for (int i = 0; i < data.size(); i++) {
                object = data.get(i).getAsJsonObject();

                TransactionData transaction = Core.GSON.fromJson(object, TransactionData.class);

                transaction.setStoreData(Core.GSON.fromJson(object.get("store").getAsJsonObject(), StoreData.class));
                transaction.setProductData(Core.GSON.fromJson(object.get("product").getAsJsonObject(), ProductData.class));
                transaction.setGatewayData(Core.GSON.fromJson(object.get("gateway").getAsJsonObject(), GatewayData.class));

                if (transaction.getStatus().equalsIgnoreCase("Aguardando pagamento")) {
                    object = object.get("online").getAsJsonObject()
                            .get("point_of_interaction").getAsJsonObject()
                            .get("transaction_data").getAsJsonObject();

                    transaction.setQrCode(object.get("qr_code").getAsString());
                    transaction.setQrCodeBase64(object.get("qr_code_base64").getAsString());
                    transaction.setTicketUrl(object.get("ticket_url").getAsString());
                }

                getTransactions().add(transaction);
            }

        }

        return this;
    }
}
