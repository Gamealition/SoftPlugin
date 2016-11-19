This project is abandoned, in favor of continuing with Bensku's Skript. It is not known
if this project is a viable alternative. Try at your own risk.

----

SoftPlugin is a [Spigot plugin][0]. It is a thin loader; at runtime, it compiles Java
source files and then loads a specific boot class. Whilst the server is running, the
source files can be changed and the plugin reloaded without restarting the server.

# Rationale

This plugin tries to have these benefits of scripting plugins (e.g. Skript, ScriptCraft):

* Rapid development; no need to build and deploy a jar
* Rapid prototyping; new features or ideas can be quickly tried
* Quick changes and fixes; IDE not needed to make changes, just a text editor and reload

By compiling Java sources into classes, it could use these benefits:

* Java language; strict-typing, static analysis, clearer code constructs, etc.
* Native use of Spigot API; no need to wait for intermediate plugin to update
* Access to existing libraries and other plugins

# Requirements

* (Paper)Spigot 1.9.2 or later
* Server needs to be running on JDK 8 or later

# Usage

## Warning

This plugin compiles, loads and executes Java source files it is given. These are executed
with zero security protection or considerations. They are executed in the same context
and privilege level as the server itself. Potentially, any SoftPlugin "scripts" can
destroy the server or the system it is running on.

Use Java source files with SoftPlugin **at your own risk**. Don't give SoftPlugin files
you are unfamiliar with.

## Setup

1. Upload a built JAR of SoftPlugin to the `plugins` directory of your server
1. Restart server once
1. Check `plugins/SoftPlugin/config.yml` and change settings to taste
1. In the created `source` directory, create or place Java source files in typical layout.
e.g, a file for class `softplugin.Main` should be in `source/softplugin/Main.java`
1. Execute `/softplugin-reload` in the server

## Commands

* `/softplugin-reload` (or `/spr`) - does the following:
  * Reloads SoftPlugin's configuration options
  * Unloads any currently loaded classes
  * Discard any compiled classes in the cache
  * Compiles the source files
  * Loads and executes the main class defined in the config

## Permissions

* `softplugin.reload` - Allows use of `/softplugin-reload`. Granted to op and console.

# Building, debugging and debug logging

For instructions and screenshots on how to. . .

* Compile this plugin from scratch
* Build a JAR of this plugin
* Debug this plugin on a server
* Enable debug logging levels such as `FINE` and `FINER`

. . .[please follow the linked guide on this Google document.](https://docs.google.com/document/d/1TTDXG7IZ9M0D2-rzbILAWg1CKjynHK8fNGxbf3W4wBk/view)

[0]: https://hub.spigotmc.org/javadocs/spigot/
