package com.playtomic.tests.wallet.dto;

import java.math.BigDecimal;

public class Charge {
    private String cardNo;
    private BigDecimal amount;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
