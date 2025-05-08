package com.minecraft.minepay.account;

import com.minecraft.minepay.http.data.transaction.TransactionCreatedData;
import com.minecraft.minepay.http.data.transaction.TransactionData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public abstract class AccountExecutor {

    private final UUID uniqueId;

    public abstract void sendMessage(String message);

    public abstract void sendTransactionCreated(TransactionCreatedData transaction);

    public abstract void sendTransaction(TransactionData transaction);
}
