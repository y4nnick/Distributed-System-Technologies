package dst.ass3.aop.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * Created by amra.
 */

//takes care of plugin monitoring
public class PluginFileMonitoring extends Thread {

    private final WatchService watchService;
    private final ConcurrentHashMap<Path, WatchKey> watchedFiles;
    private PluginExecutor pluginExecutor;

    private Set<Path> created = Collections.synchronizedSet(new HashSet<Path>());

    public PluginFileMonitoring (PluginExecutor pluginExecutor) throws IOException {
        watchService = FileSystems.getDefault().newWatchService();
        watchedFiles = new ConcurrentHashMap<>();
        this.pluginExecutor = pluginExecutor;
    }


    public void monitor(File dir) {
        try {
            WatchKey watchKey = dir.toPath().register(watchService, ENTRY_CREATE, ENTRY_MODIFY);
            watchedFiles.put(dir.toPath(), watchKey);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stopMonitoring(File dir) {
        WatchKey watchKey = watchedFiles.remove(dir.toPath());
        if (watchKey != null) watchKey.cancel();

    }

    public void run() {
        try {
            while (true) {
                //check directories
                checkDirectories();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("Plugin monitor finished!");
        }
    }


    private Path getPath(WatchKey watchKey, WatchEvent watchEvent) {
        Path parent = null;
        for (Path path : watchedFiles.keySet()) {
            if(watchedFiles.get(path).equals(watchKey)){
                parent = path;
                break;
            }
        }

        if (parent == null) return null;

        Path filePath = parent.resolve((Path) watchEvent.context());

        if (watchEvent.kind().equals(ENTRY_CREATE)) {
            created.add(filePath);
            return filePath;

        } else if(created.contains(filePath)) {
            created.remove(filePath);
            return null;

        } else {
            return filePath;
        }
    }


    private void checkDirectories() throws InterruptedException {
        WatchKey watchKey = watchService.take();

        for (WatchEvent watchEvent : watchKey.pollEvents()) {
            //get the path
            Path filePath = getPath(watchKey, watchEvent);
            if (filePath != null) pluginExecutor.runPlugin(filePath);
        }
        watchKey.reset();
    }
}