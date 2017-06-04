package resolver.country;

import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.ImmutableList;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;
import resolver.CountriesNotFoundMapper;
import resolver.InvalidPhoneMapper;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CountryResourceTest {
    public static final String INVALID_PHONE_NUMBER = "+++";
    private static final Country CANADA = new Country("Canada");
    private static final Country USA = new Country("United States of America");
    private static final CountryInfoGateway countryInfoGateway = mock(CountryInfoGateway.class);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CountryResource(new Countries(countryInfoGateway)))
            .addProvider(new CountriesNotFoundMapper(new MetricRegistry()))
            .addProvider(new InvalidPhoneMapper(new MetricRegistry()))
            .build();
    private static final String VALID_NUMBER = "+1987";
    private static final String NOT_EXISTING_NUMBER = "+00029875111";
    
    @Test
    public void resolveCountry() {
        when(countryInfoGateway.by(new Phone.CountryCode("1"))).thenReturn(ImmutableList.of(CANADA, USA));
        
        Response response = resources.target("/countries").request().post(json(VALID_NUMBER));
        
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(countriesIn(response)).isEqualTo(ImmutableList.of(CANADA, USA));
    }
    
    @Test
    public void resolveCountryFailsWithNotFound() {
        Response response = resources.target("/countries").request().post(json(NOT_EXISTING_NUMBER));
        
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }
    
    @Test
    public void resolveCountryFailsWithBadRequest() {
        Response response = resources.target("/countries").request().post(json(INVALID_PHONE_NUMBER));
        
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.getStatusCode());
    }
    
    private List<Country> countriesIn(Response response) {
        return response.readEntity(countryListType());
    }
    
    private GenericType<List<Country>> countryListType() {
        return new GenericType<List<Country>>() {
        };
    }
    
}