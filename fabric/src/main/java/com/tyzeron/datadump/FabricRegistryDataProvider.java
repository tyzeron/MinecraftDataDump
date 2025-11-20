package com.tyzeron.datadump;

import com.mojang.serialization.Codec;
import com.tyzeron.datadump.abstraction.registry.RegistryData;
import com.tyzeron.datadump.abstraction.registry.RegistryDataProvider;
import com.tyzeron.datadump.abstraction.registry.RegistryEntryData;
import com.tyzeron.datadump.abstraction.registry.RegistryEntryInfo;
import com.tyzeron.datadump.abstraction.registry.RegistryInfo;
import com.tyzeron.datadump.util.NbtConverter;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Fabric-specific implementation of RegistryDataProvider.
 * This implementation is intentionally minimal - most logic is in RegistryDataDump.
 */
public class FabricRegistryDataProvider implements RegistryDataProvider {

    private MinecraftServer server;
    private final FabricNbtConverter nbtConverter = new FabricNbtConverter();

    public FabricRegistryDataProvider() {
        // Server will be set when needed
    }

    public void setServer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public Collection<RegistryInfo> getAllRegistries() {
        if (server == null) {
            DataDump.LOGGER.warn("Server not available, cannot access dynamic registries");
            return Collections.emptyList();
        }

        List<RegistryInfo> registryInfoList = new ArrayList<>();
        RegistryAccess registryAccess = server.registryAccess();

        // Create RegistryOps for encoding with references
        RegistryOps<Tag> registryOps = RegistryOps.create(NbtOps.INSTANCE, registryAccess);

        // Iterate through all registries
        registryAccess.registries().forEach(registryEntry -> {
            ResourceKey<? extends Registry<?>> registryKey = registryEntry.key();
            Registry<?> registry = registryEntry.value();

            String registryId = registryKey.location().toString();
            RegistryInfo registryInfo = convertToRegistryInfo(registryId, registryKey, registry, registryOps);
            registryInfoList.add(registryInfo);
        });

        DataDump.LOGGER.info("Found {} registries", registryInfoList.size());
        return registryInfoList;
    }

    private <T> RegistryInfo convertToRegistryInfo(
            String registryId, ResourceKey<? extends Registry<?>> registryKey, Registry<T> registry,
            RegistryOps<Tag> registryOps
    ) {
        List<RegistryEntryInfo> entries = new ArrayList<>();

        for (var entry : registry.entrySet()) {
            ResourceLocation entryId = entry.getKey().location();
            T element = entry.getValue();
            int rawId = registry.getId(element);

            // Try to encode the element using its codec
            Map<String, Object> encodedData = encodeElement(entryId.toString(), element, registryKey, registryOps);
            
            entries.add(new RegistryEntryData(entryId.toString(), rawId, encodedData));
        }

        DataDump.LOGGER.debug("Registry {} has {} entries", registryId, entries.size());
        return new RegistryData(registryId, entries);
    }

    /**
     * Encodes a registry element using its codec
     */
    private <T> Map<String, Object> encodeElement(
            String identifier, T element, ResourceKey<? extends Registry<?>> registryKey,
            RegistryOps<Tag> registryOps
    ) {
        try {
            Optional<Codec<T>> codecOpt = getCodecForRegistry(registryKey);
            
            if (codecOpt.isPresent()) {
                Codec<T> codec = codecOpt.get();
                var result = codec.encodeStart(registryOps, element);
                
                if (result.result().isPresent()) {
                    Tag nbtTag = result.result().get();
                    return nbtConverter.convertToMap(nbtTag);
                } else if (result.error().isPresent()) {
                    DataDump.LOGGER.debug("Failed to encode {}: {}", identifier, result.error().get().message());
                }
            }
        } catch (Exception e) {
            DataDump.LOGGER.trace("Could not encode element {}: {}", identifier, e.getMessage());
        }
        
        return null;
    }

    /**
     * Gets the codec for a registry from RegistryDataLoader.
     * Uses WORLDGEN_REGISTRIES which contains all data-driven registries.
     */
    private <T> Optional<Codec<T>> getCodecForRegistry(ResourceKey<? extends Registry<?>> registryKey) {
        try {
            for (var loadable : RegistryDataLoader.WORLDGEN_REGISTRIES) {
                if (loadable.key().equals(registryKey)) {
                    return Optional.of((Codec<T>) loadable.elementCodec());
                }
            }
        } catch (Exception e) {
            DataDump.LOGGER.trace("Could not find element codec for registry {}: {}", 
                registryKey.location(), e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Fabric-specific NBT converter
     */
    private static class FabricNbtConverter extends NbtConverter<Tag> {

        @Override
        protected boolean isCompound(Tag tag) {
            return tag instanceof net.minecraft.nbt.CompoundTag;
        }

        @Override
        protected boolean isList(Tag tag) {
            return tag instanceof net.minecraft.nbt.ListTag;
        }

        @Override
        protected boolean isInt(Tag tag) {
            return tag instanceof net.minecraft.nbt.IntTag;
        }

        @Override
        protected boolean isLong(Tag tag) {
            return tag instanceof net.minecraft.nbt.LongTag;
        }

        @Override
        protected boolean isFloat(Tag tag) {
            return tag instanceof net.minecraft.nbt.FloatTag;
        }

        @Override
        protected boolean isDouble(Tag tag) {
            return tag instanceof net.minecraft.nbt.DoubleTag;
        }

        @Override
        protected boolean isByte(Tag tag) {
            return tag instanceof net.minecraft.nbt.ByteTag;
        }

        @Override
        protected boolean isShort(Tag tag) {
            return tag instanceof net.minecraft.nbt.ShortTag;
        }

        @Override
        protected Iterable<String> getCompoundKeys(Tag compound) {
            return ((net.minecraft.nbt.CompoundTag) compound).getAllKeys();
        }

        @Override
        protected Tag getCompoundValue(Tag compound, String key) {
            return ((net.minecraft.nbt.CompoundTag) compound).get(key);
        }

        @Override
        protected Iterable<Tag> getListElements(Tag list) {
            return (net.minecraft.nbt.ListTag) list;
        }

        @Override
        protected int getAsInt(Tag tag) {
            return ((net.minecraft.nbt.IntTag) tag).getAsInt();
        }

        @Override
        protected long getAsLong(Tag tag) {
            return ((net.minecraft.nbt.LongTag) tag).getAsLong();
        }

        @Override
        protected float getAsFloat(Tag tag) {
            return ((net.minecraft.nbt.FloatTag) tag).getAsFloat();
        }

        @Override
        protected double getAsDouble(Tag tag) {
            return ((net.minecraft.nbt.DoubleTag) tag).getAsDouble();
        }

        @Override
        protected byte getAsByte(Tag tag) {
            return ((net.minecraft.nbt.ByteTag) tag).getAsByte();
        }

        @Override
        protected short getAsShort(Tag tag) {
            return ((net.minecraft.nbt.ShortTag) tag).getAsShort();
        }

        @Override
        protected String getAsString(Tag tag) {
            return tag.getAsString();
        }
    }

}
