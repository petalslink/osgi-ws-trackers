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

import com.google.common.collect.Sets;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.Set;

/**
 * An application fragment is composed of a single serviceReference. A fragment may be embedded into a large application ie
 * will be available at the same root path as other.
 *
 * @author chamerling - chamerling@linagora.com
 */
public class ApplicationFragment extends Application {

    private static Logger LOG = LoggerFactory.getLogger(ApplicationFragment.class);

    private final ServiceReference serviceReference;

    private final BundleContext context;

    /**
     *
     * @param context
     * @param serviceReference
     */
    public ApplicationFragment(BundleContext context, ServiceReference serviceReference) {
        this.context = context;
        this.serviceReference = serviceReference;
    }

    public ServiceReference getServiceReference() {
        return serviceReference;
    }

    /**
     * Since the fragment is created from a service reference, we return the instance instead of returning of the class.
     * @return
     */
    @Override
    public Set<Object> getSingletons() {
        return Sets.newHashSet(this.context.getService(this.serviceReference));
    }

    @Override
    public Set<Class<?>> getClasses() {
        return Collections.emptySet();
        /*
        Class<?> clazz = this.context.getService(this.serviceReference).getClass();
        Set<Class<?>> result = new HashSet<Class<?>>();
        result.add(clazz);
        return result;
        */
    }
}
