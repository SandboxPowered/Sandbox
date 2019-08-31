package com.hrznstudio.sandbox.mixin.impl.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.*;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Mixin(CompoundTag.class)
@Implements(@Interface(iface = com.hrznstudio.sandbox.api.util.nbt.CompoundTag.class, prefix = "sbx$"))
@Unique
public abstract class MixinCompoundTag implements Tag {

    @Shadow
    public abstract int getSize();

    @Shadow
    public abstract Set<String> getKeys();

    @Shadow
    public abstract int getInt(String string_1);

    @Shadow
    public abstract void putInt(String string_1, int int_1);

    @Shadow
    public abstract int[] getIntArray(String string_1);

    @Shadow
    public abstract void putIntArray(String string_1, int[] ints_1);

    @Shadow
    public abstract String getString(String string_1);

    @Shadow
    public abstract void putString(String string_1, String string_2);

    @Shadow
    public abstract double getDouble(String string_1);

    @Shadow
    public abstract byte getByte(String string_1);

    @Shadow
    public abstract void putByte(String string_1, byte byte_1);

    @Shadow
    public abstract byte[] getByteArray(String string_1);

    @Shadow
    public abstract void putByteArray(String string_1, byte[] bytes_1);

    @Shadow
    public abstract long getLong(String string_1);

    @Shadow
    public abstract void putLong(String string_1, long long_1);

    @Shadow
    public abstract boolean getBoolean(String string_1);

    @Shadow
    public abstract void putBoolean(String string_1, boolean boolean_1);

    @Shadow
    public abstract void putDouble(String string_1, double double_1);

    @Shadow
    public abstract boolean containsKey(String string_1);

    @Shadow
    public abstract void remove(String string_1);

    @Shadow
    public abstract void putUuid(String string_1, UUID uUID_1);

    @Shadow
    public abstract UUID getUuid(String string_1);

    public int sbx$size() {
        return getSize();
    }


    public Collection<String> sbx$getKeys() {
        return getKeys();
    }


    public int sbx$getInt(String key) {
        return getInt(key);
    }


    public void sbx$setInt(String key, int i) {
        putInt(key, i);
    }


    public int[] sbx$getIntArray(String key) {
        return getIntArray(key);
    }


    public void sbx$setIntArray(String key, int[] i) {
        putIntArray(key, i);
    }


    public String sbx$getString(String key) {
        return getString(key);
    }


    public void sbx$setString(String key, String s) {
        putString(key, s);
    }


    public double sbx$getDouble(String key) {
        return getDouble(key);
    }


    public void sbx$setDouble(String key, double d) {
        putDouble(key, d);
    }


    public byte sbx$getByte(String key) {
        return getByte(key);
    }


    public void sbx$setByte(String key, byte b) {
        putByte(key, b);
    }


    public byte[] sbx$getByteArray(String key) {
        return getByteArray(key);
    }


    public void sbx$setByteArray(String key, byte[] b) {
        putByteArray(key, b);
    }


    public long sbx$getLong(String key) {
        return getLong(key);
    }


    public void sbx$setLong(String key, long l) {
        putLong(key, l);
    }


    public boolean sbx$getBoolean(String key) {
        return getBoolean(key);
    }


    public void sbx$setBoolean(String key, boolean bool) {
        putBoolean(key, bool);
    }

    public UUID sbx$getUUID(String key) {
        return getUuid(key);
    }

    public void sbx$setUUID(String key, UUID uuid) {
        putUuid(key, uuid);
    }

    public boolean sbx$remove(String key) {
        if (containsKey(key))
            return false;
        remove(key);
        return true;
    }
}