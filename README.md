# GPUP

**G.P.U.P - Generic Platform For Utilizing Processes**

The platform enables to model set of dependencies between components and handle them efficiently.

**Insights:** The platform enables extract various insights out of the ‘graph’ of components: routes, circles, transitive dependencies etc.

**Execution:** The platform enables running various tasks on the graphs nodes. It follows the Open-Close principle and can be extended with various tasks in the future. Tasks can be, for example: compilation task; testing task. Execution is done in parallel to maximize efficiency of processing, Execution can be done on part of the graph, or only on the failed node from former execution etc.

**Distribution:** the platform architecture enables distributed (remote) workers to connect to it and execute work on their private resources.
