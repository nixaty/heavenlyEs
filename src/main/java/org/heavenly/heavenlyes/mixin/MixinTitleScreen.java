package org.heavenly.heavenlyes.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.heavenly.heavenlyes.ModMenuScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {

    protected MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addCustomButton(int y, int spacingY, CallbackInfo ci) {
        ButtonWidget buttonHEX = ButtonWidget.builder(Text.literal("H-ES"), button -> {
                    ModMenuScreen.openMenu();
                })
                .dimensions(this.width / 2 - 100 + 205, y, 50, 20)
                .build();
        this.addDrawableChild(buttonHEX);
    }
}