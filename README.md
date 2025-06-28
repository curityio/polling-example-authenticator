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
git clone git@github.com:curityio/polling-example-authenticator.git
cd polling-example-authenticator
chmod +x ./deploy.sh
./deploy.sh
```

This script will:

1. Compile the plugin using Maven
2. Prompt for the Curity server's installation path if `IDSVR_HOME` is not already set
3. Copy the plugin JAR and required dependencies into the appropriate plugin directory

Once complete, restart the Curity Identity Server to load the new Polling Example authenticator.

## Start Mock API Server
Start the [mock backend API](https://github.com/curityio/mock-backend-api) by following the instructions in the repo.

## Additional Resources

- [Plugin Development Guide](https://curity.io/docs/idsvr/latest/developer-guide/plugins/index.html#plugin-installation)
- [Server-Provided Dependencies](https://curity.io/docs/idsvr/latest/developer-guide/plugins/index.html#server-provided-dependencies-1)
- [Curity.io](https://curity.io/)

> 💡 **Note for Windows users**:  
> This script requires a Unix-like shell environment (e.g., WSL, Git Bash, or Cygwin) to run. It will not work in PowerShell or Command Prompt as-is.
