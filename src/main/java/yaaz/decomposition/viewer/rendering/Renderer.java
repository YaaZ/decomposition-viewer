package yaaz.decomposition.viewer.rendering;

import yaaz.decomposition.viewer.polygon.PolygonSet;
import yaaz.decomposition.viewer.polygon.Triangulation;

import java.awt.*;


public interface Renderer {


    void setPolygonSet(PolygonSet polygonSet);

    void setTriangulation(Triangulation triangulation);

    Component getDrawingComponent();


    static Renderer create(RenderingMode renderingMode) {
        return renderingMode.canvasConstructor.create();
    }



    @FunctionalInterface
    interface Constructor {
        Renderer create();
    }


}
