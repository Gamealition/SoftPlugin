package roycurtis.softplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Core class of the SoftPlugin plugin
 */
public class SoftPlugin extends JavaPlugin
{
    static Logger LOGGER;

    @Override
    public void onLoad()
    {
        LOGGER = getLogger();
    }

    @Override
    public void onEnable()
    {
        Config.init(this);

        LOGGER.fine("Plugin fully enabled");
    }

    @Override
    public void onDisable()
    {
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);

        LOGGER.fine("Plugin fully disabled; all listeners and tasks unregistered");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if ( args.length < 1 || !args[0].equalsIgnoreCase("reload") )
            return false;

        onDisable();
        onEnable();

        sender.sendMessage("[SoftPlugin] Reloaded plugin and config.yml");
        return true;
    }
}
