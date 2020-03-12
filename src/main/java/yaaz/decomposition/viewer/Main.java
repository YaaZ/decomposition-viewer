package yaaz.decomposition.viewer;

import yaaz.decomposition.viewer.rendering.RenderingMode;

import javax.swing.*;


public class Main {


    public static void main(String[] args) {
        JNI.load(); // We want to force JNI library loading at startup to fail ASAP if it doesn't exist

        RenderingMode renderingMode = args.length == 1 && args[0].equalsIgnoreCase("graphics2d") ?
                RenderingMode.GRAPHICS2D : RenderingMode.VULKAN;

        SwingUtilities.invokeLater(() -> new Frame(renderingMode).setVisible(true));
    }


}
