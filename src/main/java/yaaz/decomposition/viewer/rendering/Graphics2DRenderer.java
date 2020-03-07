package yaaz.decomposition.viewer.rendering;

import yaaz.decomposition.viewer.polygon.Polygon;
import yaaz.decomposition.viewer.polygon.PolygonSet;
import yaaz.decomposition.viewer.polygon.Triangulation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;


public class Graphics2DRenderer extends JPanel implements Renderer {

    private static final int
            VERTEX_POINT_SIZE_PX = 10,
            EDGE_LINE_WIDTH_PX = 3;

    private static final Color
            BACKGROUND_COLOR = Color.WHITE,
            EDGE_COLOR = Color.BLACK,
            VERTEX_COLOR = Color.BLACK,
            INTERSECTION_POINT_COLOR = Color.RED,
            TRIANGLE_COLOR = new Color(1, 0, 0, 0.2F), // Half transparent triangles over polygons to see triangulation defects
            TRIANGLE_EDGE_COLOR = Color.WHITE;


    private PolygonSet polygonSet;
    private Point2D[] allVertices;
    private Triangulation.DecomposedPolygon[] decomposedPolygons;
    private Triangulation.Triangle[] triangles;

    @Override
    public void setPolygonSet(PolygonSet polygonSet) {
        this.polygonSet = polygonSet;
    }
    @Override
    public void setTriangulation(Triangulation triangulation) {
        allVertices = triangulation.getAllVertices();
        decomposedPolygons = triangulation.getDecomposedPolygons();
        triangles = triangulation.getTriangles();
    }
    @Override
    public Component getDrawingComponent() {
        return this;
    }





    @Override
    public void paint(Graphics g) {
        clear(g);
        drawAreas(g);
        drawEdges(g);
        drawVertices(g);
    }


    private void clear(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawAreas(Graphics g) {
        if(allVertices != null && decomposedPolygons != null) {
            for(Triangulation.DecomposedPolygon polygon : decomposedPolygons) {
                int[] xs = new int[polygon.vertexIndices.length], ys = new int[polygon.vertexIndices.length];
                for (int i = 0; i < polygon.vertexIndices.length; i++) {
                    Point2D point = allVertices[polygon.vertexIndices[i]];
                    xs[i] = (int) point.getX();
                    ys[i] = (int) point.getY();
                }
                if(polygon.netWinding == 0) g.setColor(BACKGROUND_COLOR);
                else {
                    float resultColor = 0.3F + (float) Math.pow(0.7, Math.abs(polygon.netWinding));
                    g.setColor(new Color(0, polygon.netWinding < 0 ? resultColor : 0, polygon.netWinding > 0 ? resultColor : 0, 1));
                }
                g.fillPolygon(xs, ys, polygon.vertexIndices.length);
            }
        }
        if(allVertices != null && triangles != null) {
            g.setColor(TRIANGLE_COLOR);
            int[] xs = new int[3], ys = new int[3];
            for(Triangulation.Triangle triangle : triangles) {
                xs[0] = (int) allVertices[triangle.vertex1].getX();
                ys[0] = (int) allVertices[triangle.vertex1].getY();
                xs[1] = (int) allVertices[triangle.vertex2].getX();
                ys[1] = (int) allVertices[triangle.vertex2].getY();
                xs[2] = (int) allVertices[triangle.vertex3].getX();
                ys[2] = (int) allVertices[triangle.vertex3].getY();
                g.fillPolygon(xs, ys, 3);
            }
        }
    }

    private void drawEdges(Graphics g) {
        if(g instanceof Graphics2D) ((Graphics2D) g).setStroke(new BasicStroke(EDGE_LINE_WIDTH_PX));
        g.setColor(EDGE_COLOR);
        for(Polygon polygon : polygonSet.polygons) drawEdges(g, polygon);
        if(allVertices != null && triangles != null) {
            if(g instanceof Graphics2D) ((Graphics2D) g).setStroke(new BasicStroke(1));
            g.setColor(TRIANGLE_EDGE_COLOR);
            for(Triangulation.Triangle triangle : triangles) {
                Point2D v1 = allVertices[triangle.vertex1];
                Point2D v2 = allVertices[triangle.vertex2];
                Point2D v3 = allVertices[triangle.vertex3];
                g.drawLine((int) v1.getX(), (int) v1.getY(), (int) v2.getX(), (int) v2.getY());
                g.drawLine((int) v2.getX(), (int) v2.getY(), (int) v3.getX(), (int) v3.getY());
                g.drawLine((int) v1.getX(), (int) v1.getY(), (int) v3.getX(), (int) v3.getY());
            }
        }
    }
    private static void drawEdges(Graphics g, Polygon polygon) {
        for (int i = 0; i < polygon.vertices.size(); i++) {
            Point2D v1 = polygon.vertices.get(i);
            Point2D v2 = polygon.vertices.get((i + 1) % polygon.vertices.size());
            g.drawLine((int) v1.getX(), (int) v1.getY(), (int) v2.getX(), (int) v2.getY());
        }
    }

    private void drawVertices(Graphics g) {
        if(allVertices != null) {
            g.setColor(INTERSECTION_POINT_COLOR);
            for (int i = 0; i < allVertices.length; i++) {
                drawVertex(g, allVertices[i]);
                drawVertexIndex(g, allVertices[i], i);
            }
        }
        g.setColor(VERTEX_COLOR);
        for(Polygon polygon : polygonSet.polygons) {
            for(Point2D vertex : polygon.vertices) drawVertex(g, vertex);
        }
    }
    private static void drawVertexIndex(Graphics g, Point2D vertex, int index) {
        g.drawString(String.valueOf(index), (int) vertex.getX() + 5, (int) vertex.getY() + 5);
    }
    private static void drawVertex(Graphics g, Point2D vertex) {
        g.fillOval((int) (vertex.getX() - VERTEX_POINT_SIZE_PX * 0.5), (int) (vertex.getY() - VERTEX_POINT_SIZE_PX * 0.5), VERTEX_POINT_SIZE_PX, VERTEX_POINT_SIZE_PX);
    }


}
