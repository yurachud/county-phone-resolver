package resolver;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.LoggerFactory;
import resolver.country.InvalidPhoneNumber;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InvalidPhoneMapper implements ExceptionMapper<InvalidPhoneNumber> {
    
    public static final String INVALID_PHONE_METRIC = "invalidPhone";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(InvalidPhoneMapper.class);
    private Meter exceptions;
    
    public InvalidPhoneMapper(MetricRegistry metrics) {
        exceptions = metrics.meter(MetricRegistry.name(getClass(), INVALID_PHONE_METRIC));
    }
    
    @Override
    public Response toResponse(InvalidPhoneNumber exception) {
        exceptions.mark();
        log.error("", exception);
        return Response
                .status(Response.Status.BAD_REQUEST)
//                .entity(exception.getMessage())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}