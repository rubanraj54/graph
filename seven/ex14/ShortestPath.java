package exercise.seven.ex14;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import exercise.seven.ex13.Edge;
import exercise.seven.ex13.Graph;
import exercise.seven.ex13.Node;

public class ShortestPath {

	public ArrayList<Node> findShortestPath(Graph graph, Node startNode, Node endNode) {

		createFile(fileName);

		List<Integer> visited = new ArrayList<Integer>();
		visited.add(startNode.getNodeName());
		List<Integer> currentMin = new ArrayList<Integer>();
		depthFirstSearch(startNode, endNode, visited, currentMin, graph);

		ArrayList<String> paths = readFile(fileName);

		removeContent(fileName);

		ArrayList<Node> traversalList = new ArrayList<>();

		String shortest_path = calculateShortestPath(paths, graph, startNode, endNode);
		for (String node : shortest_path.split(" ")) {
			traversalList.add(graph.getNode(Integer.parseInt(node)));
		}

		return traversalList;
	}

	private List<Integer> depthFirstSearch(Node current, Node goal, List<Integer> visited, List<Integer> currentMin,
			Graph graph) {

		for (Edge edge : current.getEdges()) {
			if (edge.getEdgeName() == goal.getNodeName()) {
				String result = "";
				for (int i : visited) {
					result += i + " ";
				}
				result += goal.getNodeName() + System.lineSeparator();

				writePath(result, fileName);
				return currentMin;
			}

			if (!visited.contains(edge.getEdgeName())) {
				visited.add(edge.getEdgeName());
				currentMin = depthFirstSearch(graph.getNode(edge.getEdgeName()), goal, visited, currentMin, graph);
				visited.remove(visited.size() - 1);
			}
		}
		return currentMin;

	}

	private String fileName = "shortest_paths";

	private void createFile(String fileName) {
		try {
			File file = new File(fileName + ".txt");
			if (file.createNewFile())
				System.out.println("File creation successfull");
		} catch (IOException io) {
			io.printStackTrace();
			System.out.println("Error in file creation");
		}
	}

	private void writePath(String path, String fileName) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName + ".txt", true));
			out.write(path);
			out.close();
		} catch (IOException e) {
			e.getMessage();
			System.out.println("File not found");
		}
	}

	private ArrayList<String> readFile(String fileName) {

		ArrayList<String> paths = new ArrayList<>();
		Scanner read;
		try {
			read = new Scanner(new File(fileName + ".txt"));
			while (read.hasNext()) {
				paths.add(read.nextLine());
			}
			read.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found");
		}
		return paths;
	}

	private void removeContent(String fileName) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName + ".txt"));
			out.write("");
			out.close();
		} catch (IOException e) {
			e.getMessage();
			System.out.println("File not found");
		}
	}

	private String calculateShortestPath(ArrayList<String> paths, Graph graph, Node startNode, Node endNode) {
		int shortest_distance = Integer.MAX_VALUE;
		String shortest_path = paths.get(0);
		int[] nodes;
		int length;

		for (String path : paths) {
			nodes = Stream.of(path.split(" ")).mapToInt(Integer::parseInt).toArray();
			length = 0;
			for (int i = 0; i < nodes.length - 1; i++) {
				length += graph.getNode(nodes[i]).getEdge(nodes[i + 1]).getWeight();
			}
			if (length < shortest_distance) {
				shortest_distance = length;
				shortest_path = path;
			}
		}

		System.out.println("The shortest path between " + startNode.getNodeName() + " and " + endNode.getNodeName()
				+ " is : " + shortest_path + " and the distance is " + shortest_distance);

		return shortest_path;
	}
}
