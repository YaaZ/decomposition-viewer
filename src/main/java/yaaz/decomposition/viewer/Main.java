package yaaz.decomposition.viewer;

import yaaz.decomposition.viewer.rendering.RenderingMode;


public class Main {


    public static void main(String[] args) {
        RenderingMode renderingMode = RenderingMode.VULKAN;
        if(args.length == 1 && args[0].equalsIgnoreCase("graphics2d")) renderingMode = RenderingMode.GRAPHICS2D;

        JNI.load(); // We want to force JNI library loading at startup to fail ASAP if it doesn't exist

        new Frame(renderingMode).setVisible(true);
    }


}
