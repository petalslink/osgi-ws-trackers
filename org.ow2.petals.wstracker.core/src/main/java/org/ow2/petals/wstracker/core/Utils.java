package org.ow2.petals.wstracker.core;

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
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

/**
 * @author chamerling - chamerling@linagora.com
 */
public class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    /**
     * Starts a bundle which contains the given class
     *
     */
    public static final void startBundleWithClass(Class<?> classFromBundle) {
        Bundle bundle = FrameworkUtil.getBundle(classFromBundle);
        if (bundle != null && bundle.getState() != Bundle.ACTIVE) {
            LOG.info("Starting the REST bundle from the WS Tracker...");
            try {
                bundle.start();
            } catch (BundleException e) {
                LOG.error("Can not start the bundle...", e);
            }
        } else {
            LOG.info("Bundle not found or already started");
        }
    }

    /**
     * Check is the given instance is a REST service ie got valid annotations
     *
     * @param o
     * @return
     */
    public static boolean isResource(Object o) {
        return o != null && hasRegisterableAnnotation(o);
    }

    /**
     *
     * @param service
     * @return
     */
    public static boolean hasRegisterableAnnotation(Object service) {
        boolean result = isRegisterableAnnotationPresent(service.getClass());
        if(!result) {
            // check interfaces...
            Class<?>[] interfaces = service.getClass().getInterfaces();
            for(Class<?> type : interfaces) {
                result = result || isRegisterableAnnotationPresent(type);
            }
        }
        return result;
    }

    /**
     *
     * @param type
     * @return
     */
    public static boolean isRegisterableAnnotationPresent(Class<?> type) {
        return type.isAnnotationPresent(Path.class) || type.isAnnotationPresent(Provider.class);
    }

    /**
     * Get the path for a given bundle.
     * Path will be used by the REST stack to expose the bundle classes as REST services.
     *
     * @param bundle
     * @return
     */
    public static final String getPath(Bundle bundle) {
        String result = "/";

        if (bundle == null) {
            return result;
        }

        String path = (String)bundle.getHeaders().get(Constants.GINKGO_REST_PATH);
        if (path == null || path.trim().length() == 0) {
            // create path from the bundle symbolic name
            result += bundle.getSymbolicName().toLowerCase();
            result = result.replaceAll("\\.", "/");
        } else {
            if (!path.startsWith("/")) {
                result += path;
            } else {
                result = path;
            }
        }
        return result;
    }
}
