package dst.ass3.aop.impl;

import dst.ass3.aop.IPluginExecutable;
import dst.ass3.aop.IPluginExecutor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * Created by amra.
 */
public class PluginExecutor implements IPluginExecutor {

    private PluginFileMonitoring pluginFileMonitoring;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public PluginExecutor() throws IOException {
        pluginFileMonitoring = new PluginFileMonitoring(this);
    }

    @Override
    public void monitor(File dir) {
        pluginFileMonitoring.monitor(dir);
    }

    @Override
    public void stopMonitoring(File dir) {
        pluginFileMonitoring.stopMonitoring(dir);
    }

    @Override
    public void start() {
        pluginFileMonitoring.start();
    }

    @Override
    public void stop() {
        pluginFileMonitoring.interrupt();
    }

    public void runPlugin(Path path) {

        if (!path.toString().toUpperCase().endsWith(".JAR")) return;
        try {

            //http://stackoverflow.com/questions/11016092/how-to-load-classes-at-runtime-from-a-folder-or-jar
            System.out.println("Loading JAR "+path);
            JarFile jarFile = new JarFile(path.toFile());
            Enumeration<JarEntry> jarEntries = jarFile.entries();

            URL[] urls = {new URL("jar:file:"+path.toFile().getCanonicalPath()+"!/")};
            URLClassLoader classLoader = URLClassLoader.newInstance(urls);

            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();

                String name = jarEntry.getName();

                if(jarEntry.isDirectory() || !name.endsWith(".class")) continue;

                // -6 .class extension
                String className = name.substring(0, name.length()-6);
                Class someClass = classLoader.loadClass(className.replace('/', '.'));

                if (IPluginExecutable.class.isAssignableFrom(someClass)) {
                    IPluginExecutable instance = (IPluginExecutable) someClass.newInstance();
                    executorService.submit(new PluginRunner(instance));
                }
            }

            jarFile.close();
            classLoader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
