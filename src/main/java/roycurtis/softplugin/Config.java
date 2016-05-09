package roycurtis.softplugin;

import org.bukkit.configuration.Configuration;

/**
 * Static container class for plugin's configuration values
 */
class Config
{
    static Configuration config;

    static void init(SoftPlugin plugin)
    {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        config = plugin.getConfig();
    }

    private Config() { }
}