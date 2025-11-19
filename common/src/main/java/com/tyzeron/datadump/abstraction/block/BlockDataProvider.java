package com.tyzeron.datadump.abstraction.block;

import java.util.Collection;


/**
 * Platform-agnostic interface for accessing block data.
 * Each mod loader (Fabric, Forge, NeoForge) implements this interface.
 */
public interface BlockDataProvider {

    /**
     * Get all registered blocks in the game.
     * @return Collection of all blocks
     */
    Collection<BlockInfo> getAllBlocks();
}
