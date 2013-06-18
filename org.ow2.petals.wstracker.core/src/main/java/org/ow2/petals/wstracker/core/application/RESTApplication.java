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
package org.ow2.petals.wstracker.core.application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A complete REST application. All the required information to handle REST stuff is defined here.
 *
 * @author chamerling - chamerling@linagora.com
 */
public class RESTApplication {

    private static Logger LOG = LoggerFactory.getLogger(RESTApplication.class);

    private final Bundle bundle;

    private final BaseApplication application;

    private final ServletContainer container;

    private boolean started;

    /**
     *
     * @param application
     */
    public RESTApplication(BaseApplication application, Bundle bundle) {
        this(application, new ServletContainer(ResourceConfig.forApplication(application)), bundle);
    }

    /**
     *
     * @param application
     * @param container
     * @param bundle
     */
    public RESTApplication(BaseApplication application, ServletContainer container, Bundle bundle) {
        super();
        this.application = application;
        this.container = container;
        this.bundle = bundle;
        this.started = false;
    }

    /**
     * Add a resource if not null. Does nothing else...
     *
     * @param resource
     */
    public void addResource(ApplicationFragment resource) {
        if (resource == null) {
            return;
        }
        getApplication().addResource(resource);
    }

    /**
     *
     * @param service
     */
    public void startOrReloadApplication(HttpService service) {
        LOG.debug("Starting RESTApplication from bundle {}", this.bundle.getSymbolicName());
        if (service == null) {
            LOG.warn("Can not start the RESTApplication, HttpService is null");
            return;
        }

        if (!started) {
            LOG.info("Starting the RESTApplication for the first time...");
            try {
                service.registerServlet(this.getPath(), this.getContainer(), null, null);
            } catch (Exception e) {
                LOG.error("Can not install application : {}", e.getMessage());
                e.printStackTrace();
            }
            started = true;
        }  else {
            reload();
        }
    }

    /**
     * Reload the application.
     * This can be called when the list of resources in the base application is modified.
     */
    public void reload() {
        LOG.info("Reloading application from bundle {}", this.bundle.getSymbolicName());
        if (this.getContainer() == null) {
            LOG.warn("Can not reload for a null container");
            return;
        }
        this.container.reload(ResourceConfig.forApplication(getApplication()));
    }

    public BaseApplication getApplication() {
        return application;
    }

    public ServletContainer getContainer() {
        return container;
    }

    public String getPath() {
        return application.getPath();
    }

    public Bundle getBundle() {
        return bundle;
    }
}
