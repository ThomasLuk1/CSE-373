package graphpathfinding;

import priorityqueues.DoubleMapMinPQ;
import timing.Timer;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @see ShortestPathFinder for more method documentation
 */
public class AStarPathFinder<VERTEX> extends ShortestPathFinder<VERTEX> {
    private final AStarGraph<VERTEX> graph;
    /**
     * Creates a new AStarPathFinder that works on the provided graph.
     */
    public AStarPathFinder(AStarGraph<VERTEX> graph) {
        this.graph = graph;
    }

    @Override
    public ShortestPathResult<VERTEX> findShortestPath(VERTEX start, VERTEX end, Duration timeout) {
        Timer timer = new Timer(timeout);
        int statesExplored = 0;
        if (start.equals(end)) {
            List<VERTEX> path = List.of(start);
            return new ShortestPathResult.Solved<>(path, 0.0, 1, timer.elapsedDuration());
        }

        DoubleMapMinPQ<VERTEX> fringe = new DoubleMapMinPQ<VERTEX>(); // Priority Queue of fringes
        fringe.add(start, 0); // add the start node
        Map<VERTEX, Double> distances = new HashMap<VERTEX, Double>(); // distance from start
        distances.put(start, 0.0); // add the first node into fringe
        Map<VERTEX, VERTEX> previousNode = new HashMap<VERTEX, VERTEX>(); // closest previous node

        while (!fringe.isEmpty()) { // iterates through all items in the fringe
            statesExplored++;
            VERTEX node = fringe.removeMin();
            if (node.equals(end)) {
                VERTEX resultNode = end;
                double solutionWeight = distances.get(end);
                LinkedList<VERTEX> path = new LinkedList<VERTEX>();
                while (resultNode != start) {
                    path.addFirst(resultNode);
                    resultNode = previousNode.get(resultNode);
                }
                path.addFirst(resultNode);
                return new ShortestPathResult.Solved<>(path, solutionWeight, statesExplored, timer.elapsedDuration());
            }
            for (WeightedEdge<VERTEX> edge : graph.neighbors(node)) { // iterates through all the neighbors
                if (!fringe.contains(edge.to()) && !distances.containsKey(edge.to())) {
                    fringe.add(edge.to(), Integer.MAX_VALUE);
                }
                if (!distances.containsKey(edge.to())) {
                    distances.put(edge.to(), Double.MAX_VALUE);
                }
                if (distances.get(edge.to()) > distances.get(node) + edge.weight()) {
                    distances.put(edge.to(), distances.get(node) + edge.weight());
                    fringe.changePriority(edge.to(), distances.get(edge.to())
                        + graph.estimatedDistanceToGoal(edge.to(), end));
                    previousNode.put(edge.to(), node);
                }
                if (timer.isTimeUp()) {
                    return new ShortestPathResult.Timeout<>(statesExplored, timer.elapsedDuration());
                }
            }
        }
        return new ShortestPathResult.Unsolvable<>(statesExplored, timer.elapsedDuration());
    }

    @Override
    protected AStarGraph<VERTEX> graph() {
        return this.graph;
    }
}
