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

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.ow2.petals.wstracker.core.Constants;
import org.ow2.petals.wstracker.core.Utils;
import org.ow2.petals.wstracker.core.scan.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Custom Application which get all the JAXRS annotated classes from the bundle and exposes them as REST services.
 *
 * TODO : This might be huge to manage since we do not handle any injection...
 *
 * @author chamerling - chamerling@linagora.com
 */
public class ClassBundleApplication extends BaseApplication {

    private static final Logger LOG = LoggerFactory.getLogger(ClassBundleApplication.class);

    private final Bundle bundle;

    /**
     *
     * @param bundle
     */
    public ClassBundleApplication(BundleContext context, Bundle bundle) {
        super();
        this.bundle = bundle;
    }

    @Override
    public Set<Class<?>> getClasses() {
        try {
            // TODO Cache
            Scanner scanner = new Scanner(getBundle(), getPackageBase());
            scanner.scan();
            return scanner.getClasses();
        } catch (Exception e) {
            throw new RuntimeException("Failed to scan classes from bundle", e);
        }
    }

    /**
     * TODO : Get the package from the bundle exported packages. This is useful to avoid scanning all classes...
     *
     * @return
     */
    private String getPackageBase() {
        String result = "/";
        String base = (String)getBundle().getHeaders().get(Constants.GINKGO_PACKAGE_BASE_SCAN);
        if (base != null) {
            result = base;
        }
        return result;
    }

    private Bundle getBundle() {
        return this.bundle;
    }

    @Override
    public String getPath() {
        return Utils.getPath(getBundle());
    }

    @Override
    public long getPort() {
        return 8080;
    }
}
