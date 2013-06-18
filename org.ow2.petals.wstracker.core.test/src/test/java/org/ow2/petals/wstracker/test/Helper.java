package org.ow2.petals.wstracker.test;

import org.ops4j.pax.exam.Option;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemPackage;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;


/**
 * Christophe Hamerling - chamerling@linagora.com
 */
public class Helper {
    /**
     * Jersey HTTP port.
     */
    public static final int port = getEnvVariable("JERSEY_HTTP_PORT", 8080);

    /**
     * Returns an integer value of given system property, or a default value
     * as defined by the other method parameter, if the system property can
     * not be used.
     *
     * @param varName      name of the system variable.
     * @param defaultValue the default value to return if the system variable is missing or can not be parsed as an integer.
     * @return an integer value taken either from the system property or the default value as defined by the defaultValue parameter.
     */
    public static int getEnvVariable(final String varName, int defaultValue) {
        if (null == varName) {
            return defaultValue;
        }
        String varValue = System.getProperty(varName);
        if (null != varValue) {
            try {
                return Integer.parseInt(varValue);
            } catch (NumberFormatException e) {
                // will return default value bellow
            }
        }
        return defaultValue;
    }

    /**
     * Adds a system property for Maven local repository location to the PaxExam OSGi runtime if a "localRepository" property
     * is present in the map of the system properties.
     *
     * @param options list of options to add the local repository property to.
     * @return list of options enhanced by the local repository property if this property is set or the given list if the
     *         previous condition is not met.
     */
    public static List<Option> addPaxExamMavenLocalRepositoryProperty(List<Option> options) {
        final String localRepository = System.getProperty("localRepository");

        if (localRepository != null) {
            options.addAll(expandedList(systemProperty("org.ops4j.pax.url.mvn.localRepository").value(localRepository)));
        }

        return options;
    }

    /**
     * Convert list of OSGi options to an array.
     *
     * @param options list of OSGi options.
     * @return array of OSGi options.
     */
    public static Option[] asArray(final List<Option> options) {
        return options.toArray(new Option[options.size()]);
    }

    /**
     * Create new list of common OSGi integration test options.
     *
     * @return list of common OSGi integration test options.
     */
    public static List<Option> getCommonOsgiOptions() {
        return getCommonOsgiOptions(true);
    }

    /**
     * Create new list of common OSGi integration test options.
     *
     * @param includeJerseyJaxRsLibs indicates whether JaxRs and Jersey bundles should be added into the resulting list of
     * options.
     * @return list of common OSGi integration test options.
     */
    public static List<Option> getCommonOsgiOptions(final boolean includeJerseyJaxRsLibs) {
        final List<Option> options = new LinkedList<Option>(expandedList(
                // systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("FINEST"),
                systemProperty("org.osgi.framework.system.packages.extra").value("javax.annotation"),

                // define maven repositories
                repositories("http://repo1.maven.org/maven2",
                        "http://repository.apache.org/content/groups/snapshots-group",
                        "http://repository.ops4j.org/maven2",
                        "http://svn.apache.org/repos/asf/servicemix/m2-repo",
                        "http://repository.springsource.com/maven/bundles/release",
                        "http://repository.springsource.com/maven/bundles/external",
                        "http://maven.java.net/content/repositories/snapshots"),

                // log
                // mavenBundle("org.ops4j.pax.logging", "pax-logging-api", "1.4"),
                // mavenBundle("org.ops4j.pax.logging", "pax-logging-service", "1.4"),

                // felix config admin
                // mavenBundle("org.apache.felix", "org.apache.felix.configadmin", "1.2.4"),

                // felix preference service
                // mavenBundle("org.apache.felix", "org.apache.felix.prefs","1.0.2"),

                // HTTP SPEC
                // mavenBundle("org.apache.geronimo.specs","geronimo-servlet_2.5_spec","1.1.2"),

                // javax.annotation has to go first!
                //wrappedBundle(mavenBundle().groupId("javax.annotation").artifactId("javax.annotation-api").versionAsInProject()),

                // Google Guava
                mavenBundle().groupId("com.google.guava").artifactId("guava").versionAsInProject(),

                // HK2
                mavenBundle().groupId("org.glassfish.hk2").artifactId("hk2-api").versionAsInProject(),
                mavenBundle().groupId("org.glassfish.hk2").artifactId("osgi-resource-locator").versionAsInProject(),
                mavenBundle().groupId("org.glassfish.hk2").artifactId("hk2-locator").versionAsInProject(),
                mavenBundle().groupId("org.glassfish.hk2").artifactId("hk2-utils").versionAsInProject(),
                mavenBundle().groupId("org.glassfish.hk2.external").artifactId("javax.inject").versionAsInProject(),
                mavenBundle().groupId("org.glassfish.hk2.external").artifactId("asm-all-repackaged").versionAsInProject(),
                mavenBundle().groupId("org.glassfish.hk2.external").artifactId("cglib").versionAsInProject(),

                // Grizzly
                systemPackage("sun.misc"),
                mavenBundle().groupId("org.glassfish.grizzly").artifactId("grizzly-framework").versionAsInProject(),
                mavenBundle().groupId("org.glassfish.grizzly").artifactId("grizzly-rcm").versionAsInProject(),
                mavenBundle().groupId("org.glassfish.grizzly").artifactId("grizzly-http").versionAsInProject(),
                mavenBundle().groupId("org.glassfish.grizzly").artifactId("grizzly-http-server").versionAsInProject(),

                // javax.validation
                mavenBundle().groupId("javax.validation").artifactId("validation-api").versionAsInProject(),

                // Jersey Grizzly
                mavenBundle().groupId("org.glassfish.jersey.containers").artifactId("jersey-container-grizzly2-http")
                        .versionAsInProject()

                // start felix framework
                ));

        if (includeJerseyJaxRsLibs) {
            options.addAll(expandedList(
                    // JAX-RS API
                    mavenBundle().groupId("javax.ws.rs").artifactId("javax.ws.rs-api").versionAsInProject(),

                    // Jersey bundles
                    mavenBundle().groupId("org.glassfish.jersey.core").artifactId("jersey-common").versionAsInProject(),
                    mavenBundle().groupId("org.glassfish.jersey.core").artifactId("jersey-server").versionAsInProject(),
                    mavenBundle().groupId("org.glassfish.jersey.core").artifactId("jersey-client").versionAsInProject()
            ));
        }

        return addPaxExamMavenLocalRepositoryProperty(options);
    }

    /**
     * Create expanded options list from the supplied options.
     *
     * @param options options to be expanded into the option list.
     * @return expanded options list.
     */
    public static List<Option> expandedList(Option... options) {
        return Arrays.asList(options(options));
    }
}
