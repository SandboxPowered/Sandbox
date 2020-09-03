package org.sandboxpowered.sandbox.fabric.service.rendering;

import org.sandboxpowered.api.client.rendering.RenderPipeline;
import org.sandboxpowered.api.util.Identity;

public class FabricRenderingService implements RenderPipeline.PipelineService {
    private UniversalRenderPipeline pipeline = new UniversalRenderPipeline();
    @Override
    public RenderPipeline getPipeline(Identity identity) throws RenderPipeline.UnsupportedRenderPipelineException {
        if (!"sandbox:universal".equals(identity.toString()))
            throw new RenderPipeline.UnsupportedRenderPipelineException(identity);
        return pipeline;
    }
}