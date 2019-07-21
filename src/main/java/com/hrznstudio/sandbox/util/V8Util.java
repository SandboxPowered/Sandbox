package com.hrznstudio.sandbox.util;

import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.hrznstudio.sandbox.api.ScriptEngine;
import com.hrznstudio.sandbox.server.SandboxServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class V8Util {

    private static ScriptEngine engine;
    public static List<V8Object> OBJECTS;

    public static void push(ScriptEngine se) {
        OBJECTS = new ArrayList<>();
        engine=se;
    }

    public static void pop() {
        OBJECTS.forEach(V8Value::release);
        OBJECTS = null;
        engine=null;
    }

    public static V8Object createV8Object() {
        V8Object o = new V8Object(engine.ENGINE);
        OBJECTS.add(o);
        return o;
    }

    public static V8Array createV8Array() {
        V8Array o = new V8Array(engine.ENGINE);
        OBJECTS.add(o);
        return o;
    }

    public static V8Array createV8Array(V8Object... objs) {
        V8Array array = createV8Array();
        for (V8Object obj : objs) {
            if (obj != null)
                array.push(obj);
            else
                array.pushNull();
        }
        return array;
    }

    public static V8Object createV8Pos(BlockPos pos) {
        return createV8Object()
                .add("x", pos.getX())
                .add("y", pos.getY())
                .add("z", pos.getZ())
                .add("long", pos.asLong());
    }

    public static V8Object createV8ItemStack(ItemStack stack) {
        V8Object v8 = createV8Object();
        addGetSet(v8, "Count", stack::getCount, stack::setCount);
        addGetSet(v8, "Damage", stack::getDamage, stack::setDamage);
        return v8;
    }

    public static ItemStack stackFromV8(V8Object obj) {
        return ItemStack.EMPTY;
    }

    public static V8Object createV8Inventory(Inventory inventory) {
        V8Object v8 = createV8Object();
        addGetSetWithIndex(v8, "StackInSlot", integer -> createV8ItemStack(inventory.getInvStack(integer)), (integer, v8Object) -> {
            inventory.setInvStack(integer, stackFromV8(v8Object));
        });
        return v8;
    }

    public static V8Object createV8Player(PlayerEntity player) {
        V8Object v8 = createV8Object();
        v8.registerJavaMethod((receiver, parameters) -> {
            return createV8Inventory(player.inventory);
        }, "getInventory");
        v8.registerJavaMethod((receiver, parameters) -> {
            return player.getDisplayName().asString();
        }, "getName");
        v8.registerJavaMethod((receiver, parameters) -> {
            return player.getUuidAsString();
        }, "getUUID");
        v8.registerJavaMethod((receiver, parameters) -> {
            player.sendMessage(new LiteralText(parameters.getString(0)));
        }, "sendMessage");
        return v8;
    }

    public static <T> void addGetSet(V8Object obj, String suffix, Supplier<T> get, Consumer<T> set) {
        obj.registerJavaMethod((receiver, parameters) -> {
            set.accept((T) parameters.get(0));
        }, "set" + suffix);
        obj.registerJavaMethod((receiver, parameters) -> {
            return get.get();
        }, "get" + suffix);
    }

    public static <T> void addGetSetWithIndex(V8Object obj, String suffix, Function<Integer, T> get, BiConsumer<Integer, T> set) {
        obj.registerJavaMethod((receiver, parameters) -> {
            set.accept(parameters.getInteger(0), (T) parameters.get(1));
        }, "set" + suffix);
        obj.registerJavaMethod((receiver, parameters) -> {
            return get.apply(parameters.getInteger(0));
        }, "get" + suffix);
    }
}