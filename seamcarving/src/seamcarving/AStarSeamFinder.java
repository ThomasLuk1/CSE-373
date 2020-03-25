package seamcarving;

import graphpathfinding.AStarGraph;
import graphpathfinding.AStarPathFinder;
import graphpathfinding.ShortestPathFinder;
import graphpathfinding.WeightedEdge;

import java.awt.Point;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class AStarSeamFinder extends SeamFinder {
    /*
    Use this method to create your ShortestPathFinder.
    This will be overridden during grading to use our solution path finder, so you don't get
    penalized again for any bugs in code from previous assignments
    */
    @Override
    protected <VERTEX> ShortestPathFinder<VERTEX> createPathFinder(AStarGraph<VERTEX> graph) {
        return new AStarPathFinder<>(graph);
    }

    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        List<Integer> path = new ArrayList<>();
        HorizontalEnergyGraph graph = new HorizontalEnergyGraph(energies);
        ShortestPathFinder<Point> pathFinder = createPathFinder(graph);
        Point start = new Point(-1, -1);
        Point end = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        List<Point> result = pathFinder.findShortestPath(start, end, Duration.ofSeconds(10)).solution();
        result.remove(0);
        result.remove(result.size() - 1);
        for (Point p: result) {
            path.add((int) p.getY());
        }
        return path;
    }

    private class HorizontalEnergyGraph implements AStarGraph<Point> {
        double[][] energies;
        Point start = new Point(-1, -1);
        Point end = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

        public HorizontalEnergyGraph(double[][] energies) {
            this.energies = energies;
        }

        @Override
        public List<WeightedEdge<Point>> neighbors(Point vertex) {
            List<WeightedEdge<Point>> neighbors = new ArrayList<>();
            int x = (int) vertex.getX();
            int y = (int) vertex.getY();
            if (vertex.equals(start)) {
                for (int j = 0; j < energies[0].length; j++) {
                   neighbors.add(new WeightedEdge<>(vertex, new Point(0, j), energies[0][j]));
                }
                return neighbors;
            }
            if (x == energies.length - 1) {
                neighbors.add(new WeightedEdge<>(vertex, end, 0));
                return neighbors;
            }
            if (y < energies[0].length - 1) {
                neighbors.add(new WeightedEdge<>(vertex, new Point(x + 1, y + 1), energies[x + 1][y + 1]));
            }
            if (y > 0) {
                neighbors.add(new WeightedEdge<>(vertex, new Point(x + 1, y - 1), energies[x + 1][y - 1]));
            }
            neighbors.add(new WeightedEdge<>(vertex, new Point(x + 1, y), energies[x + 1][y]));
            return neighbors;
        }

        @Override
        public double estimatedDistanceToGoal(Point v, Point goal) {
            return 0;
        }

    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        List<Integer> path = new ArrayList<>();
        VerticalEnergyGraph graph = new VerticalEnergyGraph(energies);
        ShortestPathFinder<Point> pathFinder = createPathFinder(graph);
        Point start = new Point(-1, -1);
        Point end = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        List<Point> result = pathFinder.findShortestPath(start, end, Duration.ofSeconds(10)).solution();
        result.remove(0);
        result.remove(result.size() - 1);
        for (Point p: result) {
            path.add((int) p.getX());
        }
        return path;
    }

    private class VerticalEnergyGraph implements AStarGraph<Point> {
        double[][] energies;
        Point start = new Point(-1, -1);
        Point end = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

        public VerticalEnergyGraph(double[][] energies) {
            this.energies = energies;
        }

        @Override
        public List<WeightedEdge<Point>> neighbors(Point vertex) {
            List<WeightedEdge<Point>> neighbors = new ArrayList<>();
            int x = (int) vertex.getX();
            int y = (int) vertex.getY();
            if (vertex.equals(start)) {
                for (int i = 0; i < energies.length; i++) {
                    neighbors.add(new WeightedEdge<>(vertex, new Point(i, 0), energies[i][0]));
                }
                return neighbors;
            }
            if (y == energies[0].length - 1) {
                neighbors.add(new WeightedEdge<>(vertex, end, 0));
                return neighbors;
            }
            if (x > 0) {
                neighbors.add(new WeightedEdge<>(vertex, new Point(x - 1, y + 1), energies[x - 1][y + 1]));
            }
            if (x < energies.length - 1) {
                neighbors.add(new WeightedEdge<>(vertex, new Point(x + 1, y + 1), energies[x + 1][y + 1]));
            }
            neighbors.add(new WeightedEdge<>(vertex, new Point(x, y + 1), energies[x][y + 1]));
            return neighbors;
        }

        @Override
        public double estimatedDistanceToGoal(Point v, Point goal) {
            return 0;
        }

    }
}
