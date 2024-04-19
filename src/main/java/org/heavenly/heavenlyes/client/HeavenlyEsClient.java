package org.heavenly.heavenlyes.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.heavenly.heavenlyes.client.ModMenuScreen;
import org.heavenly.heavenlyes.client.ModOptions;

import static com.mojang.text2speech.Narrator.LOGGER;

public class HeavenlyEsClient implements ClientModInitializer {
    private boolean hasJoinedServer = false;
    private ModOptions config;

    @Override
    public void onInitializeClient() {
        config = ConfigManager.loadConfig();
//        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
//        });


        // Регистрируем обработчик для отображения таба
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {

            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.getNetworkHandler() == null)
                return;

            if (config.modifyTab) {
                for (PlayerListEntry player : client.getNetworkHandler().getPlayerList()) {
                    ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
                    networkHandler.getWorld().getPlayers();

                    String originalName = player.getProfile().getName();
                    Text modifiedName = Text.of(getGameModePrefix(player.getGameMode().getId())).copy().append(Text.of(originalName)).append(" "+getPing(player.getLatency()));
                    player.setDisplayName(modifiedName);
                }
            } else {
                for (PlayerListEntry player : client.getNetworkHandler().getPlayerList()) {
                    ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();

                    player.setDisplayName(null);
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
        if (config.pingInTab) {
            if (ping < 100) {
                return "  §a" + ping + "ms";
            } else if (ping < 300) {
                return "  §e" + ping + "ms";
            } else {
                return "  §c" + ping + "ms";
            }
        } else {
            return "";
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
}
