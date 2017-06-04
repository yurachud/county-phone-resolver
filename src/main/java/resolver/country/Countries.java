package resolver.country;

import java.util.List;

public class Countries {
    private CountryInfoGateway countryInfoGateway;
    
    public Countries(CountryInfoGateway countryInfoGateway) {
        this.countryInfoGateway = countryInfoGateway;
    }
    
    public List<Country> by(Phone phone) {
        for (Phone.CountryCode countryCode : phone.countryCodes()) {
            List<Country> countriesByCode = countryInfoGateway.by(countryCode);
            if (!countriesByCode.isEmpty()) {
                return countriesByCode;
            }
        }
        throw new CountriesNotFound();
    }
    
    
}
