package com.tyzeron.datadump;

import net.fabricmc.api.ModInitializer;


public class DataDumpFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        DataDump.init();
    }

}
