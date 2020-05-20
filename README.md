# Abstract

Project for the course of Intelligenza Artificiale M, at **University of Bologna**, Master in Computer Science Engineering.

The project consists in the realization of an intelligent player for the **Tablut** game (ref: https://en.wikipedia.org/wiki/Tafl_games).

# How to use

First of all, we have to start up the server in the **RunnableJar** directory:
```
java -jar Server.jar
```

After that, we can start the player through the jar:
```
java -jar "NoiRestiamoAChesa(ni).jar" <role> <time> <ip>
<role> "white" or "black" 
<time> time in secods (60 recommended)
<ip> ip of the machine where the server has been started
```

To run the player from the **Virtual Machine**, we have to open a command prompt and execute the following instructions:
```
cd /tablut
./runmyplayer <role> <time> <ip>
<role> "white" or "black" 
<time> time in secods (60 recommended)
<ip> ip of the machine where the server has been started
```

# Authors 
- Simone Bartoli
- Damiano Bolognini
- Giulio Todde
