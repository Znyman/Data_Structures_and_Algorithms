package zacheryNyman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class RandomizedQuicksort
{
	public static void main(String[] args) throws FileNotFoundException
	{
		String filename = args[0];
		ArrayList<String> A = new ArrayList<String>();
		Scanner inFile = new Scanner(new FileInputStream(filename));
		PrintWriter outFile = new PrintWriter(new FileOutputStream("output.txt"));
		
		while(inFile.hasNext())
		{
			A.add(inFile.nextLine());
		}
		inFile.close();
		
		randomizedQuicksort(A, 0, A.size()-1);
		
		for(String s : A)
		{
			outFile.println(s);
			System.out.println(s);
		}
		outFile.close();
	}
	
	public static int partition(ArrayList<String> A, int low, int high)
	{
		String pivot = A.get(high);
		int split = low-1;
		for(int j = low; j < high; j++)
		{
			if(A.get(j).equals(pivot) || A.get(j).compareTo(pivot) < 0)
			{
				split++;
				Collections.swap(A, split, j);
			}
		}
		Collections.swap(A, split+1, high);
		return split+1;
	}
	
	public static int randomizedPartition(ArrayList<String> A, int low, int high)
	{
		int i = ThreadLocalRandom.current().nextInt(low, high+1);
		Collections.swap(A, high, i);
		return partition(A, low, high);
	}
	
	public static void randomizedQuicksort(ArrayList<String> A, int low, int high)
	{
		if(low < high)
		{
			int split = randomizedPartition(A, low, high);
			randomizedQuicksort(A, low, split-1);
			randomizedQuicksort(A, split+1, high);
		}
	}
}