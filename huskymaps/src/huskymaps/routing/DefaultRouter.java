package huskymaps.routing;

import graphpathfinding.AStarGraph;
import graphpathfinding.AStarPathFinder;
import graphpathfinding.ShortestPathFinder;
import graphpathfinding.ShortestPathResult;
import huskymaps.graph.Coordinate;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;
import pointsets.KDTreePointSet;
import pointsets.Point;
import pointsets.PointSet;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

import static huskymaps.utils.Spatial.projectToPoint;

/**
 * @see Router
 */
public class DefaultRouter extends Router {
    private StreetMapGraph graph;
    private ShortestPathFinder<Node> router;
    private PointSet tree;

    public DefaultRouter(StreetMapGraph graph) {
        this.graph = graph;
        this.router = createPathFinder(graph);
        List<Node> nodes = graph.allNodes();
        List<NodePoint> nodePoints = new ArrayList<NodePoint>();
        for (int i = 0; i < nodes.size(); i++) {
            if (!graph.neighbors(nodes.get(i)).isEmpty()) {
                nodePoints.add(createNodePoint(nodes.get(i)));
            }
        }
        this.tree = createPointSet(nodePoints);
    }

    @Override
    protected <T extends Point> PointSet<T> createPointSet(List<T> points) {
        // uncomment (and import) if you want to use WeirdPointSet instead of your own KDTreePointSet:
        // return new WeirdPointSet<>(points);
        return KDTreePointSet.createAfterShuffling(points);
    }

    @Override
    protected <VERTEX> ShortestPathFinder<VERTEX> createPathFinder(AStarGraph<VERTEX> g) {
        return new AStarPathFinder<>(g);
    }

    @Override
    protected NodePoint createNodePoint(Node node) {
        return projectToPoint(Coordinate.fromNode(node), (x, y) -> new NodePoint(x, y, node));
    }

    @Override
    protected Node closest(Coordinate c) {
        // Project to x and y coordinates instead of using raw lat and lon for finding closest points:
        Point p = projectToPoint(c, Point::new);
        NodePoint result = (NodePoint) tree.nearest(p);
        return result.node();
    }

    @Override
    public List<Node> shortestPath(Coordinate start, Coordinate end) {
        Node src = closest(start);
        Node dest = closest(end);
        ShortestPathResult<Node> result = router.findShortestPath(src, dest, Duration.ofSeconds(10));
        return result.solution();
    }

    @Override
    public List<NavigationDirection> routeDirections(List<Node> route) {
        // Optional
        return null;
    }
}
