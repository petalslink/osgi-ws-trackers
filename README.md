# Web Service Trackers

OSGI-based bundles to be used to detect Web services in bundles and expose them automatically.

Service Trackers will detect registered services which are annotated with JAX-RS or JAX-WS annotations and expose them as REST/SOAP Web Services using standard Web service stacks.

## Get them all

```
git clone https://github.com/petalslink/osgi-ws-trackers.git
mvn install
```

The easiest way to use trackers is to use Apache Karaf (http://karaf.apache.org) and features as explained below.

## Howto

### The standard OSGI Way

Create a class, annotate it with JAX-RS, and register it as service in the OSGI context.

```
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/ping")
public interface PingApi {

    /**
     *
     * @return
     */
    @GET
    @Path("/pong")
    Response pong();
}
```

```
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    ServiceRegistration registration;

    /**
     * Start
     * @param context
     * @throws Exception
     */
    public void start(BundleContext context) throws Exception {
        PingApi ping = new PingApiImpl();
        registration = context.registerService(PingApi.class.getName(), ping, null);
    }

    public void stop(BundleContext context) throws Exception {
    }
}
```

If the service trackers are installed and active, they will detect the newly registered PingApi service and expose it as REST service using the OSGI HttpService and Jersey.

### The iPOJO Way

*TODO*

## Configuration

Services/Resources will be exposed using some basic naming conventions:

- if the OSGI bundle header contains a Ginkgo-RestPath value, it will be used as path
- else the OSGI bundle symbolic name will be used (. will be replaced by /) ie foo.bar.baz will produce /foo/bar/baz path.

## Apache Karaf

The trackers are available as Apache Karaf features:

```
features:addUrl mvn:org.ow2.petals.trackers/features/1.0.0-SNAPSHOT/xml/features
features:install rest-tracker
```

## TODOs

- Configure root path
- Service unregistration
- Bundle Tracker: This can be a huge task to handle dependency injection ie we can detect annotated classes, etc...
- SOAP support (easy to add, but not so useful...)

