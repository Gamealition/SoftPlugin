package roycurtis.softplugin;

import org.bukkit.configuration.Configuration;

import java.io.File;

import static roycurtis.softplugin.SoftPlugin.SOFTLOG;

/**
 * Static container class for plugin's configuration values
 */
class Config
{
    static Configuration config;

    static class Paths {
        static File source;
        static File cache;
    }

    static class Boot {
        static String className;
        static String methodName;
    }

    static void init(SoftPlugin plugin)
    {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        config = plugin.getConfig();

        Paths.source = new File( config.getString("paths.source", "plugins/SoftPlugin/source/") );
        Paths.cache  = new File( config.getString("paths.cache",  "plugins/SoftPlugin/cache/") );

        Boot.className  = config.getString("boot.class",  "softplugin.Main");
        Boot.methodName = config.getString("boot.method", "main");

        SOFTLOG.fine("Config loaded");
    }

    private Config() { }
}