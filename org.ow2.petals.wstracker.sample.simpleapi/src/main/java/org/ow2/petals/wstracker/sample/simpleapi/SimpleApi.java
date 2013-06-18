package org.ow2.petals.wstracker.sample.simpleapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author chamerling - chamerling@linagora.com
 */
@Path("/simple")
public interface SimpleApi {

    /**
     *
     * @return
     */
    @GET
    @Path("/hello")
    Response hello();
}
