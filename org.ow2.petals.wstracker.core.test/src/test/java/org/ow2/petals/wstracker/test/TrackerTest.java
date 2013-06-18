package org.ow2.petals.wstracker.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;

import java.util.List;

import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * Christophe Hamerling - chamerling@linagora.com
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class TrackerTest {

    private final static String BUNDLE_ID = "rest-test-mock";

    @Inject
    BundleContext ctx;

    @Configuration
    public Option[] config() {

        List<Option> options = Helper.getCommonOsgiOptions(true);

        options.addAll(Helper.expandedList(
                mavenBundle().groupId("org.glassfish.jersey.bundles").artifactId("jaxrs-ri").versionAsInProject(),

                mavenBundle().groupId("org.mortbay.jetty").artifactId("servlet-api-2.5").versionAsInProject(),
                mavenBundle().groupId("org.glassfish.grizzly").artifactId("grizzly-http-servlet").versionAsInProject(),
                mavenBundle("org.ow2.petals", "org.ow2.petals.wstracker.core")
                ));
        return Helper.asArray(options);

        /*
        return options(
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("FINE"),
                //mavenBundle("com.sun.jersey", "jersey-server").versionAsInProject(),
                //mavenBundle("com.sun.jersey", "jersey-core").versionAsInProject(),
                //mavenBundle("com.sun.jersey", "jersey-servlet").versionAsInProject(),
                mavenBundle("org.apache.geronimo.specs", "geronimo-servlet_2.5_spec").versionAsInProject(),
                mavenBundle("org.glassfish.jersey.containers", "jersey-container-servlet-core").versionAsInProject(),
                mavenBundle("com.google.guava", "guava", "14.0.1"),
                mavenBundle("javax.ws.rs", "javax.ws.rs-api").versionAsInProject(),
                mavenBundle("org.glassfish.hk2", "hk2-api").versionAsInProject(),
                mavenBundle("org.ow2.petals", "org.ow2.petals.wstracker.core"),
                junitBundles()
        );
        */


    }

    @Test
    public void testSuccessScenario() throws Exception {
        for (Bundle b : this.ctx.getBundles()) {
            System.out.println(b.getBundleId());
            System.out.println(b.getSymbolicName());
            System.out.println(b.getState());

        }

        Assert.assertTrue(true);
    }
}
