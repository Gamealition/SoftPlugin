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

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static roycurtis.softplugin.SoftPlugin.INSTANCE;
import static roycurtis.softplugin.SoftPlugin.SOFTLOG;

/** Static container class for SoftPlugin's configuration values */
class Config
{
    static class Dirs
    {
        /** Directory where source files are to be found */
        static Path source;
        /** Directory where compiled class files are to be stored */
        static Path cache;
    }

    static class Boot
    {
        /** Fully qualified name of class to attempt to load  */
        static String className;
    }

    private static Configuration config;

    /** Loads config from disk into this class' fields. Creates config file if not found. */
    static void init()
    {
        INSTANCE.saveDefaultConfig();
        INSTANCE.reloadConfig();

        config = INSTANCE.getConfig();

        Dirs.source = getOrCreatePath("paths.source", "plugins/SoftPlugin/source/");
        Dirs.cache  = getOrCreatePath("paths.cache",  "plugins/SoftPlugin/cache/");

        Boot.className = config.getString("boot.class",  "softplugin.Main");
        Validate.notEmpty(Boot.className, "`boot.class` is set to empty value");

        SOFTLOG.fine("Config loaded");
    }

    /**
     * Attempts to get a Path object for a configured path. Creates the path if it doesn't exist.
     * Throws exceptions if path is invalid or cannot be created.
     *
     * @param key      YAML config key for path
     * @param defValue Default value if config not set
     * @return Path reference of existing directory
     */
    private static Path getOrCreatePath(String key, String defValue)
    {
        try
        {
            String value = config.getString(key, defValue);
            Path   path  = Paths.get(value);

            Files.createDirectories(path);
            SOFTLOG.fine("Path for `" + key + "` is valid: " + path);

            return path;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not get or create path", e);
        }
    }

    private Config() { }
}