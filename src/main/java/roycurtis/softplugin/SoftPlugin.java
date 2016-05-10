package roycurtis.softplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Core class of the SoftPlugin plugin
 */
public class SoftPlugin extends JavaPlugin
{
    static SoftPlugin INSTANCE;
    static Logger     SOFTLOG;

    private Compiler compiler;
    private Loader   loader;

    @Override
    public void onLoad()
    {
        INSTANCE = this;
        SOFTLOG  = getLogger();
    }

    @Override
    public void onEnable()
    {
        Config.init(this);

        // Ensure both directories exist
        if ( !Config.Paths.source.isDirectory() && !Config.Paths.source.mkdirs() )
            throw new RuntimeException("Could not make source directory");
        else
            SOFTLOG.fine("Sources directory exists: " + Config.Paths.source);

        if ( !Config.Paths.cache.isDirectory() && !Config.Paths.cache.mkdirs() )
            throw new RuntimeException("Could not make cache directory");
        else
            SOFTLOG.fine("Cache directory exists: " + Config.Paths.cache);

        compiler = new Compiler();
        compiler.begin();

        loader = new Loader();
        loader.begin();

        SOFTLOG.fine("Plugin fully enabled");
    }

    @Override
    public void onDisable()
    {
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);

        compiler = null;
        loader   = null;
        System.gc();

        SOFTLOG.fine("Plugin fully disabled; all listeners and tasks unregistered");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        onDisable();
        onEnable();

        sender.sendMessage("[SoftPlugin] Reloaded plugin and config.yml");
        return true;
    }
}
