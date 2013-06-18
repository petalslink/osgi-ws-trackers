Features to be used with Apache Karaf.

First install the feature from Maven dependency

    features:addUrl mvn:org.ow2.petals.trackers/features/1.0.0-SNAPSHOT/xml/features

Then install feature

    features:install XXX

You can also refresh feature URL in case you updated somthing

    features:refreshurl
