package com.minecraft.minepay;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minecraft.minepay.account.controller.AccountController;
import com.minecraft.minepay.category.CategoryController;
import com.minecraft.minepay.http.HttpHandler;
import com.minecraft.minepay.shop.ShopController;
import com.minecraft.minepay.store.StoreController;
import com.minecraft.minepay.util.logger.FormattedLogger;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

@Getter
public class Core {

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    public static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Random RANDOM = new Random();

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###,###,###,###.##");
    public static final DecimalFormat SIMPLE_DECIMAL_FORMAT = build();

    public static final Pattern NICKNAME_PATTERN = Pattern.compile("\\w{3,16}");

    public static final Executor ASYNC = Executors.newCachedThreadPool(new ThreadFactoryBuilder().build());

    public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Getter
    @Setter
    public static FormattedLogger logger;

    @Getter
    @Setter
    public static HttpHandler httpHandler;

    @Getter
    @Setter
    public static StoreController storeController;

    @Getter
    public static ShopController shopController = new ShopController();

    @Getter
    public static CategoryController categoryController = new CategoryController();

    @Getter
    public static final AccountController accountController = new AccountController();

    public static UUID getCrackedUniqueId(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name.toUpperCase()).getBytes(StandardCharsets.UTF_8));
    }

    public static boolean isUniqueId(String uniqueId) {
        try {
            UUID.fromString(uniqueId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String generateKey(int length, boolean specialChars) {
        String PATTERN = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        if (specialChars) {
            PATTERN = PATTERN + "!@#$%¨&*()-_=";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            double index = Math.random() * PATTERN.length();

            builder.append(PATTERN.charAt((int) index));
        }

        return builder.toString();
    }

    protected static DecimalFormat build() {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();

        decimalFormatSymbols.setDecimalSeparator(',');

        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        return decimalFormat;
    }

}
