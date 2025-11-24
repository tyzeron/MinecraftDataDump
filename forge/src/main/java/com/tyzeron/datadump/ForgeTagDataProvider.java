package com.tyzeron.datadump;

import com.tyzeron.datadump.abstraction.tag.TagDataProvider;
import com.tyzeron.datadump.abstraction.tag.TagEntryInfo;
import com.tyzeron.datadump.abstraction.tag.TagInfo;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Forge-specific implementation of TagDataProvider.
 */
public class ForgeTagDataProvider implements TagDataProvider {

    private MinecraftServer server;

    public ForgeTagDataProvider() {
        // Server will be set when needed
    }

    public void setServer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public Collection<TagInfo> getAllTags() {
        if (server == null) {
            DataDump.LOGGER.warn("Server not available, cannot access tags");
            return Collections.emptyList();
        }

        List<TagInfo> tagInfoList = new ArrayList<>();
        RegistryAccess registryAccess = server.registryAccess();

        // Iterate through all registries
        registryAccess.registries().forEach(registryEntry -> {
            ResourceKey<? extends Registry<?>> registryKey = registryEntry.key();
            Registry<?> registry = registryEntry.value();
            String registryType = registryKey.location().toString();

            // Get all tags for this registry
            Collection<TagInfo> registryTags = extractTagsFromRegistry(registryType, registry);
            tagInfoList.addAll(registryTags);
        });

        DataDump.LOGGER.info("Found {} tags across all registries", tagInfoList.size());
        return tagInfoList;
    }

    @Override
    public Collection<TagInfo> getTagsForRegistry(String registryType) {
        return getAllTags().stream()
            .filter(tag -> tag.getRegistryType().equals(registryType))
            .toList();
    }

    /**
     * Extracts all tags from a specific registry
     */
    private <T> Collection<TagInfo> extractTagsFromRegistry(String registryType, Registry<T> registry) {
        List<TagInfo> tags = new ArrayList<>();

        // Iterate through all tag keys in the registry
        registry.getTagNames().forEach(tagKey -> {
            try {
                TagInfo tagInfo = convertTagToInfo(registryType, registry, tagKey);
                if (tagInfo != null) {
                    tags.add(tagInfo);
                }
            } catch (Exception e) {
                DataDump.LOGGER.trace("Could not extract tag {}: {}", tagKey.location(), e.getMessage());
            }
        });

        DataDump.LOGGER.debug("Registry {} has {} tags", registryType, tags.size());
        return tags;
    }

    /**
     * Converts a Minecraft tag to TagInfo
     */
    private <T> TagInfo convertTagToInfo(String registryType, Registry<T> registry, TagKey<T> tagKey) {
        var tagOptional = registry.getTag(tagKey);
        if (tagOptional.isEmpty()) {
            return null;
        }

        HolderSet.Named<T> namedTag = tagOptional.get();
        ResourceLocation tagId = tagKey.location();
        
        // Extract entries from the tag
        List<TagEntryInfo> entries = new ArrayList<>();
        for (Holder<T> holder : namedTag) {
            // Get the resource location of the entry
            var keyOptional = holder.unwrapKey();
            if (keyOptional.isPresent()) {
                ResourceKey<T> key = keyOptional.get();
                String entryId = key.location().toString();
                
                // All entries are direct references (not tag references) when accessed this way
                // and are required by default
                entries.add(new TagEntryInfo(entryId, false, true));
            }
        }

        // Note: We don't have direct access to the replace flag or optional entries through the runtime tag API.
        // For now, we use default values (replace = false, required = true)
        return new TagInfo(registryType, tagId.toString(), false, entries);
    }

}
