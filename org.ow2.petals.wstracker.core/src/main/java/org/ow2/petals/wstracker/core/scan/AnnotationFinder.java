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

import org.objectweb.asm.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author chamerling - chamerling@linagora.com
 */
public class AnnotationFinder implements ClassVisitor {
    protected Set<String> annotations;

    protected List<String> results;

    protected String cname;

    public AnnotationFinder(Set<String> annotations) throws IOException {
        results = new ArrayList<String>();
        this.annotations = annotations;
    }

    public String getClassName() {
        return cname.replace('/', '.');
    }

    public String getFileName() {
        return cname;
    }

    public List<String> getResults() {
        return results;
    }

    public boolean hasResults() {
        return !results.isEmpty();
    }

    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        cname = name;
    }

    public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
        if (annotations.contains(arg0)) {
            results.add(arg0);
        }
        return null;
    }

    public void visitAttribute(Attribute arg0) {
    }

    public void visitEnd() {
    }

    public FieldVisitor visitField(int arg0, String arg1, String arg2,
                                   String arg3, Object arg4) {
        return null;
    }

    public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {
    }

    public MethodVisitor visitMethod(int arg0, String arg1, String arg2,
                                     String arg3, String[] arg4) {
        return null;
    }

    public void visitOuterClass(String arg0, String arg1, String arg2) {
    }

    public void visitSource(String arg0, String arg1) {
    }
}
