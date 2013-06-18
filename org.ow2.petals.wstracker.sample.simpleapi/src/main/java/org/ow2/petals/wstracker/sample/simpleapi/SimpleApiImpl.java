package org.ow2.petals.wstracker.sample.simpleapi;

import javax.ws.rs.core.Response;

/**
 * @author chamerling - chamerling@linagora.com
 */
public class SimpleApiImpl implements SimpleApi {

    public SimpleApiImpl() {
        System.out.println("Inst " + SimpleApiImpl.class.getCanonicalName());
    }

    public Response hello() {
        System.out.println("Hello!!!");
        return Response.ok("hello").build();
    }

}
