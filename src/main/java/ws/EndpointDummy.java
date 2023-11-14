package ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class EndpointDummy {
    @WebMethod
    public String testEndpoint() {
        System.out.println("Endpoint connected!\n");
        return "this is an endpoint dummy return";
    }
}
