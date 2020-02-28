package org.sandboxpowered.sandbox.fabric.impl;

import org.sandboxpowered.sandbox.api.fluid.Fluid;
import org.sandboxpowered.sandbox.api.fluid.FluidStack;
import org.sandboxpowered.sandbox.api.fluid.Fluids;
import org.sandboxpowered.sandbox.api.util.Identity;
import org.sandboxpowered.sandbox.api.util.nbt.CompoundTag;
import org.sandboxpowered.sandbox.api.util.nbt.ReadableCompoundTag;

public class FluidStackImpl implements FluidStack {
    private final Fluid fluid;
    private int amount;
    private CompoundTag tag;

    public FluidStackImpl(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }

    public FluidStackImpl(Fluid fluid, int amount, CompoundTag tag) {
        this.fluid = fluid;
        this.amount = amount;
        this.tag = tag;
    }

    public FluidStackImpl(ReadableCompoundTag tag) {
        this.fluid = Fluid.REGISTRY.get(Identity.of(tag.getString("Fluid"))).orElse(Fluids.EMPTY.get());
        this.amount = tag.getInt("Amount");
        if (tag.contains("Tag"))
            this.tag = tag.getCompound("Tag");
    }

    @Override
    public boolean isEmpty() {
        return fluid == Fluids.EMPTY || amount == 0;
    }

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public int getVolume() {
        return amount;
    }

    @Override
    public FluidStack setVolume(int volume) {
        amount = volume;
        return this;
    }

    @Override
    public FluidStack copy() {
        return new FluidStackImpl(fluid, amount, tag);
    }

    @Override
    public FluidStack shrink(int amount) {
        this.amount -= Math.min(this.amount, amount);
        return this;
    }

    @Override
    public FluidStack grow(int amount) {
        this.amount += amount;
        return this;
    }

    @Override
    public boolean hasTag() {
        return tag != null;
    }

    @Override
    public CompoundTag getTag() {
        return tag;
    }

    @Override
    public void setTag(CompoundTag tag) {
        this.tag = tag;
    }

    @Override
    public CompoundTag getOrCreateTag() {
        if (!hasTag())
            tag = CompoundTag.create();
        return tag;
    }

    @Override
    public CompoundTag getChildTag(String key) {
        if (hasTag())
            return tag.getCompound(key);
        return null;
    }

    @Override
    public CompoundTag getOrCreateChildTag(String key) {
        CompoundTag tag = getOrCreateTag();
        if (!tag.contains(key))
            tag.setTag(key, CompoundTag.create());
        return tag.getCompound(key);
    }

    @Override
    public CompoundTag asTag() {
        CompoundTag tag = CompoundTag.create();
        tag.setString("Fluid", fluid.getIdentity().toString());
        tag.setInt("Amount", amount);
        if (hasTag())
            tag.setTag("Tag", this.getTag());
        return tag;
    }

    @Override
    public boolean isEqualTo(FluidStack stack) {
        return stack.getFluid() == getFluid() && stack.getVolume() == getVolume();
    }

    @Override
    public boolean areTagsEqual(FluidStack stack) {
        return !hasTag() && !stack.hasTag() || getTag().equals(stack.getTag());
    }
}