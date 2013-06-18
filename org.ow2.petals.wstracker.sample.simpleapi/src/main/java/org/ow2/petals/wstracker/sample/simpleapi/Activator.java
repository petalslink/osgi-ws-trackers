package org.ow2.petals.wstracker.sample.simpleapi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author chamerling - chamerling@linagora.com
 */
public class Activator implements BundleActivator {

    ServiceRegistration registration;

    /**
     * Start
     * @param context
     * @throws Exception
     */
    public void start(BundleContext context) throws Exception {
        System.out.println("Starting the bundle and registering services...");

        SimpleApi api = new SimpleApiImpl();
        registration = context.registerService(SimpleApi.class.getName(), api, null);

        PingApi ping = new PingApiImpl();
        registration = context.registerService(PingApi.class.getName(), ping, null);
    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("Stopping the bundle");
        // TODO
    }
}
