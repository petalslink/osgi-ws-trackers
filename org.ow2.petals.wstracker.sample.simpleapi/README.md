# Tracker Sample

This OSGI bundle is a sample to be used with the REST tracker. The bundle activator will register REST-annotated classes as OSGI services.
These services will be detected by the tracker as REST services and exposed using Jersey.

By default, once detected, REST resources are available at:

- http://localhost:8181/org/ow2/petals/wstracker/sample/simpleapi/simple/hello : Will return an HTTP OK with 'hello' message.
- http://localhost:8181/org/ow2/petals/wstracker/sample/simpleapi/ping/pong : Will return a HTTP OK with 'pong' message.
