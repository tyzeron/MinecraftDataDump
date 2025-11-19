package com.tyzeron.datadump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataDump {

    public static final String MOD_ID = "datadump";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Data Dump mod by Tyzeron initialized!");
    }

}
