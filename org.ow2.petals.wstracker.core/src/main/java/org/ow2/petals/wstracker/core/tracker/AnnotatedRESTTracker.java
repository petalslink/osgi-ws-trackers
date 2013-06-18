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
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.ow2.petals.wstracker.core.ApplicationManager;
import org.ow2.petals.wstracker.core.Constants;
import org.ow2.petals.wstracker.core.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle services registered in the context which are JAXRS annotated
 *
 * @author chamerling - chamerling@linagora.com
 */
public class AnnotatedRESTTracker extends ServiceTracker {

    private static Logger LOG = LoggerFactory.getLogger(AnnotatedRESTTracker.class);

    // TODO : Filter definition...
    public static final String FILTER = "(objectClass=*)";//(!(" + Constants.PUBLISH_REST_FILTER + "=false)))";

    /**
     *
     * @param context
     */
    public AnnotatedRESTTracker(BundleContext context) throws InvalidSyntaxException {
        super(context, FrameworkUtil.createFilter(FILTER), null);
        LOG.debug("AnnotatedRESTTracker init");
    }

    @Override
    public Object addingService(ServiceReference reference) {
        // don't care about HTTPService...
        if (reference instanceof HttpService) {
            return super.addingService(reference);
        }

        Object result = null;
        Object service = this.context.getService(reference);
        if (Utils.isResource(service)) {
            LOG.info("REST service has been detected in {}", service.getClass());
            result = ApplicationManager.getInstance().addResource(reference);
        } else {
            LOG.debug("Service {} is not a REST service and will not be added", service.getClass());
            result = super.addingService(reference);
        }
        return result;
    }
}
