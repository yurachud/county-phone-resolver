package resolver.country;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CountriesTest {
    public static final ImmutableList<Country> LV = ImmutableList.of(new Country("LV"));
    public static final Phone.CountryCode LV_CODE = new Phone.CountryCode("371");
    private final CountryInfoGateway countryInfoGateway = mock(CountryInfoGateway.class);
    Countries countries = new Countries(countryInfoGateway);
    
    @Test
    public void shouldFindByPhoneCode() throws Exception {
        when(countryInfoGateway.by(LV_CODE)).thenReturn(LV);
        
        countries.by(new Phone("+371123"));
        
        verify(countryInfoGateway, times(2)).by(any());
    }
    
    @Test(expected = CountriesNotFound.class)
    public void shouldThrowCountriesNotFound() throws Exception {
        countries.by(new Phone("+371123"));
    }
}