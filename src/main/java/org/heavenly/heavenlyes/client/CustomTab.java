package org.heavenly.heavenlyes.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.heavenly.heavenlyes.ConfigManager;
import org.heavenly.heavenlyes.ModOptions;

public class CustomTab {
    private boolean hasJoinedServer = false;
    private ModOptions config;

    public void startTabEditor() {
        config = ConfigManager.loadConfig();
//        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
//        });

//        WorldRenderEvents.BEFORE_ENTITIES.register(this::onRender);

        // Регистрируем обработчик для отображения таба
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {


            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.getNetworkHandler() == null)
                return;

            if (config.modifyTab) {
                for (PlayerListEntry player : client.getNetworkHandler().getPlayerList()) {

                    String originalName = player.getProfile().getName();
                    Text modifiedName = Text.of(originalName);
                    if (config.pingInTab) modifiedName = Text.of(originalName + " "+getPing(player.getLatency()));
                    if (config.gameModeInTab) modifiedName = Text.of(getGameModePrefix(player.getGameMode().getId()) + " " + modifiedName.getString());
                    player.setDisplayName(modifiedName);
                }
            }


        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            config = ConfigManager.actualConfig;
            if (client.player != null && client.getCurrentServerEntry() != null && !hasJoinedServer && config.pingInChat) {
                hasJoinedServer = true;
                Text pingsMessage = Text.of("§3Пинг игроков: \n");
                MutableText message = MutableText.of(pingsMessage.getContent());
                for (PlayerListEntry player : getServerPlayers(client)) {
                    message.append("§3" + player.getProfile().getName() + ": " + player.getLatency() + "\n");

                }
                client.player.sendMessage(message);



            } else if (client.player == null && hasJoinedServer) {
                hasJoinedServer = false;
            }
        });


    }


    private String getPing(int ping) {
        if (ping < 100) {
            return "§a" + ping + "ms";
        } else if (ping < 300) {
            return "§e" + ping + "ms";
        } else {
            return "§c" + ping + "ms";
        }
    }


    private String getGameModePrefix(int gameMode) {
        switch (gameMode) {
            case 0:
                return "§c[SU]§r "; // Creative
            case 1:
                return "§6[CR]§r "; // Survival
            case 2:
                return "§e[AD]§r "; // Adventure
            case 3:
                return "§3[SP]§r "; // Spectator
            default:
                return ""; // Пустая приставка по умолчанию
        }
    }

    private Iterable<PlayerListEntry> getServerPlayers(MinecraftClient client) {
        assert client.player != null;
        return client.player.networkHandler.getPlayerList();
    }

    public static void start() {
        CustomTab customTab = new CustomTab();
        customTab.startTabEditor();
    }
}
