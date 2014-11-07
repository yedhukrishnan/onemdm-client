One MDM Client
==============

One MDM Client is the android application to run on the client device to communicate with the [One MDM](https://github.com/multunus/one-mdm/) Server.

## How to set up One MDM Client?

### Prerequisites

* Java Development Kit
* Android SDK Tools
* Android 4.3 APIs (API Level 18)

Clone the One MDM Client repo:

``` bash
git clone https://github.com/multunus/one-mdm-client.git
```

Go to the one-mdm-client directory and copy the file `Config.java.template` to `Config.java` in the package:

``` bash
cd one-mdm-client
cp src/main/java/com/multunus/one_mdm_client/Config.java.template src/main/java/com/multunus/one_mdm_client/Config.java
```

Set variable `SERVER` to point to the hosted One MDM Server. Also configure the `BUGSNAG_API_KEY` for error reporting:

``` java
public static final String SERVER = <Server URL>;
public static final String BUGSNAG_API_KEY = <Bugsnag API Key>;
```

Build the project from the command line:

``` bash
./gradlew clean build
```


