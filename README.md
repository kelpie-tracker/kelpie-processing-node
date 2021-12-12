# Kelpie Processing Node
Tracking system for positions of cattle with ContextNet

![Kelpie Diagram](https://github.com/kelpie-tracker/kelpie-processing-node/blob/master/docs/Diagramas-Overview.png?raw=true)

Our system aims to collect data, both from location and from embedded sensors, from mobile entities and store them for reliable analysis. To do this, we use M-Hub, the ContextNet component that serves IoMT applications, to interact with sensors and actuators not just built into smartphones, but built into smart things as well.

## Setup
To execute:

1. Login to the ContextNet Kafka Image Registry
```
make login-docker
```

2. Provision gateway environment:
```
make environment
```

3) Run Processing Node to monitor messages:
```
java -jar MainPN.jar
```