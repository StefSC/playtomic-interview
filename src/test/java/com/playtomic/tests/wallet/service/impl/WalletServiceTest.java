package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.dto.Purchase;
import com.playtomic.tests.wallet.dto.Recharge;
import com.playtomic.tests.wallet.dto.Wallet;
import com.playtomic.tests.wallet.dto.WalletRepository;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;
import com.playtomic.tests.wallet.service.WalletService;
import com.playtomic.tests.wallet.service.WalletServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

public class WalletServiceTest {
    WalletRepository walletRepository = Mockito.mock(WalletRepository.class);
    StripeService stripeService = Mockito.mock(StripeService.class);
    Wallet wallet = new Wallet(123);
    Optional<Wallet> optionalWallet = Optional.of(new Wallet());
    WalletService walletService = new WalletService(walletRepository, stripeService);

    @Test
    public void testGetWallet() {
        Mockito.when(walletRepository.findById(Mockito.any())).thenReturn(optionalWallet);
        Assertions.assertEquals(optionalWallet, walletService.getWallet(1));
    }

    @Test
    public void testRechargeWallet() throws StripeServiceException {
        Mockito.when(walletRepository.findById(Mockito.any())).thenReturn(optionalWallet);
        Mockito.when(walletRepository.save(Mockito.any())).thenReturn(wallet);
        Recharge recharge = new Recharge();
        recharge.setAmount(BigDecimal.valueOf(555));
        recharge.setCardNo("test card");
        Assertions.assertEquals(123 + 555, walletService.rechargeWallet(wallet, recharge).getBalance());
    }

    @Test
    public void testRechargeWalletNegativeAmount() throws StripeServiceException {
        Mockito.when(walletRepository.findById(Mockito.any())).thenReturn(optionalWallet);
        Mockito.when(walletRepository.save(Mockito.any())).thenReturn(wallet);
        Recharge recharge = new Recharge();
        recharge.setAmount(BigDecimal.valueOf(-555));
        recharge.setCardNo("test card");
        Assertions.assertEquals(123, walletService.rechargeWallet(wallet, recharge).getBalance());
    }

    @Test
    public void testWalletPurchase() throws StripeServiceException, WalletServiceException {
        Mockito.when(walletRepository.findById(Mockito.any())).thenReturn(optionalWallet);
        Mockito.when(walletRepository.save(Mockito.any())).thenReturn(wallet);
        Purchase purchase = new Purchase();
        purchase.setAmount(BigDecimal.valueOf(20));
        Assertions.assertEquals(103, walletService.purchase(wallet, purchase).getBalance());
    }

    @Test
    public void testWalletPurchaseInsufficientFunds() throws StripeServiceException, WalletServiceException {
        Mockito.when(walletRepository.findById(Mockito.any())).thenReturn(optionalWallet);
        Mockito.when(walletRepository.save(Mockito.any())).thenReturn(wallet);
        Purchase purchase = new Purchase();
        purchase.setAmount(BigDecimal.valueOf(133));
        WalletServiceException walletServiceException = Assertions.assertThrows(WalletServiceException.class, () ->{
            walletService.purchase(wallet, purchase);
        });
        Assertions.assertEquals("Insufficient funds! :(", walletServiceException.getMessage());
    }
}
