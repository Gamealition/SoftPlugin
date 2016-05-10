package roycurtis.softplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import static roycurtis.softplugin.SoftPlugin.SOFTLOG;

class Loader
{
    URL[]          cacheUrl;
    URLClassLoader loader;

    Class<?> bootClass;
    Object   bootInstance;

    Loader()
    {
        System.out.println("Loader has begun: " + this);
    }

    public void begin()
    {
        try
        {
            cacheUrl = new URL[] { Config.Paths.cache.toURI().toURL() };
            loader   = new URLClassLoader(cacheUrl, Bukkit.class.getClassLoader());

            bootClass    = loader.loadClass(Config.Boot.className);
            bootInstance = bootClass
                .getDeclaredConstructor(JavaPlugin.class)
                .newInstance(SoftPlugin.INSTANCE);
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
            SOFTLOG.severe("Boot class lacks valid constructor: " + Config.Boot.methodName);
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

        SOFTLOG.fine("Loader finalized; soft plugin objects cleanup attempted " + this);
        super.finalize();
    }


}
