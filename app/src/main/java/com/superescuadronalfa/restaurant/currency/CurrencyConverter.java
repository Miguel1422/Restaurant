package com.superescuadronalfa.restaurant.currency;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class CurrencyConverter {
    public static String currencyFormat(BigDecimal n) {
        n = n.setScale(2, BigDecimal.ROUND_HALF_UP);
        return NumberFormat.getCurrencyInstance().format(n);
    }
}
