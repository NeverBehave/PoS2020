package hw7;

import hw4.Edge;
import hw4.Graph;
import hw5.PathAlgorithmWithNumber;

import java.util.*;

public class CampusModel {

    PathAlgorithmWithNumber<Building, Double> p = new PathAlgorithmWithNumber<>();
    Graph<Building, Double> g = new Graph<>();
    Map<Integer, Building> idMap = new HashMap<>();
    Map<String, Building> nameMap = new HashMap<>();

    /**
     * Read in data from given filename and parse it into algorithm optimized
     * form. It depends on what algorithm is used in findPath method
     *
     * @param buildingFile CSV file that contains buildings' info
     * @param connectionFile CSV file that contains connections between buildings
     * @effects g Graph will be filled with given file data 's relationship
     */
    public void createNewGraph(String buildingFile, String connectionFile) {
        Set<Building> buildingList = new HashSet<>();
        List<Map.Entry<Integer, Integer>> connections = new LinkedList<>();
        try {
            CampusParser.readBuildings(buildingFile, buildingList);
            CampusParser.readBuildingConnections(connectionFile, connections);
        } catch (Exception e) {
            System.out.println("Error when reading data from file: ");
            e.printStackTrace();
        } finally {
            g = new Graph<>();
            // Use ID to fast track buildings
            for (Building b : buildingList) {
                g.addNode(b);
                idMap.put(b.getId(), b);
                nameMap.put(b.getName(), b);
            }

            // Build Relationship
            for (Map.Entry<Integer, Integer> i : connections) {
                Building a = idMap.get(i.getKey());
                Building b = idMap.get(i.getValue());
                Double distance = a.calculateDistance(b);
                g.connect(a, b, distance);
                g.connect(b, a, distance);
            }

            p.setGraph(g);
        }
    }

    /**
     * FindPath based on given algorithm implemented in this function
     *
     * @param node1 Start node
     * @param node2 End node
     * @return String Indicate the path that start from node1 and end with node2
     * if node 1 and/or node2 has no found in the graph, unknown character will be returned
     * if there is no path between these two character, null will be returned
     */
    public Map<Building, List<Edge<Building, Double>>> findPath(Building node1, Building node2) {
        Map<Building, List<Edge<Building, Double>>> result = new HashMap<>();

        if (p.checkNode(node1, node2) == 0) {
            result = p.findDijkstra(node1, node2);
        }

        return result;
    }

    public int checkNode(Building a, Building b) {
        return p.checkNode(a, b);
    }

    public Building findBuildingByName(String name) {
            Building b = nameMap.get(name);
            return b.isIntersection() ? null : b;
    }

    public Building findBuildingById(Integer id) {
        Building b = idMap.get(id);
        return b.isIntersection() ? null : b;
    }

    public Set<Building> buildingSet() {
        return g.getNodes();
    }

//    @Override
//    public String toString() {
//        StringBuilder b = new StringBuilder();
//        for (Building bu : g.getNodes()) {
//           for (Edge<Building, Double> e : g.connectedEdge(bu)) {
//               b // .append(e.getFrom().getId())
//                       .append("(") .append(e.getFrom().getName()).append(")")
//                       .append(String.format(" %.3f ", e.getName()))
//                       //.append(e.getTo().getId())
//                       .append("(") .append(e.getTo().getName()).append(")")
//                       .append("\n");
//           }
//        }
//        return b.toString();
//    }
}