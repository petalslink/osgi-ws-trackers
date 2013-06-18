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
package org.ow2.petals.wstracker.core.tracker;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.ow2.petals.wstracker.core.ApplicationManager;
import org.ow2.petals.wstracker.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;

/**
 * Ensure that REST services are exposed as soon as the HTTPService becomes available.
 *
 * @author chamerling - chamerling@linagora.com
 */
public class HTTPServiceTracker extends ServiceTracker {

    private static Logger LOG = LoggerFactory.getLogger(HTTPServiceTracker.class);

    private HttpService httpService;

    private BundleContext bc;

    /**
     *
     * @param context
     */
    public HTTPServiceTracker(BundleContext context) {
        super(context, HttpService.class.getName(), null);
        this.bc = context;
    }

    @Override
    public Object addingService(ServiceReference serviceRef) {
        LOG.info("HTTP Service tracker detected HttpService...");

        httpService = (HttpService)super.addingService(serviceRef);
        try {
            ApplicationManager.getInstance().initHttpService(httpService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpService;
    }

    @Override
    public void removedService(ServiceReference ref, Object service) {
        if (httpService == service) {
            httpService = null;
        }
        // TODO : ApplicationManager...
        super.removedService(ref, service);
    }
}
