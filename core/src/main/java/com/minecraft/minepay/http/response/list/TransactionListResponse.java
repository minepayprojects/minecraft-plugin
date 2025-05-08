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

            setObject(object);

            setPage(getInt("page"));
            setLimit(getInt("limit"));
            setTotal(getInt("total"));
            setTotalPage(getInt("totalPage"));

            JsonArray data = getJsonArray("data");

            for (int i = 0; i < data.size(); i++) {
                setObject(data.get(i).getAsJsonObject());

                int id = getInt("id");
                String uniqueId = getString("uniqueId");
                String username = getString("username");
                String transactionId = getString("transactionId");
                boolean delivered = getBoolean("delivered");
                boolean paid = getBoolean("paid");
                boolean refunded = getBoolean("refunded");
                boolean productRefunded = getBoolean("productRefunded");
                String status = getString("status");
                String statusDetail = getString("statusDetail");
                String expireAt = getString("expireAt");
                String createdAt = getString("createdAt");
                String updatedAt = getString("updatedAt");
                String deletedAt = getString("deletedAt");

                TransactionData transaction = new TransactionData(id, uniqueId, username, transactionId, delivered, paid, refunded, productRefunded, status, statusDetail, expireAt, createdAt, updatedAt, deletedAt);

                if (!isJsonNull("details")) {
                    transaction.setDetails(getJsonObject("details"));
                }

                transaction.setStoreData(Core.GSON.fromJson(getJsonObject("store"), StoreData.class));
                transaction.setProductData(Core.GSON.fromJson(getJsonObject("product"), ProductData.class));
                transaction.setGatewayData(Core.GSON.fromJson(getJsonObject("gateway"), GatewayData.class));

                if (transaction.getStatus().equalsIgnoreCase("Aguardando pagamento") && !isJsonNull("online")) {
                    setObject(getJsonObject("online"));
                    setObject(getJsonObject("point_of_interaction"));
                    setObject(getJsonObject("transaction_data"));

                    transaction.setQrCode(getString("qr_code"));
                    transaction.setQrCodeBase64(getString("qr_code_base64"));
                    transaction.setTicketUrl(getString("ticket_url"));
                }

                getTransactions().add(transaction);
            }

        }

        return this;
    }
}
