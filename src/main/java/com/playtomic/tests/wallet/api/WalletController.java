package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.dto.Charge;
import com.playtomic.tests.wallet.dto.Purchase;
import com.playtomic.tests.wallet.dto.Wallet;
import com.playtomic.tests.wallet.service.WalletService;
import com.playtomic.tests.wallet.service.StripeServiceException;
import com.playtomic.tests.wallet.service.WalletServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);
    @Autowired
    private WalletService walletService;

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }

    @GetMapping("/new")
    Wallet createWallet() {
        return this.walletService.createWallet();
    }

    @GetMapping("/wallet/{id}")
    Optional<Wallet> getWallet(@PathVariable long id) {
        log.info("Getting wallet with id: {}", id);
        return this.walletService.getWallet(id);
    }

    @PostMapping("/wallet/{id}/recharge")
    Optional<Wallet> rechargeWallet(@PathVariable Integer id, @RequestBody Charge charge) {
        Optional<Wallet> wallet = this.walletService.getWallet(id);
        if (wallet.isPresent()) {
            try {
                walletService.rechargeWallet(wallet.get(), charge);
            } catch (StripeServiceException e) {
                this.log.error(e.getMessage());
                e.printStackTrace();
                return Optional.empty();
            }
        }
        this.log.info("-----------------------Recharged successfully!-----------------------");
        return this.walletService.getWallet(id);
    }

    @PostMapping("/wallet/{id}/purchase")
    Optional<Wallet> purchase(@PathVariable Integer id, @RequestBody Purchase purchase) {
        Optional<Wallet> wallet = this.walletService.getWallet(id);
        if (wallet.isPresent()) {
            try {
                walletService.purchase(wallet.get(), purchase);
            } catch (WalletServiceException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }
        this.log.info("-----------------------Purchased!-----------------------");
        return this.walletService.getWallet(id);
    }


}
