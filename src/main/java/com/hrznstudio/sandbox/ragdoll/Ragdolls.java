package com.hrznstudio.sandbox.ragdoll;

import com.hrznstudio.sandbox.client.entity.RagdollEntity;
import com.hrznstudio.sandbox.ragdoll.generation.data.RagdollData;
import com.hrznstudio.sandbox.ragdoll.ragdolls.generated.FromDataRagdoll;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sekawh on 8/4/2015.
 */
public class Ragdolls {
    // add code to store a hashmap of all entities to ragdolls

    // Key is entity class and stores a ragdoll class
    private static Map<String, RagdollData> entityToRagdollHashmap = new HashMap<String, RagdollData>();

    private static MinecraftClient mc = MinecraftClient.getInstance();

    public static EntityType<RagdollEntity> RAGDOLL = Registry.register(Registry.ENTITY_TYPE, new Identifier("sandbox", "ragdoll"),
            EntityType.Builder.create(RagdollEntity::new, EntityCategory.MISC).disableSaving().disableSummon().setDimensions(0.15f, 0.15f).build(new Identifier("sandbox", "ragdoll").toString()));


    public List<RagdollEntity> ragdolls = new ArrayList<>();

    public static EntityRenderDispatcher entityRenderDispatcher;

    /**
     * Need to add update counts to the ragdoll data rather than global also 10 is for cloths
     */
    //public static int maxUpdateCount = 10;


    public static float gravity = 0.05F;
    //public static float gravity = 0F;

    public void registerRagdoll(Class<? extends Entity> entityClass, RagdollData ragdollData) {
        this.entityToRagdollHashmap.put(entityClass.getName(), ragdollData);

    }

    /**
     * Will overwrite ragdolls if there is one already there
     * @param entityClass
     * @param ragdollData
     */
    public void registerRagdoll(String entityClass, RagdollData ragdollData) {
        this.entityToRagdollHashmap.put(entityClass, ragdollData);
    }

    public void updateRagdolls() {
        this.ragdolls.removeIf(ragdoll -> ragdoll.removed);

        for(RagdollEntity ragdoll : this.ragdolls) {
            ragdoll.tick();
        }
    }

    public void spawnRagdoll(RagdollEntity ragdoll) {
        this.ragdolls.add(ragdoll);
    }

    /**
     * TODO replace with callback to addon to create/set the ragdoll data allowing to register to entities.
     * @param entity
     * @return
     */
    public FromDataRagdoll createRagdoll(Entity entity) {

        FromDataRagdoll ragdoll = null;

        try
        {
            String entityClass = entity.getClass().getName();
            RagdollData ragdollData = entityToRagdollHashmap.get(entityClass);

            if (ragdollData != null) {
                ragdoll = new FromDataRagdoll(ragdollData);
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return ragdoll;
    }

    // Material.rock for the sounds, could add a bit of bouncing from materials rather than instantly losing velocity when it hits soemthing.
}
