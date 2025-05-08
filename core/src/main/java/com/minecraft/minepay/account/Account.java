package com.minecraft.minepay.account;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.HttpHandlerBuilder;
import com.minecraft.minepay.http.data.transaction.TransactionData;
import com.minecraft.minepay.http.request.HttpRequest;
import com.minecraft.minepay.http.response.list.TransactionCreateResponse;
import com.minecraft.minepay.http.response.list.TransactionListResponse;
import com.minecraft.minepay.http.type.HttpType;
import com.minecraft.minepay.transaction.TransactionHandler;
import com.minecraft.minepay.util.color.ColorUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Data
public class Account {

    private final UUID uniqueId;

    private final String username;

    private final AccountExecutor executor;

    private List<TransactionData> transactions = new ArrayList<>();

    public void loadTransactions(int storeId, Consumer<Account> consumer) {
        getTransactions().clear();

        HttpHandlerBuilder builder = HttpHandlerBuilder
                .builder()
                .path("/transactions/" + getUniqueId().toString())
                .type(HttpType.GET)
                .tokenFinder("store:" + storeId)
                .build();

        Core.getHttpHandler().execute(builder, request -> {
            TransactionListResponse response = new TransactionListResponse().parse(request);

            if (response.isSuccess()) {
                setTransactions(response.getTransactions());

                response.getTransactions().forEach(executor::sendTransaction);

                consumer.accept(this);
            }
        });
    }

    public void createTransaction(int storeId, int categoryId, int shopId, int productId, int gatewayId, String address) {
        TransactionHandler.getInstance().createTransaction(getUsername(), storeId, categoryId, shopId, productId, gatewayId, getUniqueId().toString(), address, this::parseRequest);
    }

    public void createTransaction(int storeId, int categoryId, int shopId, int productId, int gatewayId, String address, String coupon) {
        TransactionHandler.getInstance().createTransaction(getUsername(), storeId, categoryId, shopId, productId, gatewayId, getUniqueId().toString(), address, coupon, this::parseRequest);
    }

    public void parseRequest(HttpRequest request) {
        TransactionCreateResponse response = new TransactionCreateResponse().parse(request);

        if (!response.isSuccess()) {
            getExecutor().sendMessage(ColorUtil.RED + "Ocorreu um erro ao criar sua transação. Contate a administração para verificar o ocorrido");
            Core.getLogger().error("Ocorreu um erro ao gerar a transação para o usuário '%s': %s", getUsername(), response.getMessage());
            return;
        }

        Core.getLogger().log("Transação criada com sucesso para o usuário '%s'(%s)", getUsername(), getUniqueId().toString());

        getExecutor().sendMessage(ColorUtil.GREEN + "Yay! A transação foi criada com sucesso.");
        getExecutor().sendTransactionCreated(response.getTransactionCreateData());
    }

    public static Account getAccount(UUID uniqueId) {
        return Core.getAccountController().getAccount(uniqueId);
    }

}
