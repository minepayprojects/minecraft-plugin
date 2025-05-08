package com.minecraft.minepay.bukkit.task;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.account.Account;
import com.minecraft.minepay.http.HttpHandlerBuilder;
import com.minecraft.minepay.http.data.store.StoreData;
import com.minecraft.minepay.http.data.transaction.TransactionData;
import com.minecraft.minepay.http.params.HttpParams;
import com.minecraft.minepay.http.response.list.TransactionListResponse;
import com.minecraft.minepay.http.type.HttpType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AsyncRefundProductTask implements Runnable {

    @Override
    public void run() {

        for (StoreData store : Core.getStoreController().getStores()) {

            HttpParams httpParams = HttpParams.create()
                    .add("refunded", true)
                    .add("productRefunded", false);

            HttpHandlerBuilder builder = HttpHandlerBuilder
                    .builder()
                    .path("/transactions")
                    .type(HttpType.GET)
                    .tokenFinder("store:" + store.getId())
                    .params(httpParams)
                    .build();

            Core.getHttpHandler().execute(builder, false, request -> {

                TransactionListResponse response = new TransactionListResponse().parse(request);

                if (!response.isSuccess()) {
                    Core.getLogger().error("Ocorreu um erro ao obter as transações pagas a serem entregues");
                    return;
                }

                for (TransactionData transaction : response.getTransactions()) {

                    Player player = Bukkit.getPlayer(UUID.fromString(transaction.getUniqueId()));

                    if (player == null) {
                        continue;
                    }

                    Account account = Account.getAccount(player.getUniqueId());

                    account.getExecutor().sendTransaction(transaction);
                }

            });
        }

    }

}
