package yaaz.decomposition.viewer.polygon;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


public class PolygonSet implements Serializable {


    public final List<Polygon> polygons = new CopyOnWriteArrayList<>();


    public Optional<Polygon.Vertex> findVertexByCoordinates(Point2D coordinates, double radius) {
        for(Polygon polygon : polygons) {
            Optional<Polygon.Vertex> vertex = polygon.findVertexByCoordinates(coordinates, radius);
            if(vertex.isPresent()) return vertex;
        }
        return Optional.empty();
    }

    public Optional<Polygon.Vertex> findEdgeByCoordinates(Point2D coordinates, double radius) {
        for(Polygon polygon : polygons) {
            Optional<Polygon.Vertex> vertex = polygon.findEdgeByCoordinates(coordinates, radius);
            if(vertex.isPresent()) return vertex;
        }
        return Optional.empty();
    }


}
