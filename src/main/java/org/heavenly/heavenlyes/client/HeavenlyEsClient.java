package org.heavenly.heavenlyes.client;

import net.fabricmc.api.ClientModInitializer;

public class HeavenlyEsClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        CustomTab.start();
    }





}
