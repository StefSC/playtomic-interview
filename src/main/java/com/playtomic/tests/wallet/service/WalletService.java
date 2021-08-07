package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.dto.Recharge;
import com.playtomic.tests.wallet.dto.Purchase;
import com.playtomic.tests.wallet.dto.Wallet;
import com.playtomic.tests.wallet.dto.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletService {
    private WalletRepository walletRepository;
    private StripeService stripeService;

    @Autowired
    public WalletService(WalletRepository walletRepository, StripeService stripeService) {
        super();
        this.walletRepository = walletRepository;
        this.stripeService = stripeService;
    }

    public Optional<Wallet> getWallet(long id) {
        return this.walletRepository.findById(id);
    }

    public Wallet createWallet() {
        return this.walletRepository.save(new Wallet(100));
    }

    public Wallet rechargeWallet(Wallet wallet, Recharge recharge) throws StripeServiceException {
        this.stripeService.charge(recharge.getCardNo(), recharge.getAmount());
        if (recharge.getAmount().compareTo(BigDecimal.ZERO) == 1) {
            wallet.setBalance(wallet.getBalance() + recharge.getAmount().longValue());
        }
        return this.walletRepository.save(wallet);
    }

    public Wallet purchase(Wallet wallet, Purchase purchase) throws WalletServiceException {
        if (wallet.getBalance() >= purchase.getAmount().longValue()) {
            wallet.setBalance(wallet.getBalance() - purchase.getAmount().longValue());
            return this.walletRepository.save(wallet);
        } else {
            throw new WalletServiceException("Insufficient funds! :(");
        }
    }
}
