package com.playtomic.tests.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.dto.Purchase;
import com.playtomic.tests.wallet.dto.Recharge;
import com.playtomic.tests.wallet.dto.Wallet;
import com.playtomic.tests.wallet.dto.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import javax.servlet.ServletContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class WalletApplicationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    public void emptyTest() {
    }

    @Test
    public void testGetWallet() throws Exception {
        mockMvc.perform(get("/new")).andExpect(status().isOk());
        String response = mockMvc.perform(get("/wallet/1")).andReturn().getResponse().getContentAsString();
        assertThat(response).contains("1");
        assertThat(response).contains("100").contains("balance");
    }

    @Test
    public void testRechangeWallet() throws Exception {
        mockMvc.perform(get("/new")).andExpect(status().isOk());

        Recharge recharge = new Recharge();
        recharge.setCardNo("MR MONOPOLY");
        recharge.setAmount(BigDecimal.valueOf(777));
        mockMvc.perform(post("/wallet/1/recharge").contentType("application/json").content(objectMapper.writeValueAsString(recharge))).andExpect(status().isOk());

        String response = mockMvc.perform(get("/wallet/1")).andReturn().getResponse().getContentAsString();
        assertThat(response).contains("1");
        assertThat(response).contains("877").contains("balance");
    }

    @Test
    public void testWalletPurchase() throws Exception {
        mockMvc.perform(get("/new")).andExpect(status().isOk());
        mockMvc.perform(get("/new")).andExpect(status().isOk());

        Purchase purchase = new Purchase();
        purchase.setAmount(BigDecimal.valueOf(50));
        mockMvc.perform(post("/wallet/2/purchase").contentType("application/json").content(objectMapper.writeValueAsString(purchase))).andExpect(status().isOk());

        String response = mockMvc.perform(get("/wallet/2")).andReturn().getResponse().getContentAsString();
        assertThat(response).contains("2");
        assertThat(response).contains("50").contains("balance");
    }
}
