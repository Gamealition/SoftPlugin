/**
 * Copyright © 2016 Roy Adrian Curtis
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the “Software”), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package roycurtis.softplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Core singleton class of the SoftPlugin plugin. Initialization of important classes and plugin
 * code begins here.
 */
public class SoftPlugin extends JavaPlugin
{
    /** Singleton instance of SoftPlugin, as created and run by Bukkit */
    static SoftPlugin INSTANCE;
    /** Singleton instance of SoftPlugin's logger, provided by Bukkit */
    static Logger     SOFTLOG;

    private boolean     running;
    private Diagnostics diagnostics;
    private Compiler    compiler;
    private Loader      loader;

    @Override
    public void onLoad()
    {
        INSTANCE = this;
        SOFTLOG  = getLogger();
    }

    @Override
    public void onEnable()
    {
        try
        {
            Config.init();

            diagnostics = new Diagnostics();
            compiler    = new Compiler(diagnostics);
            compiler.compile();

            loader = new Loader();
            loader.load();

            running = true;
            SOFTLOG.info("Enabled; all code compiled and loaded");
        }
        catch (CompilerException e)
        {
            SOFTLOG.severe("*** One or more source files failed to compile. Please check the" +
                           " diagnostic output above to identify any errors.");
            SOFTLOG.severe("*** SoftPlugin will go idle and not load any code. If possible, try" +
                           " fixing the build errors and then retry using `/softplugin-reload`");

            onDisable();
        }
        catch (RuntimeException e)
        {
            SOFTLOG.severe("*** Plugin could not start because:");
            SOFTLOG.severe("* " + e);
            SOFTLOG.severe("* Inner exception: " + e.getCause() );
            SOFTLOG.severe("* Reported by: " + e.getStackTrace()[0].getClassName() );
            SOFTLOG.severe("*** SoftPlugin will go idle and not load any code. If possible, try" +
                           " fixing any config issues and then do `/softplugin-reload`");

            onDisable();
        }
    }

    @Override
    public void onDisable()
    {
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);

        running     = false;
        diagnostics = null;
        compiler    = null;
        loader      = null;

        // Force a garbage collect to try finalize and clean-up custom loaded classes
        System.gc();

        SOFTLOG.fine("Disabled; all listeners and tasks unregistered");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        onDisable();
        onEnable();

        sender.sendMessage(running
            ? "§7*** Reloaded SoftPlugin config & code"
            : "§c*** Could not reload SoftPlugin; see console for errors"
        );

        return true;
    }
}