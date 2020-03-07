package yaaz.decomposition.viewer.polygon;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


public class Polygon implements Serializable {


    public final List<Point2D> vertices = new CopyOnWriteArrayList<>();


    public Optional<Vertex> findVertexByCoordinates(Point2D coordinates, double radius) {
        double radiusSquared = radius * radius;
        for (int i = 0; i < vertices.size(); i++) {
            Point2D vertex = vertices.get(i);
            if(vertex.distanceSq(coordinates) <= radiusSquared) {
                return Optional.of(new Vertex(i, vertex));
            }
        }
        return Optional.empty();
    }

    public Optional<Vertex> findEdgeByCoordinates(Point2D coordinates, double radius) {
        for (int i = 0; i < vertices.size(); i++) {
            Point2D v1 = vertices.get(i);
            Point2D v2 = vertices.get((i + 1) % vertices.size());
            double dx = v2.getX() - v1.getX();
            double dy = v2.getY() - v1.getY();
            double length = Math.sqrt(dx*dx + dy*dy);
            dx /= length;
            dy /= length;
            double relativeX = coordinates.getX() - v1.getX();
            double relativeY = coordinates.getY() - v1.getY();
            double dot = relativeX * dx + relativeY * dy;
            double perpendicularDistance = Math.abs(relativeX * dy - relativeY * dx);
            if(dot > 0 && dot < length && perpendicularDistance <= radius) return Optional.of(new Vertex(i, v1));
        }
        return Optional.empty();
    }



    public class Vertex {

        public final int index;
        public final Point2D point;

        public Vertex(int index, Point2D point) {
            this.index = index;
            this.point = point;
        }

        public Polygon getPolygon() {
            return Polygon.this;
        }

    }


}
