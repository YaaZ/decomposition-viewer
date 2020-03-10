package yaaz.decomposition.viewer;

import yaaz.decomposition.viewer.polygon.Polygon;
import yaaz.decomposition.viewer.polygon.PolygonSet;
import yaaz.decomposition.viewer.polygon.Triangulation;
import yaaz.decomposition.viewer.rendering.Renderer;
import yaaz.decomposition.viewer.rendering.RenderingMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


public class Frame extends JFrame {


    private final Renderer renderer;

    public Frame(RenderingMode renderingMode) {
        renderer = Renderer.create(renderingMode);
        setTitle("Decomposition viewer - Nikita Gubarkov");
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setupContent();
    }

    private void setupContent() {
        setLayout(new BorderLayout());
        add(new JLabel(loadHelpHtml()), BorderLayout.SOUTH);

        Controller controller = new Controller();
        renderer.setPolygonSet(controller.polygonSet);
        renderer.getDrawingComponent().addMouseListener(controller);
        renderer.getDrawingComponent().addMouseMotionListener(controller);
        add(renderer.getDrawingComponent(), BorderLayout.CENTER);
    }

    private String loadHelpHtml() {
        try {
            return new String(getClass().getResourceAsStream("/help.html").readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new Error(e);
        }
    }





    private class Controller extends MouseAdapter {
        private static final int VERTEX_POINT_SIZE_PX = 10;

        private final PolygonSet polygonSet = new PolygonSet();
        private Point2D lastLMBPressCoordinates;
        private Polygon.Vertex currentlyDraggingVertex;


        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() != MouseEvent.BUTTON1 && e.getButton() != MouseEvent.BUTTON3) return;
            Optional<Polygon.Vertex> vertex =
                    polygonSet.findVertexByCoordinates(new Point2D.Double(e.getX(), e.getY()), VERTEX_POINT_SIZE_PX);
            if(vertex.isPresent()) {
                if(e.getButton() == MouseEvent.BUTTON3) {
                    deleteVertex(vertex.get());
                    updateTriangulation();
                }
            }
            else if(e.getButton() == MouseEvent.BUTTON1) {
                addNewTriangle(e.getX(), e.getY());
                updateTriangulation();
            }
        }
        private void deleteVertex(Polygon.Vertex vertex) {
            if(vertex.getPolygon().vertices.size() <= 3) polygonSet.polygons.remove(vertex.getPolygon());
            else vertex.getPolygon().vertices.remove(vertex.index);
        }
        private void addNewTriangle(double x, double y) {
            final double newTriangleRadiusPx = 40;
            Polygon newTriangle = new Polygon();
            newTriangle.vertices.add(new Point2D.Double(x, y - newTriangleRadiusPx));
            newTriangle.vertices.add(new Point2D.Double(x - newTriangleRadiusPx * 0.866, y + newTriangleRadiusPx * 0.5));
            newTriangle.vertices.add(new Point2D.Double(x + newTriangleRadiusPx * 0.866, y + newTriangleRadiusPx * 0.5));
            polygonSet.polygons.add(newTriangle);
        }


        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) lastLMBPressCoordinates = new Point2D.Double(e.getX(), e.getY());
        }


        @Override
        public void mouseReleased(MouseEvent e) {
            if(currentlyDraggingVertex != null) updateTriangulation();
            lastLMBPressCoordinates = null;
            currentlyDraggingVertex = null;
        }


        @Override
        public void mouseDragged(MouseEvent e) {
            if(currentlyDraggingVertex == null && lastLMBPressCoordinates != null) {
                currentlyDraggingVertex = findOrTryCreatingVertexOnEdge(lastLMBPressCoordinates).orElse(null);
            }
            if(currentlyDraggingVertex != null) {
                currentlyDraggingVertex.getPolygon().vertices.set(currentlyDraggingVertex.index, new Point2D.Double(e.getX(), e.getY()));
                updateTriangulation();
            }
        }
        private Optional<Polygon.Vertex> findOrTryCreatingVertexOnEdge(Point2D coordinates) {
            Optional<Polygon.Vertex> vertex =
                    polygonSet.findVertexByCoordinates(coordinates, VERTEX_POINT_SIZE_PX * 0.5);
            if(vertex.isPresent()) return vertex;
            Optional<Polygon.Vertex> edgeVertex =
                    polygonSet.findEdgeByCoordinates(coordinates, VERTEX_POINT_SIZE_PX * 0.5);
            if(edgeVertex.isPresent()) {
                Polygon.Vertex base = edgeVertex.get();
                base.getPolygon().vertices.add(base.index + 1, coordinates);
                return Optional.of(base.getPolygon().new Vertex(base.index + 1, coordinates));
            }
            else return Optional.empty();
        }


        private void updateTriangulation() {
            System.out.println("\nPolygon set updated:");
            for(Polygon polygon : polygonSet.polygons) {
                for(Point2D vertex : polygon.vertices) System.out.println(vertex.getX() + ", " + vertex.getY());
                System.out.println();
            }

            renderer.setTriangulation(Triangulation.create(polygonSet));
            renderer.getDrawingComponent().repaint();
        }

    }


}
