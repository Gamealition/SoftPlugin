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
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import static roycurtis.softplugin.SoftPlugin.SOFTLOG;

/** Handles the loading and execution of compiled classes. Holds sole reference to custom code */
class Loader
{
    private URL[]          cacheUrl;
    private URLClassLoader loader;

    private Class<?> bootClass;
    private Object   bootInstance;

    Loader()
    {
        SOFTLOG.fine("Loader created");
    }

    /** Uses a {@see URLClassLoader} to attempt to load & run the boot class from cache */
    public void load()
    {
        try
        {
            cacheUrl = new URL[] { Config.Dirs.cache.toUri().toURL() };
            loader   = new URLClassLoader( cacheUrl, Bukkit.class.getClassLoader() );

            bootClass    = loader.loadClass(Config.Boot.className);
            bootInstance = bootClass
                .getDeclaredConstructor(JavaPlugin.class)
                .newInstance(SoftPlugin.INSTANCE);

            SOFTLOG.fine("Loaded and created instance of boot class: " + bootInstance);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException("Could not create cache path URL", e);
        }
        catch (ClassNotFoundException e)
        {
            SOFTLOG.severe("Could not find boot class: " + Config.Boot.className);
        }
        catch (NoSuchMethodException e)
        {
            SOFTLOG.severe("Boot class lacks a valid constructor");
        }
        catch (InstantiationException e)
        {
            SOFTLOG.severe("Could not create boot class");
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            SOFTLOG.severe("Boot class' constructor is not set to public");
        }
        catch (InvocationTargetException e)
        {
            SOFTLOG.severe("Exception thrown in boot class' constructor");
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        bootClass    = null;
        bootInstance = null;

        if (loader != null)
            loader.close();

        SOFTLOG.fine("Loader finalized; attempted cleanup of loaded instances");
        super.finalize();
    }


}
