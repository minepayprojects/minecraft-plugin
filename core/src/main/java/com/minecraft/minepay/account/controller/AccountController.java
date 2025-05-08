package com.minecraft.minepay.account.controller;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.account.Account;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountController {

    private final Map<UUID, Account> accounts = new HashMap<>();

    public void register(UUID uniqueId, Account account) {
        this.accounts.put(uniqueId, account);
    }

    public void unregister(UUID uniqueId) {
        this.accounts.remove(uniqueId);
    }

    public Account getAccount(UUID uniqueId) {
        return accounts.get(uniqueId);
    }

    public Collection<Account> getAccounts() {
        return accounts.values();
    }

}
