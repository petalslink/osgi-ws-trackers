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

import org.osgi.framework.BundleContext;
import org.ow2.petals.wstracker.core.tracker.AnnotatedRESTTracker;
import org.ow2.petals.wstracker.core.tracker.HTTPServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts all the required stuff to detect and expose new REST and SOAP services deployed from other bundles.
 *
 * @author chamerling - chamerling@linagora.com
 */
public class Activator implements org.osgi.framework.BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

    private AnnotatedRESTTracker annotatedRestTracker;

    private HTTPServiceTracker httpServiceTracker;

    private BundleContext context;

    public void start(BundleContext context) throws Exception {
        LOG.info("REST Tracker bundle is starting...");
        this.context = context;

        ApplicationManager.getInstance().start(context);

        // just track the HTTPService in case it is not already available...
        httpServiceTracker = new HTTPServiceTracker(context);
        httpServiceTracker.open();

        annotatedRestTracker = new AnnotatedRESTTracker(context);
        annotatedRestTracker.open();

        // others...
    }

    public void stop(BundleContext context) throws Exception {
        LOG.info("Stopping REST tracker bundle");

        if (annotatedRestTracker != null)
            annotatedRestTracker.close();

        if (httpServiceTracker != null)
            httpServiceTracker.close();

        ApplicationManager.getInstance().stop();
    }
}
