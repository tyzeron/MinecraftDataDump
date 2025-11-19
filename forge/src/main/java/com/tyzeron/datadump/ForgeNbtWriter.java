package com.tyzeron.datadump;

import com.tyzeron.datadump.abstraction.nbt.NbtCompound;
import com.tyzeron.datadump.abstraction.nbt.NbtList;
import com.tyzeron.datadump.abstraction.nbt.NbtWriter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringTag;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Forge-specific implementation of NbtWriter.
 */
public class ForgeNbtWriter implements NbtWriter {

    @Override
    public void writeNbt(NbtCompound compound, File file, boolean compressed) throws IOException {
        CompoundTag tag = ((ForgeNbtCompound) compound).getTag();
        if (compressed) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                NbtIo.writeCompressed(tag, fos);
            }
        } else {
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
                NbtIo.write(tag, dos);
            }
        }
    }

    @Override
    public NbtCompound createCompound() {
        return new ForgeNbtCompound(new CompoundTag());
    }

    /**
     * Forge-specific NBT compound wrapper.
     */
    static class ForgeNbtCompound implements NbtCompound {

        private final CompoundTag tag;

        public ForgeNbtCompound(CompoundTag tag) {
            this.tag = tag;
        }

        public CompoundTag getTag() {
            return tag;
        }

        @Override
        public void putString(String key, String value) {
            tag.putString(key, value);
        }

        @Override
        public void putInt(String key, int value) {
            tag.putInt(key, value);
        }

        @Override
        public void putBoolean(String key, boolean value) {
            tag.putBoolean(key, value);
        }

        @Override
        public void putCompound(String key, NbtCompound compound) {
            tag.put(key, ((ForgeNbtCompound) compound).getTag());
        }

        @Override
        public void putList(String key, NbtList list) {
            tag.put(key, ((ForgeNbtList) list).getTag());
        }

        @Override
        public NbtCompound createCompound() {
            return new ForgeNbtCompound(new CompoundTag());
        }

        @Override
        public NbtList createList() {
            return new ForgeNbtList(new ListTag());
        }
    }

    /**
     * Forge-specific NBT list wrapper.
     */
    static class ForgeNbtList implements NbtList {

        private final ListTag tag;

        public ForgeNbtList(ListTag tag) {
            this.tag = tag;
        }

        public ListTag getTag() {
            return tag;
        }

        @Override
        public void addString(String value) {
            tag.add(StringTag.valueOf(value));
        }

        @Override
        public void addCompound(NbtCompound compound) {
            tag.add(((ForgeNbtCompound) compound).getTag());
        }
    }

}
