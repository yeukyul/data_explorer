package framework.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import framework.gui.DisplayPlugin;

/**
 * Class that helps find implementation of {@link framework.core.DataPlugin} and
 * {@link framework.gui.DisplayPlugin}. In other for this class to find the
 * plugin, it must be included in the correct file located in
 * resource/META-INF/services.
 * 
 * @author sdk1
 *
 */
public class PluginFinder {
  /**
   * @return the list of data Plugin specified in
   *         resource/META-INF/services/framework.core.DataPlugin.
   */
  public static List<DataPlugin> getDataPlugins() {
    URL[] jars = findPluginJars();
    ClassLoader cl = new URLClassLoader(jars, Thread.currentThread().getContextClassLoader());
    Iterator<DataPlugin> dataPlugins = ServiceLoader.load(DataPlugin.class, cl).iterator();

    List<DataPlugin> dataPluginList = new ArrayList<>();
    dataPlugins.forEachRemaining(dataPluginList::add);

    return dataPluginList;
  }

  /**
   * @return the list of display Plugin specified in
   *         resource/META-INF/services/framework.gui.displayPlugin.
   */
  public static List<DisplayPlugin> getDisplayPlugins() {
    URL[] jars = findPluginJars();
    ClassLoader cl = new URLClassLoader(jars, Thread.currentThread().getContextClassLoader());
    Iterator<DisplayPlugin> displayPlugins = ServiceLoader.load(DisplayPlugin.class, cl).iterator();

    List<DisplayPlugin> displayPluginList = new ArrayList<>();
    displayPlugins.forEachRemaining(displayPluginList::add);

    return displayPluginList;
  }

  /**
   * Code derived from <a href=
   * "https://github.com/CMU-15-214/pluginloader-example">pluginloader-example</a>
   * from 214 Github.
   * 
   * @return list of URL it finds in the specified folder. .
   */
  private static URL[] findPluginJars() {
    File pluginsDir = new File("plugins");
    if (!pluginsDir.exists() || !pluginsDir.isDirectory()) {
      System.err.println("plugins/ directory not found");
      return new URL[0];
    }
    return Arrays.stream(new File("plugins").listFiles()).filter(file -> file.getName().toLowerCase().endsWith(".jar"))
        .map(file -> {
          try {
            return file.toURI().toURL();
          } catch (MalformedURLException e) {
            throw new RuntimeException(e);
          }
        }).toArray(URL[]::new);
  }
}
