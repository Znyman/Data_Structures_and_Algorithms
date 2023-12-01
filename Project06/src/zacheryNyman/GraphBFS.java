package zacheryNyman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GraphBFS {

	public static void main(String[] args) throws FileNotFoundException
	{
		int vNum, vertexIndex, vertexEdge;
		
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
				if(i == 0)
				{
					vertices.get(0).setParent(new Vertex(-1));
				}
			}
			
			while(inFile.hasNext())
			{
				vertexIndex = inFile.nextInt();
				vertexEdge = inFile.nextInt();
				vertices.get(vertexIndex).addEdge(vertices.get(vertexEdge));
				vertices.get(vertexEdge).addEdge(vertices.get(vertexIndex));
			}
			
			int sourceIndex = Integer.parseInt(args[k+1]);
			Vertex source = vertices.get(sourceIndex);
			
			BFS(vertices, source);
			
			for(int i = 0; i < vNum; i++)
			{
				System.out.print(i + " " + vertices.get(i).getDistance() + " ");
				getPath(vertices.get(i), source, path);
				Collections.reverse(path);
				for(int j = 0; j < path.size(); j++)
				{
					if(j < path.size()-1)
					{
						System.out.print(path.get(j) + "-");
					}
					else
					{
						System.out.print(path.get(j));
					}
				}
				System.out.println();
				path.clear();
			}
			inFile.close();
		}
	}
	
	public static Vertex getPath(Vertex v, Vertex thisSource, ArrayList<Vertex> thisPath)
	{
		if(v.equals(thisSource))//base case
		{
			thisPath.add(v);
			return v;
		}
		else if(!thisPath.contains(v))//ensures no duplicates
		{
			thisPath.add(v);
		}
		else if(!thisPath.contains(v.getParent()))//ensures no duplicates
		{
			thisPath.add(v.getParent());
		}
		return getPath(v.getParent(), thisSource, thisPath);
	}

	//Beginning of BFS
	public static void BFS(ArrayList<Vertex> G, Vertex s)
	{
		for(Vertex u : G)
		{
			if(!u.equals(s))
			{
				u.setDistance(-1);
				u.setParent(null);
			}
		}
		s.setDistance(0);
		s.setParent(null);
		ArrayList<Vertex> Q = new ArrayList<Vertex>();
		enqueue(Q, s);
		while(!Q.isEmpty())
		{
			Vertex u = dequeue(Q);
			for(Vertex v : u.getEdges())
			{
				if(v.getDistance() < 0)
				{
					v.setDistance(u.getDistance()+1);
					v.setParent(u);
					enqueue(Q, v);
				}
			}
		}
	}
	//End of BFS
	
	//Beginning of Queue
	private static void enqueue(ArrayList<Vertex> Q, Vertex s)
	{
		Q.add(s);
	}
	private static Vertex dequeue(ArrayList<Vertex> Q)
	{
		return Q.remove(0);
	}
	//End of Queue

	private static class Vertex
	{
		Vertex parent;
		private int key, distance;
		private ArrayList<Vertex> edges = new ArrayList<Vertex>();

		public Vertex(int newKey)
		{
			key = newKey;
		}
		
		public void setDistance(int d)
		{
			distance = d;
		}
		public void setParent(Vertex p)
		{
			parent = p;
		}
		public void addEdge(Vertex newEdge)
		{
			edges.add(newEdge);
		}
		
		public int getDistance()
		{
			return distance;
		}
		public int getKey()
		{
			return key;
		}
		public ArrayList<Vertex> getEdges()
		{
			return edges;
		}
		public Vertex getParent()
		{
			return parent;
		}
		
		public String toString()
		{
			return key + "";
		}
	}
}
