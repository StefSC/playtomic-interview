package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.dto.Recharge;
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

    /* For some reason my data.sql is not working.
    *  From logs it executes the insert but the database is empty.
    *  So in order to have some registered wallets, we need to call this endpoint to create a new one.
    * */
    @GetMapping("/new")
    Wallet createWallet() {
        return this.walletService.createWallet();
    }

    /*
    * Get wallet by id - usually just 1.
    * */
    @GetMapping("/wallet/{id}")
    Optional<Wallet> getWallet(@PathVariable long id) {
        log.info("Getting wallet with id: {}", id);
        return this.walletService.getWallet(id);
    }

    /*
    * Recharge the wallet. I used postman so i can send the body of this request.
    * ex:
    * {"cardNo": "testCard","amount": 10}
    * */
    @PostMapping("/wallet/{id}/recharge")
    Wallet rechargeWallet(@PathVariable Integer id, @RequestBody Recharge charge) {
        Optional<Wallet> wallet = this.walletService.getWallet(id);
        if (wallet.isPresent()) {
            try {
                return walletService.rechargeWallet(wallet.get(), charge);
            } catch (StripeServiceException e) {
                this.log.error(e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /*
     * Charge the wallet. I used postman so i can send the body of this request.
     * ex:
     * {"amount": 10}
     * */
    @PostMapping("/wallet/{id}/purchase")
    Wallet purchase(@PathVariable Integer id, @RequestBody Purchase purchase) {
        Optional<Wallet> wallet = this.walletService.getWallet(id);
        if (wallet.isPresent()) {
            try {
                return walletService.purchase(wallet.get(), purchase);
            } catch (WalletServiceException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


}
