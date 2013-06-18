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

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.ow2.petals.wstracker.core.ApplicationManager;
import org.ow2.petals.wstracker.core.Constants;
import org.slf4j.LoggerFactory;

/**
 * @author chamerling - chamerling@linagora.com
 */
public class RESTBundleTracker implements BundleTrackerCustomizer {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(RESTBundleTracker.class);

    private final BundleContext context;

    private BundleTracker tracker;

    public RESTBundleTracker(BundleContext context) {
        this.context = context;
    }

    public void start() {
        tracker = new BundleTracker(context, Bundle.ACTIVE | Bundle.STARTING | Bundle.RESOLVED, this);
        tracker.open();
    }

    public void stop() {
        if (tracker != null) {
            tracker.close();
        }
    }

    public Object addingBundle(Bundle bundle, BundleEvent event) {
        LOG.info("Adding bundle " + bundle.getSymbolicName());
        String v = (String)bundle.getHeaders().get(Constants.GINKGO_BUNDLE);
        if (v != null) {
            LOG.info("Bundle {} is a ginkgo module", bundle.getSymbolicName());
            ApplicationManager.getInstance().addResources(bundle);
        } else {
            LOG.info("{} is not a ginkgo bundle and will not be added", bundle.getSymbolicName());
        }
        return null;
    }

    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
    }

    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
    }
}
