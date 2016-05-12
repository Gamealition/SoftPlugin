SoftPlugin is a [Spigot plugin](https://hub.spigotmc.org/javadocs/spigot/). It is a thin loader; at runtime, it compiles Java source files and then loads a specific boot class. Whilst the server is running, the source files can be changed and the plugin reloaded without restarting the server.

# Rationale

This plugin was written so that we could use these benefits of scripting plugins (e.g. Skript, ScriptCraft):

* Rapid development; no need to build and deploy a jar
* Rapid prototyping; new features or ideas can be quickly tried
* Quick changes and fixes; no need to open an IDE to make changes, just a text editor and reload

By compiling Java sources into classes, we could use these benefits:

* Java language; strict-typing, static analysis, clearer code constructs, etc.
* Native use of Spigot API; no need to wait for intermediate plugin to update
* Access to existing libraries and other plugins

# Requirements

* (Paper)Spigot 1.9.2 or later
* Server needs to be running on JDK 8 or later

# Building

*This is for building SoftPlugin itself, not for any custom code you want to load via SoftPlugin*

SoftPlugin uses Maven for dependency management and building. These instructions are simply for building a jar file of SoftPlugin. This is useful for use with CI servers (e.g. Jenkins) or for checking if the code builds in your development environment.

## Command line (Win/Linux)

*Assuming Maven is [installed to or available in PATH](https://maven.apache.org/install.html)*

1. Clone this repository using your git client (e.g. `git clone https://github.com/Gamealition/SoftPlugin.git`)
* Go into repository directory
* Execute `mvn clean package`
* Built jar file will be located in the new `target` directory

## Eclipse (Mars)

*In need of screenshots*

1. Clone this repository using your git client
* In a new or blank Eclipse Workspace, go to `File > Import`
* Under "Maven", select "Existing Maven Projects" and go Next
* Set "Root Directory" to the cloned repository directory, click "Refresh", ensure the
`pom.xml` file is checked and go Finish
* Go to `Run > Run Configurations...`
* Right-click "Maven Build" and click "New", then configure as such:
    * Set Name to "Build SoftPlugin JAR"
    * Set "Base Directory" to the repository's directory
    * Set "Goals" to `clean package` - This will make Maven clean the workspace, and then
    build the jar, on each build.
* Click "Apply", and then "Run"
* Built JAR file will be located in the new `target` directory
* For subsequent builds, go to `Run > Run History > Build SoftPlugin JAR`

## IntelliJ

*In need of screenshots*

1. Clone this repository using your git client
* In IntelliJ, go to `File > Open`
* Navigate to the repository and open the `pom.xml` file
* Look for and open the "Maven Projects" tab, expand "SoftPlugin" and then "Lifecycle"
* Double-click "Clean" and wait for the process to finish.
This will ensure there are no left-over files from previous Maven builds that may
interfere with the final build.
* Double-click "Package" and wait for the process to finish
* Built Jar file will be located in the new `target` directory

# Debugging

*This is for debugging SoftPlugin itself, not for any custom code you want to load via SoftPlugin*

These instructions are for running and debugging SoftPlugin from within your development environment. These will help you debug SoftPlugin and reload certain code changes as it runs. [Each of these steps assumes you have a Bukkit/Spigot/PaperSpigot server locally installed.](http://i.imgur.com/q0B28cR.png)

## Eclipse (Mars)

*In need of screenshots*

1. Build a JAR using the above instructions for Maven in Eclipse
* Copy the jar to the plugins folder of your local server
* [Follow these instructions to set up your local server and Eclipse for remote debugging](https://www.spigotmc.org/wiki/eclipse-debug-your-plugin/)

## IntelliJ

*In need of screenshots*

1. Clone this repository using your git client
* In IntelliJ, go to `File > Open`
* Navigate to the repository and open the `pom.xml` file
* Go to `File > Project Structure... > Artifacts`
* Click `Add > JAR > Empty`, then configure as such:
    * Set Name to "SoftPlugin"
    * Set Output directory to the "plugins" folder of your local server
    * Check "Build on make"
* Right-click "'SoftPlugin' compile output" and then click "Put into Output Root", then click OK
* Go to `Run > Edit Configurations...`
* Click `Add New Configuration > JAR Application`, then configure as such:
    * Set Name to "Server" (or "Spigot" or "PaperSpigot", etc)
    * Set Path to JAR to the full path of your local server's executable JAR
        * e.g. `C:\Users\SoftPluginDev\AppData\Local\Programs\Spigot\spigot-1.9.2.jar`
    * Set VM options to "-Xmx2G -XX:MaxPermSize=128M" (allocates 2GB RAM)
    * Set Working directory to the full path of your local server
        * e.g. `C:\Users\SoftPluginDev\AppData\Local\Programs\Spigot\`
    * Checkmark "Single instance only" on the top right corner
* Under "Before launch", click `Add New Configuration > Build Artifacts`
* Check "SoftPlugin" and then click OK twice

After setting up IntelliJ for debugging, all you need to do is press SHIFT+F9 to begin debugging. This will automatically build a jar, put it in your local server's plugins folder and then start your server automatically.

## Debug logging

SoftPlugin makes use of `FINE`, `FINER` and `FINEST` logging levels for debugging. To enable
these messages, append this line to the server's JVM arguments:

> `-Dlog4j.configurationFile=log4j.xml`

Then in the root directory of the server, create the file `log4j.xml` with these contents:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration monitorInterval="5" packages="com.mojang.util">
  <Appenders>
    <Queue name="TerminalConsole">
      <PatternLayout pattern="[%d{HH:mm:ss} %level]: %msg%n"/>
    </Queue>
  </Appenders>
  <Loggers>
    <Root level="INFO">
      <AppenderRef ref="TerminalConsole"/>
    </Root>
    <Logger additivity="false" level="ALL" name="roycurtis.softplugin.SoftPlugin">
      <AppenderRef ref="TerminalConsole"/>
    </Logger>
  </Loggers>
</Configuration>
```