package yaaz.decomposition.viewer.rendering;

public enum RenderingMode {


    GRAPHICS2D(Graphics2DRenderer::new),
    VULKAN(VulkanRenderer::create);


    final Renderer.Constructor canvasConstructor;


    RenderingMode(Renderer.Constructor canvasConstructor) {
        this.canvasConstructor = canvasConstructor;
    }


}