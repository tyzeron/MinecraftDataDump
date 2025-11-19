package com.tyzeron.datadump;

import com.tyzeron.datadump.abstraction.block.BlockDataProvider;
import com.tyzeron.datadump.abstraction.block.BlockInfo;
import com.tyzeron.datadump.abstraction.block.BlockStateInfo;
import com.tyzeron.datadump.abstraction.block.PropertyInfo;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * NeoForge-specific implementation of BlockDataProvider.
 */
public class NeoForgeBlockDataProvider implements BlockDataProvider {

    @Override
    public Collection<BlockInfo> getAllBlocks() {
        List<BlockInfo> blockInfoList = new ArrayList<>();

        // Iterate through all registered blocks in NeoForge
        for (Map.Entry<ResourceKey<Block>, Block> entry : BuiltInRegistries.BLOCK.entrySet()) {
            ResourceLocation blockId = entry.getKey().location();
            Block block = entry.getValue();

            // Convert block to BlockInfo
            BlockInfo blockInfo = convertToBlockInfo(blockId.toString(), block);
            blockInfoList.add(blockInfo);
        }

        return blockInfoList;
    }

    private BlockInfo convertToBlockInfo(String identifier, Block block) {
        StateDefinition<Block, BlockState> stateDefinition = block.getStateDefinition();
        Collection<Property<?>> properties = stateDefinition.getProperties();

        // Convert properties
        List<PropertyInfo> propertyInfoList = new ArrayList<>();
        for (Property<?> property : properties) {
            PropertyInfo propertyInfo = convertToPropertyInfo(property);
            propertyInfoList.add(propertyInfo);
        }

        // Convert block states
        List<BlockStateInfo> stateInfoList = new ArrayList<>();
        for (BlockState state : stateDefinition.getPossibleStates()) {
            BlockStateInfo stateInfo = convertToBlockStateInfo(state, properties, block.defaultBlockState());
            stateInfoList.add(stateInfo);
        }

        return new BlockInfo(identifier, propertyInfoList, stateInfoList);
    }

    private <T extends Comparable<T>> PropertyInfo convertToPropertyInfo(Property<T> property) {
        List<String> possibleValues = new ArrayList<>();
        for (T value : property.getPossibleValues()) {
            possibleValues.add(property.getName(value));
        }
        return new PropertyInfo(property.getName(), possibleValues);
    }

    private BlockStateInfo convertToBlockStateInfo(BlockState state, Collection<Property<?>> properties, BlockState defaultState) {
        int stateId = Block.getId(state);
        boolean isDefault = state.equals(defaultState);

        // Convert properties
        Map<String, String> stateProperties = new LinkedHashMap<>();
        for (Property<?> property : properties) {
            String value = getPropertyValueAsString(state, property);
            stateProperties.put(property.getName(), value);
        }

        return new BlockStateInfo(stateId, stateProperties, isDefault);
    }

    private <T extends Comparable<T>> String getPropertyValueAsString(BlockState state, Property<T> property) {
        return property.getName(state.getValue(property));
    }

}
