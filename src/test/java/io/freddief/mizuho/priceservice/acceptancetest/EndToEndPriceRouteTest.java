package io.freddief.mizuho.priceservice.acceptancetest;

import io.freddief.mizuho.priceservice.domain.price.CurrencyCode;
import io.freddief.mizuho.priceservice.dto.price.BloombergPrice;
import io.freddief.mizuho.priceservice.dto.price.Price;
import org.apache.camel.ProducerTemplate;
import org.assertj.core.util.Lists;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class EndToEndPriceRouteTest extends BaseAcceptanceTest {

    @Autowired
    private ProducerTemplate template;

    @Test
    public void priceRouteEndToEndTest() throws Exception {

        // Open stub Bloomberg socket and post prices
        AsyncHttpClient c = new DefaultAsyncHttpClient();

        WebSocket websocket = c.prepareGet("ws://127.0.0.1:1234/bloombergPrices").execute(
            new WebSocketUpgradeHandler.Builder()
                .addWebSocketListener(new WebSocketListener() {
                    @Override
                    public void onOpen(WebSocket websocket) {
                    }

                    @Override
                    public void onClose(WebSocket webSocket, int i, String s) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onBinaryFrame(byte[] payload, boolean finalFragment, int rsv) {

                    }

                    @Override
                    public void onTextFrame(String payload, boolean finalFragment, int rsv) {

                    }

                    @Override
                    public void onPingFrame(byte[] payload) {

                    }

                    @Override
                    public void onPongFrame(byte[] payload) {

                    }
                }).build()).get();


        BloombergPrice bloombergPrice = new BloombergPrice(
            "BARC",
            BigDecimal.valueOf(1),
            "GBP"
        );

        BloombergPrice bloombergPrice2 = new BloombergPrice(
            "HSBA",
            BigDecimal.valueOf(17),
            "GBP"
        );

        BloombergPrice bloombergPrice3 = new BloombergPrice(
            "BARC",
            BigDecimal.valueOf(19),
            "GBP"
        );


        websocket.sendTextFrame(objectMapper.writeValueAsString(bloombergPrice));
        websocket.sendTextFrame(objectMapper.writeValueAsString(bloombergPrice2));

        //Send new price for BARC stock
        websocket.sendTextFrame(objectMapper.writeValueAsString(bloombergPrice3));

        // Await until the rest endpoint has the correct Prices in the cache
        await()
            .atMost(10, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                List<Price> prices = Lists.newArrayList(
                    given()
                        .contentType("application/json")
                    .when()
                        .get(formatUrl("/prices/vendors/bloombergVendorId"))
                    .then()
                        .statusCode(200)
                        .extract()
                        .as(io.freddief.mizuho.priceservice.dto.price.Price[].class));

                assertThat(prices).hasSize(2);
                assertThat(prices.get(0).getId()).isNotNull();
                assertThat(prices.get(0).getInstrument()).isEqualTo(new io.freddief.mizuho.priceservice.dto.instrument.Stock("barclaysStockId", "BARC"));
                assertThat(prices.get(0).getVendor()).isEqualTo(new io.freddief.mizuho.priceservice.dto.vendor.Vendor("bloombergVendorId", "Bloomberg"));
                assertThat(prices.get(0).getPrice()).isEqualTo(BigDecimal.valueOf(19));
                assertThat(prices.get(0).getCurrencyCode()).isEqualTo(CurrencyCode.GBP);
                assertThat(prices.get(0).getTimestamp()).isNotNull();

                assertThat(prices.get(1).getId()).isNotNull();
                assertThat(prices.get(1).getInstrument()).isEqualTo(new io.freddief.mizuho.priceservice.dto.instrument.Stock("hsbcStockId", "HSBA"));
                assertThat(prices.get(1).getVendor()).isEqualTo(new io.freddief.mizuho.priceservice.dto.vendor.Vendor("bloombergVendorId", "Bloomberg"));
                assertThat(prices.get(1).getPrice()).isEqualTo(BigDecimal.valueOf(17));
                assertThat(prices.get(1).getCurrencyCode()).isEqualTo(CurrencyCode.GBP);
                assertThat(prices.get(1).getTimestamp()).isNotNull();

            });

    }

}