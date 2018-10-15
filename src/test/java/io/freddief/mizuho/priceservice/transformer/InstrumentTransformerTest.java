package io.freddief.mizuho.priceservice.transformer;

import io.freddief.mizuho.priceservice.domain.instrument.InstrumentType;
import io.freddief.mizuho.priceservice.dto.instrument.Bond;
import io.freddief.mizuho.priceservice.dto.instrument.Derivative;
import io.freddief.mizuho.priceservice.dto.instrument.Instrument;
import io.freddief.mizuho.priceservice.dto.instrument.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InstrumentTransformerTest {

    @InjectMocks
    private InstrumentTransformer instrumentTransformer;

    @Test
    public void toDomain_transformsStock() {

        Stock dto = new Stock(
            "id",
            "name"
        );

        io.freddief.mizuho.priceservice.domain.instrument.Instrument domain = instrumentTransformer.toDomain(dto);

        assertThat(domain).isInstanceOf(io.freddief.mizuho.priceservice.domain.instrument.Stock.class);
        assertThat(domain.getId()).isEqualTo("id");
        assertThat(domain.getCode()).isEqualTo("name");
    }

    @Test
    public void toDomain_transformsBond() {

        Bond dto = new Bond(
            "id",
            "name"
        );

        io.freddief.mizuho.priceservice.domain.instrument.Instrument domain = instrumentTransformer.toDomain(dto);

        assertThat(domain).isInstanceOf(io.freddief.mizuho.priceservice.domain.instrument.Bond.class);
        assertThat(domain.getId()).isEqualTo("id");
        assertThat(domain.getCode()).isEqualTo("name");
    }

    @Test
    public void toDomain_transformsDerivative() {

        Derivative dto = new Derivative(
            "id",
            "name"
        );

        io.freddief.mizuho.priceservice.domain.instrument.Instrument domain = instrumentTransformer.toDomain(dto);

        assertThat(domain).isInstanceOf(io.freddief.mizuho.priceservice.domain.instrument.Derivative.class);
        assertThat(domain.getId()).isEqualTo("id");
        assertThat(domain.getCode()).isEqualTo("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toDomain_whenUnmappedType_thenThrowException() {

        class Unmapped implements Instrument {
            @Override
            public String getId() {
                return null;
            }

            @Override
            public String getCode() {
                return null;
            }
        };

        Unmapped unmapped = new Unmapped();

        instrumentTransformer.toDomain(unmapped);
    }

    @Test
    public void toDto_transformsStock() {

        io.freddief.mizuho.priceservice.domain.instrument.Stock domain = new io.freddief.mizuho.priceservice.domain.instrument.Stock(
            "id",
            "name"
        );

        Instrument dto = instrumentTransformer.toDto(domain);

        assertThat(dto).isInstanceOf(Stock.class);
        assertThat(dto.getId()).isEqualTo("id");
        assertThat(dto.getCode()).isEqualTo("name");
    }

    @Test
    public void toDto_transformsBond() {

        io.freddief.mizuho.priceservice.domain.instrument.Bond domain = new io.freddief.mizuho.priceservice.domain.instrument.Bond(
            "id",
            "name"
        );

        Instrument dto = instrumentTransformer.toDto(domain);

        assertThat(dto).isInstanceOf(Bond.class);
        assertThat(dto.getId()).isEqualTo("id");
        assertThat(dto.getCode()).isEqualTo("name");
    }

    @Test
    public void toDto_transformsDerivative() {

        io.freddief.mizuho.priceservice.domain.instrument.Derivative domain = new io.freddief.mizuho.priceservice.domain.instrument.Derivative(
            "id",
            "name"
        );

        Instrument dto = instrumentTransformer.toDto(domain);

        assertThat(dto).isInstanceOf(Derivative.class);
        assertThat(dto.getId()).isEqualTo("id");
        assertThat(dto.getCode()).isEqualTo("name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toDto_whenUnmappedType_thenThrowException() {

        class Unmapped implements io.freddief.mizuho.priceservice.domain.instrument.Instrument {

            @Override
            public String getId() {
                return null;
            }

            @Override
            public String getCode() {
                return null;
            }
            @Override
            public InstrumentType getType() {
                return null;
            }
        };

        Unmapped unmapped = new Unmapped();

        instrumentTransformer.toDto(unmapped);

    }

}