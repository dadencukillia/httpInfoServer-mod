package com.crocoby;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class InfoCollector {
    // Turns string like "item.minecraft.stick" to "minecraft:stick" (format that minecraft uses)
    public static String getIdentifierByTranslationKey(String translation) {
        String[] split = translation.split("\\.");
        return split[1]+":"+ Arrays.stream(split).skip(2).collect(Collectors.joining("."));
    }

    // Generates JSON string with info about player
    public static String genJson() {
        // Creating map to store data
        HashMap<String, Object> jsonData = new HashMap<>();

        jsonData.put("nickname", MinecraftClient.getInstance().getGameProfile().getName());
        jsonData.put("UUID", MinecraftClient.getInstance().getGameProfile().getId().toString());

        ClientWorld world = MinecraftClient.getInstance().world;

        jsonData.put("isInWorld", world!=null);

        // Data about world player in
        if (world != null) {
            HashMap<String, Object> worldData = new HashMap<>();

            worldData.put("isDay", world.isDay());
            worldData.put("weather", world.isRaining()?"rain":(world.isThundering()?"thunder":"clear"));
            worldData.put("timeOfDay", world.getTimeOfDay());
            worldData.put("time", world.getTime());
            Identifier world_regkey = world.getRegistryKey().getValue();
            worldData.put("type", world_regkey.equals(DimensionTypes.OVERWORLD_ID)?"overworld":(
                    world_regkey.equals(DimensionTypes.THE_END_ID)?"theEnd":(
                                world_regkey.equals(DimensionTypes.THE_NETHER_ID)?"theNether":"custom"
                            )
                    )
            );

            // Data about players in the world
            ArrayList<HashMap<String, Object>> playersData = new ArrayList<>();
            for (AbstractClientPlayerEntity pl : world.getPlayers()) {
                HashMap<String, Object> playerData = new HashMap<>();

                playerData.put("skinUrl", pl.getSkinTextures().textureUrl());
                playerData.put("isSkinSlim", pl.getSkinTextures().model().getName().equals("slim"));
                playerData.put("nickname", pl.getGameProfile().getName());
                if (pl.getDisplayName() != null) {
                    playerData.put("displayName", pl.getDisplayName().getString());
                }
                playerData.put("UUID", pl.getUuidAsString());
                playerData.put("position", new double[]{pl.getX(), pl.getY(), pl.getZ()});
                playerData.put("gamemode", pl.isSpectator()?"spectator":(pl.isCreative()?"creative":"survival"));
                playerData.put("lightLevelAtPlayer", world.getLightLevel(pl.getBlockPos()));
                playerData.put("luminaceAtPlayer", world.getLuminance(pl.getBlockPos()));
                playerData.put("blockUnder", getIdentifierByTranslationKey(world.getBlockState(pl.getBlockPos().subtract(new Vec3i(0, 1, 0))).getBlock().getTranslationKey()));
                playerData.put("holdingItem", getIdentifierByTranslationKey(pl.getInventory().getMainHandStack().getItem().getTranslationKey()));

                playersData.add(playerData);
            }
            worldData.put("players", playersData);

            // Data about entities in the world
            ArrayList<HashMap<String, Object>> entitiesData = new ArrayList<>();
            for (Entity en : world.getEntities()) {
                String e = getIdentifierByTranslationKey(en.getType().getTranslationKey());

                if (e.equals("minecraft:player")) {
                    continue;
                }
                HashMap<String, Object> entityData = new HashMap<>();

                entityData.put("id", en.getId());
                entityData.put("typeName", e);
                entityData.put("position", new double[]{en.getX(), en.getY(), en.getZ()});

                entitiesData.add(entityData);
            }
            worldData.put("entities", entitiesData);

            // Data about server
            ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();
            worldData.put("isServer", server!=null);
            if (server != null) {
                HashMap<String, Object> serverData = new HashMap<>();

                serverData.put("isLocal", server.isLocal());
                serverData.put("isRealm", server.isRealm());
                serverData.put("address", server.address);
                serverData.put("currentPing", server.ping);
                serverData.put("name", server.name);
                serverData.put("motd", server.label.getLiteralString());

                worldData.put("server", serverData);
            }

            jsonData.put("world", worldData); // Store world data using "world" key
        }

        // Converting map to JSON string
        return new Gson().toJson(jsonData);
    }

    public InfoCollector() {}
}
