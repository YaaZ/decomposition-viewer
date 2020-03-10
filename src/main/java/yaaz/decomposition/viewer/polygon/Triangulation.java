package yaaz.decomposition.viewer.polygon;


import yaaz.decomposition.viewer.JNI;

import java.awt.geom.Point2D;
import java.lang.ref.Cleaner;

/**
 * Native handle to results of triangulation algorithm including intermediate steps for easy debug rendering
 */
public class Triangulation {


    static {
        JNI.load();
    }
    private static final Cleaner CLEANER = Cleaner.create();



    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final long address;
    private Triangulation(long address) {
        this.address = address;
        CLEANER.register(this, ()-> destroy(address));
    }


    public static native Triangulation create(PolygonSet polygonSet);

    private static native void destroy(long address);

    public native Point2D[] getAllVertices();

    public native DecomposedPolygon[] getDecomposedPolygons();

    public native Triangle[] getTriangles();





    public static class DecomposedPolygon {
        public final int netWinding;
        public final int[] vertexIndices;
        private DecomposedPolygon(int netWinding, int[] vertexIndices) {
            this.netWinding = netWinding;
            this.vertexIndices = vertexIndices;
        }
    }


    public static class Triangle {
        public final int vertex1, vertex2, vertex3;
        private Triangle(int vertex1, int vertex2, int vertex3) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.vertex3 = vertex3;
        }
    }


}
