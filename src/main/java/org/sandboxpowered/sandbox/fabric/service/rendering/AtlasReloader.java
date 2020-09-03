package org.sandboxpowered.sandbox.fabric.service.rendering;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloadListener;
import net.minecraft.util.Identifier;
import org.sandboxpowered.api.client.rendering.RenderPipeline;
import org.sandboxpowered.sandbox.fabric.util.Log;

import java.util.Collection;

public class AtlasReloader implements SynchronousResourceReloadListener {

    public void apply(ResourceManager resourceManager) {
        Collection<Identifier> atlases = resourceManager.findResources("textures/gui", s -> s.endsWith(".atlas.json"));
        if (atlases.isEmpty())
            return;
        UniversalRenderPipeline pipeline = (UniversalRenderPipeline) RenderPipeline.getUniversalPipeline();
        pipeline.getDynamicRenderer().empty();
        //TODO: Multithread, when attempted minecraft gets stuck on loading screen, needs further investigation
        atlases.forEach(id -> pipeline.getDynamicRenderer().loadSprite(resourceManager, id));
        Log.info("Loaded %d sprites", pipeline.getDynamicRenderer().spriteCount());
    }
}