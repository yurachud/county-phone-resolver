package resolver;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.LoggerFactory;
import resolver.country.CountriesNotFound;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class CountriesNotFoundMapper implements ExceptionMapper<CountriesNotFound> {
    
    public static final String INVALID_PHONE_METRIC = "countriesNotFound";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CountriesNotFoundMapper.class);
    private Meter exceptions;
    
    public CountriesNotFoundMapper(MetricRegistry metrics) {
        exceptions = metrics.meter(MetricRegistry.name(getClass(), INVALID_PHONE_METRIC));
    }
    
    @Override
    public Response toResponse(CountriesNotFound exception) {
        exceptions.mark();
        log.error("", exception);
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}