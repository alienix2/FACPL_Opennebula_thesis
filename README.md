**A simple interface that provides a real world implementation of the FACPL language**

# Usage instructions:

## Resource management:
**Installation:**
- run **mvn install** inside the *resource_management* folder
- edit the file */resource_maangement/config.properties* with your actual hosts IDs (you can read them from OpenNebula UI or from the shell)

**Description:**
- If you just want to implement different commands that allow to dialogate with the openNebula API in a simpler way you can import the resource_management project and use it as is.
- The *entryPoint* package contains classes that allow to use all the functionalities, if all you need is to compile a policy and/or to execute a request in your java code you can take a look at them.
- The *contextStub* and *PEPActions* files are provided inside the *opennebula_context_actions* folder
- The other packages contain an abstraction of the OpenNebula APIs and some interfaces.
- Implementing or extending some of them allows you to easily reuse the concrete implementations with FACPL without having to redo all the work

## Policy manager:
**Installation:**
- run **mvn install** inside the *resource_management* folder
- run **mvn test** inside the *policy_manager* folder (optional, ensures that everything is fine)
- run **mvn spring-boot:run** inside the *policy_manager* folder (keep the term open for the springboot logs)
- use the browser to connect to *localhost:8080* (or *host_ip:8080*)
- - edit the file */policy_manager/config.properties* with your actual hosts IDs (you can read them from OpenNebula UI or from the shell)

**Description:**
- Inside the *policy_manager* folder there is a simple backend and fronted implementation of the funcionalities provided by the *resource_management* project.
- A default PAS is already compiled and implemented. An example of policy set is compiled and implemented, and is the same visible on the UI, an example of a request is also visible from the UI.
- The user can change the set of policies and send requests from the web UI
- **MUST DO:** change: *TYPE_1*, *TYPE_2*, *HYPER_1* and *HYPER_2* accordingly to you OpenNebula setup. 
- The PAS cannot be changed from the web UI.
- If you want to change the PAS you can compile a PAS separately and then move it to the *src/onlinePolicies* folder. Examples of policy sets can be found in the *resource_management* project.
- The *contextStub* and *PEPActions* files are provided inside the *opennebula_context_actions* folder, they are needed to run the requests, do not remove them from the folder.
- If you want to change the *contextStub* and/or *PEPActions* files you can create them and then put them in the *opennebula_context_actions* folder, they will be used in the evaluation process.
- From the file located inside *policy_manager/src/main/resources* you can change where certain files are stored (logs, files to run ecc.)
