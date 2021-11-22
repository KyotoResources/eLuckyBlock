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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class VandalUpdater extends BukkitRunnable {

    private final Plugin plugin;
    private final String resourceId;
    private final UpdateType updateType;

    @Getter
    private boolean updateFound = false;
    private boolean firstCheck = true;

    @Setter
    private String updateMessage;

    public UpdateAgent checkForUpdates(String currentVersion) {
        try {
            JSONObject lastVersionObject = readLatestVersion();
            JSONArray allVersionsArray = readAllVersions();

            String updateName = lastVersionObject.get("title").toString();
            String updateVersion = (String) ((JSONObject) allVersionsArray.get(0)).get("name");

            Version currentVersionFixed = new Version(currentVersion);
            Version updateVersionFixed = new Version(updateVersion);

            try {
                if (updateVersionFixed.compareTo(currentVersionFixed) > 0) {
                    return new UpdateAgent(plugin, resourceId, currentVersion, updateVersion, updateName);
                } else {
                    return null;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    private JSONArray readAllVersions() throws IOException, JSONException {
        String RESOURCE_VERSIONS_SPIGET = "https://api.spiget.org/v2/resources/%id%/versions?sort=-name";
        try (InputStream stream = new URL(RESOURCE_VERSIONS_SPIGET.replace("%id%", resourceId)).openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String jsonText = readAll(reader);
            return new JSONArray(jsonText);
        }
    }

    private JSONObject readLatestVersion() throws IOException, JSONException {
        String RESOURCE_UPDATES_SPIGET = "https://api.spiget.org/v2/resources/%id%/updates/latest";
        try (InputStream stream = new URL(RESOURCE_UPDATES_SPIGET.replace("%id%", resourceId)).openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String jsonText = readAll(reader);
            return new JSONObject(jsonText);
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    public void run() {
        UpdateAgent agent = checkForUpdates(plugin.getDescription().getVersion());
        if (agent != null) {

            plugin.getLogger().info(String.format("An update has been detected for %s: %s (Current version: %s)",
                    plugin.getName(), agent.getNewVersion(), agent.getCurrentVersion()));

            if (updateMessage == null) {
                updateMessage = ChatColor.DARK_RED + "An update has been detected for " + plugin.getName() + ": %new% (Current version: %current%)";
            }

            updateFound = true;

            if (firstCheck) {
                firstCheck = false;

                updateMessage = updateMessage.replace("%new%", agent.getNewVersion());
                updateMessage = updateMessage.replace("%old%", agent.getCurrentVersion());

                if (updateType == UpdateType.DOWNLOAD) {
                    agent.download(throwable -> {
                        if (throwable != null) {
                            plugin.getLogger().warning("Could not download the update for " + plugin.getName() + ". " +
                                    "Check the log for more info and report this!");
                            throwable.printStackTrace();
                        } else {
                            plugin.getLogger().info("Update successfully downloaded! Please restart the server to load the new version!");
                        }
                    });
                }

                plugin.getServer().getPluginManager().registerEvents(new Listener() {
                    @EventHandler
                    public void onPlayerJoin(PlayerJoinEvent event) {
                        if (event.getPlayer().isOp()) {
                            event.getPlayer().sendMessage(updateMessage);
                        }
                    }
                }, plugin);
            }
        } else {
            updateFound = false;
        }
    }

}
