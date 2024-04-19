package org.heavenly.heavenlyes.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.heavenly.heavenlyes.ModTexts;

@Environment(EnvType.CLIENT)
public class ModMenuScreen extends Screen {

    private ModOptions config = ConfigManager.loadConfig();

    protected ModMenuScreen() {
        // The parameter is the title of the screen,
        // which will be narrated when you enter the screen.
        super(Text.literal("Heavenly extensions params"));
        config = ConfigManager.loadConfig();
    }


    public ButtonWidget buttonModTab;
    public ButtonWidget buttonPingInTab;
    public ButtonWidget buttonBack;
    public TextWidget textModTab;
    public TextWidget textPingInTab;

    private ButtonWidget lastAddButton;


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

        textModTab = new TextWidget(10 , 20, 100, 20 ,Text.literal(ModTexts.enableCustomTabText), textRenderer).alignLeft();
        buttonModTab = ButtonWidget.builder(getButtonBoolText(config.modifyTab), button -> {
                    config.modifyTab = !config.modifyTab;
                    button.setMessage(getButtonBoolText(config.modifyTab));
                })
                .dimensions(150, 20, 150, 20)
                .tooltip(Tooltip.of(Text.literal(ModTexts.enableCustomTabButtonTip)))
                .build();
        lastAddButton = buttonModTab;

        textPingInTab = new TextWidget(textModTab.getX(), textModTab.getY() + 25, 100 , 20, Text.literal(ModTexts.customShowPingInTabText), textRenderer).alignLeft();
        buttonPingInTab = ButtonWidget.builder(getButtonBoolText(config.pingInTab), button -> {
                    config.pingInTab = !config.pingInTab;
                    button.setMessage(getButtonBoolText(config.pingInTab));
                })
                .dimensions(lastAddButton.getX(), textPingInTab.getY(), 150, 20)
                .tooltip(Tooltip.of(Text.literal(ModTexts.customShowPingInTabButtonTip)))
                .build();
        lastAddButton = buttonPingInTab;


        buttonBack = ButtonWidget.builder(Text.literal("Done"), button -> {
//                    ConfigManager.saveConfig(config);
                    close();
                })
                .dimensions(width / 2 - 20, height - 25, 45, 20)
                .build();



        addDrawableChild(textPingInTab);
        addDrawableChild(textModTab);
        addDrawableChild(buttonModTab);
        addDrawableChild(buttonPingInTab);
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