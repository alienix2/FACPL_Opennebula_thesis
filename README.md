**A simple interface that provides a real world implementation of the FACPL language**

# Usage instructions:**

## Resource management:**
**Installation:**
- run mvn install inside the *resource_management* folder
**Description:**
If you just want to implement different commands that allow to dialogate with the openNebula API in a simpler way you can import the resource_management project and use it as is.
The *entryPoint* package contains classes that allow to use all the functionalities, if all you need is to compile a policy and/or to execute a request in your java code you can take a look at them.
The contextStub and PEPActions file are provided inside the *opennebula_context_actions* folder
The other packages contain an abstraction of the OpenNebula APIs and some interfaces.
Implementing or extending some of them allows you to easily reuse the concrete implementations with FACPL without having to redo all the work

## Policy manager:
**Installation:**
- run mvn install inside the *resource_management* folder
- run mvn install inside the *policy_manager* folder
- use the browser to connect to *localhost:8080* (or *host_ip:8080*)
**Description:**
Inside the *policy_manager* folder there is a simple backend and fronted implementation of the funcionalities provided by the *resource_management* project.
A default PAS and a set of default policies are preloaded.
The PAS cannot be changed from the web UI.
The user can change the set of policies and send requests from the web UI
From the file located inside *policy_manager/src/main/resources* you can change where certain files are stored (logs, files to run ecc.)
