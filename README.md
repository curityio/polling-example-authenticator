# Polling Example Authenticator Plugin

A custom authenticator plugin for the [Curity Identity Server](https://curity.io/docs/idsvr/latest/) that demonstrates how to implement a polling-based authentication flow.

## Features

- Demonstrates use of a polling mechanism in a custom authenticator

---

## Building and Deploying the Plugin

### Prerequisites

- Java 21
- Maven
- Access to a Curity Identity Server installation
- Environment variable `IDSVR_HOME` pointing to your Curity installation (or enter it when prompted)

### Steps

To build and deploy the plugin to your Curity Identity Server:

```bash
./deploy.sh
```

This script will:

1. Compile the plugin using Maven
2. Prompt for the Curity server's installation path if `IDSVR_HOME` is not already set
3. Copy the plugin JAR and required dependencies into the appropriate plugin directory

Once complete, restart the Curity Identity Server to load the new authenticator.

## Additional Resources

- [Plugin Development Guide](https://curity.io/docs/idsvr/latest/developer-guide/plugins/index.html#plugin-installation)
- [Server-Provided Dependencies](https://curity.io/docs/idsvr/latest/developer-guide/plugins/index.html#server-provided-dependencies-1)
- [Curity.io](https://curity.io/)

