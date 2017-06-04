package resolver.country;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import resolver.CountryByPhoneApplication;
import resolver.CountryByPhoneConfiguration;

public class CountryByPhoneApplicationTest {
    private static final String configPath = ResourceHelpers.resourceFilePath("app-config.yml");
    @ClassRule
    public static final DropwizardAppRule<CountryByPhoneConfiguration> RULE =
            new DropwizardAppRule<>(CountryByPhoneApplication.class, configPath);
    
    @Test
    public void startup() {
    }
    
}