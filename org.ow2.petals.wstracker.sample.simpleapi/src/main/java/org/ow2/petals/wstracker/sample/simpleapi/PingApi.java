package org.ow2.petals.wstracker.sample.simpleapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * @author chamerling - chamerling@linagora.com
 */
@Path("/ping")
public interface PingApi {

    /**
     *
     * @return
     */
    @GET
    @Path("/pong")
    Response pong();
}
