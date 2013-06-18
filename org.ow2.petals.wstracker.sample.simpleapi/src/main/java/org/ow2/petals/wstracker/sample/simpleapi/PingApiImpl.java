package org.ow2.petals.wstracker.sample.simpleapi;

import javax.ws.rs.core.Response;

/**
 * @author chamerling - chamerling@linagora.com
 */
public class PingApiImpl implements PingApi {

    public PingApiImpl() {
        System.out.println("Inst " + PingApiImpl.class.getCanonicalName());
    }

    public Response pong() {
        System.out.println("Pong!!!");
        return Response.ok("pong").build();
    }
}
