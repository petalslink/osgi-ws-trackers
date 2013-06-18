/****************************************************************************
 *
 * Copyright (c) 2013, Linagora
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *****************************************************************************/
package org.ow2.petals.wstracker.core;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.ow2.petals.wstracker.core.application.ApplicationFragment;
import org.ow2.petals.wstracker.core.application.BundleApplication;
import org.ow2.petals.wstracker.core.application.ClassBundleApplication;
import org.ow2.petals.wstracker.core.application.RESTApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * REST applications manager singleton.
 *
 * @author chamerling - chamerling@linagora.com
 */
public class ApplicationManager {

    private static Logger LOG = LoggerFactory.getLogger(ApplicationManager.class);

    private static ApplicationManager instance = new ApplicationManager();

    public static ApplicationManager getInstance() {
        return instance;
    }

    /**
     * Key is the bundle from which the application is created
     */
    private Map<Bundle, RESTApplication> applications;

    /**
     * HTTPService is injected by the HTTPService Tracker when available
     */
    private HttpService service;

    protected BundleContext context;

    /**
     *
     */
    private ApplicationManager() {
        this.applications = new HashMap<Bundle, RESTApplication>();
    }

    /**
     * Start to track bundles for REST annotated classes
     *
     * @param context
     */
    public synchronized void start(BundleContext context) {
        this.context = context;
    }

    /**
     *
     */
    public synchronized void stop() {
    }

    /**
     * Add a single resource from a service reference
     *
     * @param reference to a REST-enabled service
     * @return
     */
    public Object addResource(ServiceReference reference) {
        LOG.debug("Adding resource from reference {}", reference);
        if (reference == null) {
            return null;
        }

        Bundle bundle = reference.getBundle();
        ApplicationFragment fragment = new ApplicationFragment(this.context, reference);
        RESTApplication app = getApplication(bundle);
        if (app == null) {
            app = new RESTApplication(new BundleApplication(bundle), bundle);
            applications.put(bundle, app);
        }
        app.addResource(fragment);
        startOrReloadApplication(app);
        return app;
    }

    /**
     * @param bundle
     * @return
     */
    private RESTApplication getApplication(Bundle bundle) {
        return applications.get(bundle);
    }

    /**
     * Add all resources from the bundle
     *
     * @param bundle
     */
    public void addResources(Bundle bundle) {
        ClassBundleApplication application = new ClassBundleApplication(this.context, bundle);
        RESTApplication app = new RESTApplication(application, new ServletContainer(ResourceConfig.forApplication(application)), bundle);
        applications.put(bundle, app);
        startOrReloadApplication(app);
    }

    /**
     *
     * @param service
     * @throws Exception
     */
    public synchronized void initHttpService(HttpService service) throws Exception {
        if (this.service == null) {
            this.service = service;
            // install applications if there are already some...
            installApplications();
        }
    }

    /**
     * Install the applications which are pending
     */
    protected void installApplications() {
        if (service != null) {
            Iterator<Bundle> iter = applications.keySet().iterator();
            while(iter.hasNext()) {
                startOrReloadApplication(applications.get(iter.next()));
            }
        }
    }

    /**
     * Does not work if application is a composite application ie path has already been used by another fragment, we need to reload all!!!
     * @param app
     */
    protected void startOrReloadApplication(RESTApplication app) {
        LOG.info("Starting application from bundle {} at {}", app.getBundle().getSymbolicName(), app.getPath());
        if (service != null) {
            app.startOrReloadApplication(service);
        } else {
            LOG.warn("HTTPService has not been found, start is delayed");
        }
    }
}
