package com.playtomic.tests.wallet.service;

public class WalletServiceException extends Exception {
    private String message;

    public WalletServiceException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
