package com.playtomic.tests.wallet.dto;

import java.math.BigDecimal;

public class Purchase {
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
