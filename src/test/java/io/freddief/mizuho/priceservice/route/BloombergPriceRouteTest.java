package io.freddief.mizuho.priceservice.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.freddief.mizuho.priceservice.domain.price.CurrencyCode;
import io.freddief.mizuho.priceservice.dto.bloomberg.BloombergPrice;
import io.freddief.mizuho.priceservice.dto.instrument.Stock;
import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.dto.vendor.Vendor;
import io.freddief.mizuho.priceservice.service.BloombergPriceService;
import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@MockEndpoints(PriceRoute.PRICES_TOPIC)
public class BloombergPriceRouteTest {

    @Autowired
    private ObjectMapper objectMapper;
    @EndpointInject(uri = "mock:activemq:topic:prices")
    private MockEndpoint mockEndpoint;

    @Test
    public void bloombergPriceRoute() throws Exception {

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

        mockEndpoint.expectedMessageCount(1);

        websocket.sendTextFrame(objectMapper.writeValueAsString(bloombergPrice));

        mockEndpoint.assertIsSatisfied();

        Price price = objectMapper.readValue(mockEndpoint.getExchanges().get(0).getMessage().getBody(String.class), Price.class);

        assertThat(price.getId()).isNotNull();
        assertThat(price.getTimestamp()).isNotNull();
        assertThat(price.getCurrencyCode()).isEqualTo(CurrencyCode.GBP);
        assertThat(price.getPrice()).isEqualTo(BigDecimal.valueOf(1));
        assertThat(price.getVendor()).isEqualTo(new Vendor(BloombergPriceService.BLOOMBERG_VENDOR_ID, "Bloomberg"));
        assertThat(price.getInstrument()).isEqualTo(new Stock("barclaysStockId", "BARC"));

    }

}