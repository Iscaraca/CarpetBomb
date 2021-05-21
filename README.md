# Minecraft Carpet Bomb Plugin

A Minecraft plugin that adds a command to carpet bomb your least favourite village :)

## Table of Contents

- [About](#about)
- [Usage](#usage)
- [Prerequisites](#prereqs)
- [Installation (Default)](#default)
- [Installation (Customised)](#custom)

## About <a name = "about"></a>

It's a plugin that spawns a carpet of primed TNT spreading from your location in the direction you're looking at. What more could you ask for?

P.S. This was done in the middle of the night so the code is a bit unoptimised. My sincerest apologies.

## Usage <a name = "usage"></a>

After installation (follow the steps after this section), simply type

```/carpetbomb <player name> <number of cycles>```

to unleash hellfire.

<iframe src="https://player.vimeo.com/video/553471765?badge=0&amp;autopause=0&amp;player_id=0&amp;app_id=58479" width="640" height="360" frameborder="0" allow="autoplay; fullscreen; picture-in-picture" allowfullscreen title="showcase"></iframe>

## Prerequisites <a name = "prereqs"></a>

- [Paper 1.16.5](https://papermc.io/api/v2/projects/paper/versions/1.16.5/builds/711/downloads/paper-1.16.5-711.jar)
- [VS Code](https://code.visualstudio.com/downloads)
- [VS Code Java Extension Pack](https://code.visualstudio.com/docs/java/java-project)
- [OpenJDK](https://openjdk.java.net/install/)
- [Apache Maven](https://maven.apache.org/download.cgi)

## Installation (Default) <a name = "default"></a>

Initialise your server by navigating to the directory with your Paper `.jar` file and run

```java -Xms2G -Xmx2G -jar paper-<insert-version-here>.jar --nogui```

in the terminal.

In the same directory, you should see a folder named `plugins`. Drag `carpetbomb-1.0.jar` into the folder and reinitialise your server. After entering your machine's IP address in Minecraft's multiplayer option, you should be good to go!

## Installation (Customised) <a name = "custom"></a>

After editing the values in `src/main/java/carpetbomb/App.java`, remove `carpetbomb-1.0.jar` from the root directory and run

```mvn package```

in the terminal to build and compile the code. Follow the steps in [Installation (Default)](#default), replacing the default `carpetbomb-1.0.jar` with the newly compiled version.
  
