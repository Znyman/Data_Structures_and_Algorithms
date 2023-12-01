package zacheryNyman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class InsertionSort
{
	
	public static void main(String[] args)
	{
		String filename = "ExampleInput.txt";
		Scanner inFile = null;
        try
        {
            inFile = new Scanner(new FileInputStream(filename));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Cannot open file: " + filename);
            System.exit(0);
        }
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		while(inFile.hasNextInt())
		{
			list.add(inFile.nextInt());
		}
		
		insertionSort(list);
		
		for(int i = 0; i < list.size(); i++)
		{
			System.out.println(list.get(i));
		}
	}
	
	public static ArrayList<Integer> insertionSort(ArrayList<Integer> input)
	{
		int key;
		int j;
		for(int i = 1; i < input.size(); i++)
		{
			key = input.get(i);
			j = i-1;
			
			while(j >= 0 && input.get(j) > key)
			{
				input.set(j+1, input.get(j));
				j = j-1;
			}
			
			input.set(j+1, key);
		}
		return input;
	}
}
