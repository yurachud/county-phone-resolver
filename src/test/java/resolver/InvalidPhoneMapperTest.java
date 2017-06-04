package resolver;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import resolver.country.InvalidPhoneNumber;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class InvalidPhoneMapperTest {
    MetricRegistry metricRegistry = Mockito.mock(MetricRegistry.class);
    Meter meter = mock(Meter.class);
    
    @Before
    public void setUp() throws Exception {
        when(metricRegistry.meter(MetricRegistry.name(InvalidPhoneMapper.class, InvalidPhoneMapper.INVALID_PHONE_METRIC)))
                .thenReturn(meter);
    }
    
    @Test
    public void shouldKeepMetrics() throws Exception {
        InvalidPhoneMapper invalidPhoneMapper = new InvalidPhoneMapper(metricRegistry);
        
        Response response = invalidPhoneMapper.toResponse(new InvalidPhoneNumber(""));
        
        verify(meter).mark();
        assertThat(response.getStatusInfo(), Is.is(Response.Status.BAD_REQUEST));
        assertThat(response.getMediaType(), Is.is(MediaType.APPLICATION_JSON_TYPE));
    }
}