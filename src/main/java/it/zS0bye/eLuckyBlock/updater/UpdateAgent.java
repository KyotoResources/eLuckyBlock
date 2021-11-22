/*
 * MIT License
 *
 * Copyright (c) 2021 ImOnlyFire
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 */

package it.zS0bye.eLuckyBlock.updater;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;

@AllArgsConstructor
public class UpdateAgent {

    private final Plugin plugin;
    private final String resourceId;

    @Getter
    private final String currentVersion;
    @Getter
    private final String newVersion;
    @Getter
    private final String updateName;

    public void download(DownloadCallback callback) {
        String DOWNLOAD_URL = "https://api.spiget.org/v2/resources/%id%/download";

        File updateFolder = plugin.getServer().getUpdateFolderFile();
        Throwable throwable = null;

        if (!updateFolder.exists()) {
            updateFolder.mkdirs();
        }

        File pluginFile = this.getPluginFile();
        if (pluginFile == null) {
            return;
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(DOWNLOAD_URL.replace("%id%", resourceId)).openConnection();
            connection.setRequestProperty("User-Agent", "VandalUpdater");
            FileOutputStream output = new FileOutputStream(new File(updateFolder, pluginFile.getName()));
            output.getChannel().transferFrom(Channels.newChannel(connection.getInputStream()), 0, Long.MAX_VALUE);
            output.flush();
            output.close();
        } catch (Exception e) {
            throwable = e;
        }

        callback.onDownload(throwable);
    }

    private File getPluginFile() {
        try {
            Method method = JavaPlugin.class.getDeclaredMethod("getFile");
            method.setAccessible(true);
            return (File) method.invoke(plugin);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not get plugin file", e);
        }
    }

    @FunctionalInterface
    public interface DownloadCallback {
        void onDownload(Throwable throwable);
    }

}
