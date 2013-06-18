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
import org.osgi.framework.Bundle;
import org.ow2.petals.wstracker.core.Utils;

import java.util.Set;

/**
 * Bundle-based app
 *
 * @author chamerling - chamerling@linagora.com
 */
public class BundleApplication extends BaseApplication {

    private Bundle bundle;

    public BundleApplication(Bundle bundle) {
        super();
        this.bundle = bundle;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return super.getClasses();
    }

    /**
     * Returns all singletons which are in application fragments.
     *
     * @return
     */
    @Override
    public Set<Object> getSingletons() {
        Set<Object> result = Sets.newHashSet();
        for(ApplicationFragment fragment : this.resources) {
            result.addAll(fragment.getSingletons());
        }
        return result;
    }

    @Override
    public String getPath() {
        return Utils.getPath(this.bundle);
    }

    @Override
    public long getPort() {
        return -1;
    }
}
