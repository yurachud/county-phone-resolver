package resolver;

import io.dropwizard.Application;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.glassfish.jersey.logging.LoggingFeature;
import resolver.country.Countries;
import resolver.country.CountryInfoGateway;
import resolver.country.CountryResource;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CountryByPhoneApplication extends Application<CountryByPhoneConfiguration> {
    public static void main(String[] args) throws Exception {
        new CountryByPhoneApplication().run(args);
    }
    
    @Override
    public String getName() {
        return "name-by-phone-resolver";
    }
    
    @Override
    public void initialize(Bootstrap<CountryByPhoneConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<CountryByPhoneConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(CountryByPhoneConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }
    
    @Override
    public void run(CountryByPhoneConfiguration configuration,
                    Environment environment) {
        Client client = client()
                .orElseThrow(() -> new RuntimeException("Cannot create jersey client"));
        JerseyEnvironment jerseyEnvironment = environment.jersey();
        jerseyEnvironment.register(new InvalidPhoneMapper(environment.metrics()));
        jerseyEnvironment.register(new CountriesNotFoundMapper(environment.metrics()));
        jerseyEnvironment.register(new CountryResource(new Countries(new CountryInfoGateway(client))));
        jerseyEnvironment.register(new LoggingFeature(Logger.getLogger(getClass().getName()), Level.INFO, LoggingFeature.Verbosity.PAYLOAD_TEXT, 8192));
    }
    
    
    public Optional<Client> client() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManager(), null);
            return Optional.of(ClientBuilder
                    .newBuilder()
                    .hostnameVerifier((s, session) -> true)
                    .sslContext(sslContext).build());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            return Optional.empty();
        }
        
    }
    
    private TrustManager[] trustManager() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        
                    }
                    
                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        
                    }
                    
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };
    }
}