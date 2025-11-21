package com.tyzeron.datadump;

import com.tyzeron.datadump.abstraction.nbt.NbtCompound;
import com.tyzeron.datadump.abstraction.nbt.NbtList;
import com.tyzeron.datadump.abstraction.nbt.NbtWriter;

import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Fabric-specific implementation of NbtWriter.
 */
public class FabricNbtWriter implements NbtWriter {

    @Override
    public void writeNbt(NbtCompound compound, File file, boolean compressed) throws IOException {
        CompoundTag tag = ((FabricNbtCompound) compound).getTag();
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
        return new FabricNbtCompound(new CompoundTag());
    }

    /**
     * Fabric-specific NBT compound wrapper.
     */
    static class FabricNbtCompound implements NbtCompound {

        private final CompoundTag tag;

        public FabricNbtCompound(CompoundTag tag) {
            this.tag = tag;
        }

        public CompoundTag getTag() {
            return tag;
        }

        @Override
        public void putByte(String key, byte value) {
            tag.putByte(key, value);
        }

        @Override
        public void putShort(String key, short value) {
            tag.putShort(key, value);
        }

        @Override
        public void putInt(String key, int value) {
            tag.putInt(key, value);
        }

        @Override
        public void putLong(String key, long value) {
            tag.putLong(key, value);
        }

        @Override
        public void putFloat(String key, float value) {
            tag.putFloat(key, value);
        }

        @Override
        public void putDouble(String key, double value) {
            tag.putDouble(key, value);
        }

        @Override
        public void putString(String key, String value) {
            tag.putString(key, value);
        }

        @Override
        public void putByteArray(String key, byte[] value) {
            tag.putByteArray(key, value);
        }

        @Override
        public void putIntArray(String key, int[] value) {
            tag.putIntArray(key, value);
        }

        @Override
        public void putLongArray(String key, long[] value) {
            tag.putLongArray(key, value);
        }

        @Override
        public void putBoolean(String key, boolean value) {
            tag.putBoolean(key, value);
        }

        @Override
        public void putCompound(String key, NbtCompound compound) {
            tag.put(key, ((FabricNbtCompound) compound).getTag());
        }

        @Override
        public void putList(String key, NbtList list) {
            tag.put(key, ((FabricNbtList) list).getTag());
        }

        @Override
        public NbtCompound createCompound() {
            return new FabricNbtCompound(new CompoundTag());
        }

        @Override
        public NbtList createList() {
            return new FabricNbtList(new ListTag());
        }
    }

    /**
     * Fabric-specific NBT list wrapper.
     */
    static class FabricNbtList implements NbtList {

        private final ListTag tag;

        public FabricNbtList(ListTag tag) {
            this.tag = tag;
        }

        public ListTag getTag() {
            return tag;
        }

        @Override
        public void addByte(byte value) {
            tag.add(ByteTag.valueOf(value));
        }

        @Override
        public void addShort(short value) {
            tag.add(ShortTag.valueOf(value));
        }

        @Override
        public void addInt(int value) {
            tag.add(IntTag.valueOf(value));
        }

        @Override
        public void addLong(long value) {
            tag.add(LongTag.valueOf(value));
        }

        @Override
        public void addFloat(float value) {
            tag.add(FloatTag.valueOf(value));
        }

        @Override
        public void addDouble(double value) {
            tag.add(DoubleTag.valueOf(value));
        }

        @Override
        public void addString(String value) {
            tag.add(StringTag.valueOf(value));
        }

        @Override
        public void addByteArray(byte[] value) {
            tag.add(new ByteArrayTag(value));
        }

        @Override
        public void addIntArray(int[] value) {
            tag.add(new IntArrayTag(value));
        }

        @Override
        public void addLongArray(long[] value) {
            tag.add(new LongArrayTag(value));
        }

        @Override
        public void addCompound(NbtCompound compound) {
            tag.add(((FabricNbtCompound) compound).getTag());
        }

        @Override
        public void addList(NbtList list) {
            tag.add(((FabricNbtList) list).getTag());
        }
    }

}
