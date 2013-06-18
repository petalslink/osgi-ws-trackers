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

import javax.ws.rs.core.Application;
import java.util.LinkedList;
import java.util.List;

/**
 * @author chamerling - chamerling@linagora.com
 */
public abstract class BaseApplication extends Application {

    protected List<ApplicationFragment> resources;

    /**
     *
     */
    public BaseApplication() {
        this.resources = new LinkedList<ApplicationFragment>();
    }

    public void addResource(ApplicationFragment resource) {
        if (resource == null) {
            return;
        }
        this.resources.add(resource);
    }

    public void removeResource(ApplicationFragment resource) {
        resources.remove(resource);
    }

    public boolean hasResources() {
        return !resources.isEmpty();
    }

    /**
     * Get the application path. Path needs to start with a trailing '/'
     *
     * @return
     */
    public abstract String getPath();

    /**
     * Get the application port
     *
     * @return
     */
    public abstract long getPort();
}
