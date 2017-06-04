package resolver.country;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

public class CountryInfoGateway {
    private static final Logger log = LoggerFactory.getLogger(CountryInfoGateway.class);
    private Client client;
    
    public CountryInfoGateway(Client client) {
        this.client = client;
    }
    
    public List<Country> by(Phone.CountryCode countryCode) {
        try {
            return client
                    .target("https://restcountries.eu/rest/v2/callingcode")
                    .path(countryCode.value)
                    .queryParam("fields", "name")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(countryListType());
        } catch (Exception e) {
            log.warn("County not found by code {}", countryCode.value);
            return Collections.emptyList();
        }
    }
    
    private GenericType<List<Country>> countryListType() {
        return new GenericType<List<Country>>() {
        };
    }
}
