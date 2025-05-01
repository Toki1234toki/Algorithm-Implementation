import java.util.*;
import java.io.*;
public class DijkstraShortestPath {
   static class Edge {
       String target;
     int weight;
      Edge(String target, int weight) {
        this.target = target;
        this.weight = weight;
        }
    }
  static class Node implements Comparable<Node> {
    String vertex;
     int distance;
      Node(String vertex, int distance) {
      this.vertex = vertex;
     this.distance = distance;
        }
        public int compareTo(Node other) {
        			return Integer.compare(this.distance, other.distance);
        }
    }
    public static void main(String[] args) throws IOException {
        Map<String, List<Edge>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();
        Scanner fileScanner = new Scanner(new File("input.txt"));
        while (fileScanner.hasNext()) {
            String from = fileScanner.next();
            String to = fileScanner.next();
            int weight = fileScanner.nextInt();
            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(new Edge(to, weight));
            vertices.add(from);
            vertices.add(to);
        }
        fileScanner.close();
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("Enter the source vertex: ");
        String source = inputScanner.nextLine().trim();
        inputScanner.close();
        Map<String, Integer> distance = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (String vertex : vertices) {
            distance.put(vertex, Integer.MAX_VALUE);
        }
        distance.put(source, 0);
        pq.add(new Node(source, 0));
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (!graph.containsKey(current.vertex)) continue;
            for (Edge edge : graph.get(current.vertex)) {
                int newDist = distance.get(current.vertex) + edge.weight;
                if (newDist < distance.get(edge.target)) {
                    distance.put(edge.target, newDist);
                    previous.put(edge.target, current.vertex);
                    pq.add(new Node(edge.target, newDist));
                }
            }
        }
        for (String target : vertices) {
            if (target.equals(source)) continue;
            if (distance.get(target) == Integer.MAX_VALUE) {
                System.out.println("No path to " + target);
                continue;
            }
            List<String> path = new ArrayList<>();
            String current = target;
            while (current != null) {
                path.add(current);
                current = previous.get(current);
            }
            Collections.reverse(path);
            System.out.println(String.join(",", path) + " – " + distance.get(target));
        }
    }
}
