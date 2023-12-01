package zacheryNyman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class MST {

	public static void main(String[] args) throws FileNotFoundException
	{
		int vNum, vertexIndex, vertexNeighbor, edgeWeight;

		for(int k = 0; k < args.length; k += 2)
		{
			String filename = args[k];
			ArrayList<Vertex> path = new ArrayList<Vertex>();
			ArrayList<Vertex> vertices = new ArrayList<Vertex>();
			Scanner inFile = new Scanner(new FileInputStream(filename));

			vNum = inFile.nextInt();

			for(int i = 0; i < vNum; i++)
			{
				vertices.add(new Vertex(i));
			}

			while(inFile.hasNext())
			{
				vertexIndex = inFile.nextInt();
				vertexNeighbor = inFile.nextInt();
				edgeWeight = inFile.nextInt();
				vertices.get(vertexIndex).addNeighbor(vertices.get(vertexNeighbor));
				vertices.get(vertexNeighbor).addNeighbor(vertices.get(vertexIndex));
				vertices.get(vertexIndex).addEdge(new Edge(vertices.get(vertexIndex), vertices.get(vertexNeighbor), edgeWeight));
				vertices.get(vertexNeighbor).addEdge(new Edge(vertices.get(vertexNeighbor), vertices.get(vertexIndex), edgeWeight));
			}

			int sourceIndex = Integer.parseInt(args[k+1]);
			Vertex source = vertices.get(sourceIndex);

			Prim(vertices, source);
			
			int total = 0;
			for(Vertex v : vertices)
			{
				System.out.println(v);
				total += v.getKey();
			}
			System.out.println(total);
			inFile.close();
		}
	}

	public static void Prim(ArrayList<Vertex> G, Vertex r)
	{
		for(Vertex u : G)
		{
			u.setKey(Integer.MAX_VALUE);
			u.setParent(new Vertex(-1));
		}
		r.setKey(0);
		ArrayList<Vertex> Q = new ArrayList<Vertex>(G);
		buildMinHeap(Q);
		while(!Q.isEmpty())
		{
			//System.err.println("Before: "+ Q);
			Vertex u = extractMin(Q);
			//System.err.println("After: " + Q);
			for(Vertex v : u.getNeighbors())
			{
				if(Q.contains(v) && weight(u, v) < v.getKey())
				{
					v.setParent(u);
					v.setKey(weight(u, v));
				}
			}
		}
	}

	public static int weight(Vertex u, Vertex v)
	{
		for(Edge e : u.getEdges())
		{
			if(e.getV().equals(v))
			{
				return e.getWeight();
			}
		}
		return 0;
	}

	//Beginning of Minimum Priority Queue
	public static Vertex extractMin(ArrayList<Vertex> A)
	{
		buildMinHeap(A);
		Vertex min;
		if(A.size() < 1)
		{
			System.err.println("Heap underflow.");
			System.exit(0);
		}
		min = A.get(0);
		if(A.size() > 1)
		{
			A.set(0, A.remove(A.size()-1));
		}
		else
		{
			min = A.remove(0);
		}
		minHeapify(A, 0);
		return min;
	}
	//End of Minimum Priority Queue

	//Beginning of Heap Methods
	private static int parent(int i)
	{
		return i/2;
	}
	private static int left(int i)
	{
		return 2*i+1;
	}
	private static int right(int i)
	{
		return 2*i+2;
	}
	private static void minHeapify(ArrayList<Vertex> A, int i)
	{
		int leftChildIndex = left(i), rightChildIndex = right(i), smallest;

		if(leftChildIndex <= A.size()-1 && A.get(leftChildIndex).getKey() < A.get(i).getKey())
		{
			smallest = leftChildIndex;
		}
		else
		{
			smallest = i;
		}

		if(rightChildIndex <= A.size()-1 && A.get(rightChildIndex).getKey() < A.get(smallest).getKey())
		{
			smallest = rightChildIndex;
		}
		
		if(smallest != i)
		{
			Vertex temp = A.get(i);
			A.set(i, A.get(smallest));
			A.set(smallest, temp);
			minHeapify(A, smallest);
		}
	}
	public static void buildMinHeap(ArrayList<Vertex> A)
	{
		for(int i = A.size()/2; i >= 0; i--)
		{
			minHeapify(A, i);
		}
	}
	//End of Heap Methods

	private static class Vertex
	{
		private Vertex parent;
		private int vNum, key;
		private ArrayList<Vertex> neighbors = new ArrayList<Vertex>();
		private ArrayList<Edge> edges = new ArrayList<Edge>();

		public Vertex(int newVNum)
		{
			vNum = newVNum;
		}
		//setters
		public void setKey(int newKey)
		{
			key = newKey;
		}
		public void setParent(Vertex p)
		{
			parent = p;
		}
		public void addNeighbor(Vertex newNeighbor)
		{
			neighbors.add(newNeighbor);
		}
		public void addEdge(Edge newEdge)
		{
			edges.add(newEdge);
		}
		//getters
		public int getKey()
		{
			return key;
		}
		public int getVNum()
		{
			return vNum;
		}
		public ArrayList<Vertex> getNeighbors()
		{
			return neighbors;
		}
		public Vertex getParent()
		{
			return parent;
		}
		public ArrayList<Edge> getEdges()
		{
			return edges;
		}

		public String toString()
		{
			return vNum + " " + key + " " + parent.getVNum();
		}
	}

	private static class Edge
	{
		private Vertex u, v;
		private int weight;

		public Edge(Vertex newU, Vertex newV, int newWeight)
		{
			u = newU;
			v = newV;
			weight = newWeight;
		}

		public int getWeight()
		{
			return weight;
		}
		public Vertex getU()
		{
			return u;
		}
		public Vertex getV()
		{
			return v;
		}
	}
}
