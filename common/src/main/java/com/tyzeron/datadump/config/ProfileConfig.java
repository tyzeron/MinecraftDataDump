package com.tyzeron.datadump.config;

import java.util.Map;


/**
 * Represents a profile configuration loaded from a TOML file
 */
public class ProfileConfig {

    private ExportConfig export;
    private BlocksConfig blocks;
    private RegistriesConfig registries;
    private Map<String, MultiOutputConfig> multi_output;

    public ExportConfig getExport() {
        return export;
    }

    public void setExport(ExportConfig export) {
        this.export = export;
    }

    public BlocksConfig getBlocks() {
        return blocks;
    }

    public void setBlocks(BlocksConfig blocks) {
        this.blocks = blocks;
    }

    public RegistriesConfig getRegistries() {
        return registries;
    }

    public void setRegistries(RegistriesConfig registries) {
        this.registries = registries;
    }

    public Map<String, MultiOutputConfig> getMultiOutput() {
        return multi_output;
    }

    public void setMultiOutput(Map<String, MultiOutputConfig> multi_output) {
        this.multi_output = multi_output;
    }

    public static class ExportConfig {

        private String format;
        private boolean single_file;
        private String filename;
        private JsonConfig json;
        private NbtConfig nbt;

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public boolean isSingleFile() {
            return single_file;
        }

        public void setSingleFile(boolean single_file) {
            this.single_file = single_file;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public JsonConfig getJson() {
            return json;
        }

        public void setJson(JsonConfig json) {
            this.json = json;
        }

        public NbtConfig getNbt() {
            return nbt;
        }

        public void setNbt(NbtConfig nbt) {
            this.nbt = nbt;
        }
    }

    public static class JsonConfig {

        private boolean pretty;

        public boolean isPretty() {
            return pretty;
        }

        public void setPretty(boolean pretty) {
            this.pretty = pretty;
        }
    }

    public static class NbtConfig {

        private boolean compressed;

        public boolean isCompressed() {
            return compressed;
        }

        public void setCompressed(boolean compressed) {
            this.compressed = compressed;
        }
    }

    public static class BlocksConfig {

        private boolean properties;
        private boolean states;

        public boolean isProperties() {
            return properties;
        }

        public void setProperties(boolean properties) {
            this.properties = properties;
        }

        public boolean isStates() {
            return states;
        }

        public void setStates(boolean states) {
            this.states = states;
        }
    }

    public static class RegistriesConfig {

        private boolean codec;

        public boolean isCodec() {
            return codec;
        }

        public void setCodec(boolean codec) {
            this.codec = codec;
        }
    }

    public static class MultiOutputConfig {

        private String file;
        private String format;

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }
    }

}
