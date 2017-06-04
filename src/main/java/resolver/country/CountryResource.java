package resolver.country;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/countries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/countries", description = "Operations about countries")
public class CountryResource {
    private final Countries countries;
    
    public CountryResource(Countries countries) {
        this.countries = countries;
    }
    
    @POST
    @Timed
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid phone format. Length[4:17] allowed characters ['(' ')' '.' ' ' '_' '-']"),
            @ApiResponse(code = 404, message = "Countries not found by code")
    })
    public List<Country> resolveCountry(
            @ApiParam(value = "Example value: 37129875712", required = true) String phoneNumber) {
        return countries.by(new Phone(phoneNumber));
    }
    
}