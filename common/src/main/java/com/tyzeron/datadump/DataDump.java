package com.tyzeron.datadump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataDump {

    public static final String MOD_ID = "datadump";
    public static final String MOD_NAME = "Data Dump";
    public static final String MOD_VERSION = "1.0.0";
    public static final String MOD_AUTHOR = "Tyzeron";
    public static final String MOD_DESCRIPTION = "A mod that dumps data from the game";
    public static final String MOD_URL = "https://github.com/tyzeron/MinecraftDataDump";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("{} mod by {} initialized!", MOD_NAME, MOD_AUTHOR);
    }

}
