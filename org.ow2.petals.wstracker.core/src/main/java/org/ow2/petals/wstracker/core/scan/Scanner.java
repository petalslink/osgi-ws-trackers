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
package org.ow2.petals.wstracker.core.scan;

import org.objectweb.asm.ClassReader;
import org.osgi.framework.Bundle;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Get all the classes from a path which are JAXRS annotated
 *
 * @author chamerling - chamerling@linagora.com
 */
public class Scanner {

    public final static String PATH_ANNO = "Ljavax/ws/rs/Path;";

    public final static String PROVIDER_ANNO = "Ljavax/ws/rs/ext/Provider;";
    private final HashMap<String,Collection<Class<?>>> collectors;

    private Bundle bundle;

    private String packageBase;

    /**
     * Default REST annotations scanner
     *
     * @param bundle
     * @param packageBase
     */
    public Scanner(Bundle bundle, String packageBase) {
        this(bundle, packageBase, PATH_ANNO, PROVIDER_ANNO);
    }

    /**
     *
     * @param bundle
     * @param packageBase
     * @param annotations
     */
    public Scanner(Bundle bundle, String packageBase, String ... annotations) {
        this.bundle = bundle;
        this.packageBase = packageBase == null ? "/" : packageBase;
        this.collectors = new HashMap<String, Collection<Class<?>>>();
        for (String annotation  : annotations) {
            addCollector(annotation);
        }
    }

    public void addCollector(String annotation) {
        collectors.put(annotation, new ArrayList<Class<?>>());
    }

    public void addCollector(String annotation, Collection<Class<?>> collector) {
        collectors.put(annotation, collector);
    }

    /**
     * Scan classes and get the ones where right annotations are set
     *
     * @throws Exception
     */
    public void scan() throws Exception {
        Enumeration<URL> urls = bundle.findEntries(packageBase, "*.class", true);
        if (urls == null) {
            return;
        }
        Set<String> annotations = collectors.keySet();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            InputStream in = url.openStream();
            try {
                ClassReader cr = new ClassReader(in);
                AnnotationFinder reader = new AnnotationFinder(annotations);
                cr.accept(reader, null,
                        ClassReader.SKIP_DEBUG | ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES);
                if (reader.hasResults()) {
                    String cname = reader.getClassName();
                    for (String anno : reader.getResults()) {
                        collectors.get(anno).add(bundle.loadClass(cname));
                    }
                }
            } finally {
                in.close();
            }
        }
    }

    /**
     * Get all the classes where the annotations are available
     *
     * @return
     */
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> result = new HashSet<Class<?>>();
        for (Collection<Class<?>> c : collectors.values()) {
            result.addAll(c);
        }
        return result;
    }

    /**
     * Get all the classes where the given annotation is present
     *
     * @param anno
     * @return
     */
    public Set<Class<?>> getClasses(String anno) {
        return new HashSet<Class<?>>(collectors.get(anno));
    }
}
