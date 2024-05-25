package org.heavenly.heavenlyes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class ModMenuScreen extends Screen {

    private ModOptions config;

    protected ModMenuScreen() {
        // The parameter is the title of the screen,
        // which will be narrated when you enter the screen.
        super(Text.literal("Heavenly extensions params"));
        config = ConfigManager.loadConfig();
    }


    public ButtonWidget buttonBack;

    public ButtonWidget buttonModTab;
    public ButtonWidget buttonPingInTab;
    public ButtonWidget buttonModeInTab;
    public ButtonWidget buttonHealthOverPlayer;
    public TextWidget textModTab;
    public TextWidget textPingInTab;
    public TextWidget textModeInTab;
    public TextWidget textHealthOverPlayer;


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        super.render(context, mouseX, mouseY, delta); // Отрисовка содержимого экрана
    }

    @Override
    public void close() {
        ConfigManager.saveConfig(config);
        super.close();
    }

    @Override
    protected void init() {
        config = ConfigManager.actualConfig;

        textModTab = new TextWidget(10 , 20, 100, 20 ,Text.literal(ModTexts.enableCustomTabText), textRenderer).alignLeft();
        buttonModTab = ButtonWidget.builder(getButtonBoolText(config.modifyTab), button -> {
                    config.modifyTab = !config.modifyTab;
                    button.setMessage(getButtonBoolText(config.modifyTab));
                    try {
                        for (PlayerListEntry player : client.getNetworkHandler().getPlayerList()) {
                            ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();

                            player.setDisplayName(null);
                        }
                    } catch (Exception e) {}
                })
                .dimensions(150, 20, 150, 20)
                .tooltip(Tooltip.of(Text.literal(ModTexts.enableCustomTabButtonTip)))
                .build();
        ButtonWidget lastAddButton = buttonModTab;


        textPingInTab = new TextWidget(textModTab.getX(), textModTab.getY() + 25, 100 , 20, Text.literal(ModTexts.customShowPingInTabText), textRenderer).alignLeft();
        buttonPingInTab = ButtonWidget.builder(getButtonBoolText(config.pingInTab), button -> {
                    config.pingInTab = !config.pingInTab;
                    button.setMessage(getButtonBoolText(config.pingInTab));
                })
                .dimensions(lastAddButton.getX(), textPingInTab.getY(), 150, 20)
                .tooltip(Tooltip.of(Text.literal(ModTexts.customShowPingInTabButtonTip)))
                .build();
        lastAddButton = buttonPingInTab;


        textModeInTab = new TextWidget(textPingInTab.getX(), textPingInTab.getY() + 25, 100 , 20, Text.literal(ModTexts.enableUsersGmInTabText), textRenderer).alignLeft();
        buttonModeInTab = ButtonWidget.builder(getButtonBoolText(config.gameModeInTab), button -> {
                    config.gameModeInTab = !config.gameModeInTab;
                    button.setMessage(getButtonBoolText(config.gameModeInTab));
                })
                .dimensions(lastAddButton.getX(), textModeInTab.getY(), 150, 20)
                .tooltip(Tooltip.of(Text.literal(ModTexts.showUsersGM)))
                .build();
        lastAddButton = buttonModeInTab;


        textHealthOverPlayer = new TextWidget(textModeInTab.getX(), textModeInTab.getY() + 25, 100 , 20, Text.literal(ModTexts.enableHpOverPlayersText), textRenderer).alignLeft();
        buttonHealthOverPlayer = ButtonWidget.builder(getButtonBoolText(config.healthOverPlayer), button -> {
                    config.healthOverPlayer = !config.healthOverPlayer;
                    button.setMessage(getButtonBoolText(config.healthOverPlayer));
                })
                .dimensions(lastAddButton.getX(), textHealthOverPlayer.getY(), 150, 20)
                .tooltip(Tooltip.of(Text.literal(ModTexts.showHpOverPlayer)))
                .build();
        lastAddButton = buttonHealthOverPlayer;

        

        buttonBack = ButtonWidget.builder(Text.literal("Done"), button -> {
//                    ConfigManager.saveConfig(config);
                    close();
                })
                .dimensions(width / 2 - 25, height - 30, 50, 20)
                .build();



        addDrawableChild(textModTab);
        addDrawableChild(textPingInTab);
        addDrawableChild(textModeInTab);
        addDrawableChild(textHealthOverPlayer);
        addDrawableChild(buttonModTab);
        addDrawableChild(buttonPingInTab);
        addDrawableChild(buttonModeInTab);
        addDrawableChild(buttonHealthOverPlayer);

        addDrawableChild(buttonBack);
    }

    public static void openMenu() {
        MinecraftClient.getInstance().setScreen(
                new ModMenuScreen()
        );
    }

    private Text getButtonBoolText(boolean enabled) {
        if (enabled) {
            return Text.of(String.valueOf(true)).copy().formatted(Formatting.DARK_GREEN);
        } else {
            return Text.of(String.valueOf(false)).copy().formatted(Formatting.DARK_RED);
        }
    }
}