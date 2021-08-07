package com.playtomic.tests.wallet.dto;

import javax.persistence.*;

@Entity
@Table(name = "WALLET")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "WALLET_ID")
    private long id;

    @Column(name = "BALANCE")
    private long balance;

    public Wallet() {
        super();
    }

    public Wallet(long balance) {
        super();
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
