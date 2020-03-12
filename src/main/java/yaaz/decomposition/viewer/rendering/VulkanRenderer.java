package yaaz.decomposition.viewer.rendering;

import yaaz.decomposition.viewer.polygon.PolygonSet;
import yaaz.decomposition.viewer.polygon.Triangulation;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.lang.ref.Cleaner;


class VulkanRenderer extends Canvas implements Renderer {
    private static final Cleaner CLEANER = Cleaner.create();


    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final long nativeHandle;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private PolygonSet polygonSet;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Triangulation triangulation;


    private VulkanRenderer(long nativeHandle) {
        this.nativeHandle = nativeHandle;
        CLEANER.register(this, ()->destroy(nativeHandle));
    }


    @Override
    public void setPolygonSet(PolygonSet polygonSet) {
        this.polygonSet = polygonSet;
    }
    @Override
    public void setTriangulation(Triangulation triangulation) {
        this.triangulation = triangulation;
    }
    @Override
    public Component getDrawingComponent() {
        return this;
    }



    public static native VulkanRenderer create();

    private static native void destroy(long nativeHandle);

    @Override
    public void paint(Graphics g) {
        if(getBufferStrategy() == null) createBufferStrategy(2);
        AffineTransform transform = getGraphicsConfiguration().getDefaultTransform();
        paint(transform.getScaleX(), transform.getScaleY());
    }


    private native void paint(double scaleX, double scaleY);


}
