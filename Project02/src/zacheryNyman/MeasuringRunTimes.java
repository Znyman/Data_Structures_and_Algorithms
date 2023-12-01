package zacheryNyman;

import java.util.ArrayList;
import java.util.Random;

public class MeasuringRunTimes
{
	public static void main(String args[])
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		double iter, totalInsertionTime, totalMergeTime, avgInsertionTime, avgMergeTime;
		
		for(int n = 8; n <= 8192; n += n)
		{
			list = randomArray(n);
			iter = Math.max(4, 8192/n);
			
			CpuTimer insertionTimer = new CpuTimer();
			for(int i = 0; i < iter; i++)
			{
				insertionSort(list);
			}
			totalInsertionTime = insertionTimer.getElapsedCpuTime();
			avgInsertionTime = totalInsertionTime/iter;
			
			if(!isSorted(insertionSort(list)))
			{
				System.err.println("Insertion sort didn't sort the list.");
				System.exit(0);
			}
			
			CpuTimer mergeTimer = new CpuTimer();
			for(int i = 0; i < iter; i++)
			{
				mergeSort(list);
			}
			totalMergeTime = mergeTimer.getElapsedCpuTime();
			avgMergeTime = totalMergeTime/iter;
			
			if(!isSorted(mergeSort(list)))
			{
				System.err.println("Merge sort didn't sort the list.");
				System.exit(0);
			}
			
			System.out.println("Avg. times for n = " + n + ": " + "Insertion Sort " + avgInsertionTime + " sec., "
					+ "Merge Sort " + avgMergeTime + " sec.");
		}
	}
	
	public static boolean isSorted(ArrayList<Integer> input)
	{
		if(input.isEmpty())
		{
			System.err.println("The array list is empty.");
		}
		else
		{
			for(int i = 0; i < input.size()-1; i++)
			{
				if(input.get(i) > input.get(i+1))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public static ArrayList<Integer> insertionSort(ArrayList<Integer> input)
	{
		ArrayList<Integer> sortedList = new ArrayList<Integer>(input);
		int key;
		int j;
		for(int i = 1; i < sortedList.size(); i++)
		{
			key = sortedList.get(i);
			j = i-1;
			
			while(j >= 0 && sortedList.get(j) > key)
			{
				sortedList.set(j+1, sortedList.get(j));
				j = j-1;
			}
			
			sortedList.set(j+1, key);
		}
		return sortedList;
	}
	
	public static ArrayList<Integer> randomArray(int n)
	{
		ArrayList<Integer> output = new ArrayList<Integer>(n);
		Random randomNumber = new Random();
		
		for(int i = 0; i < n; i++)
		{
			output.add(randomNumber.nextInt(10000));
		}
		return output;
	}
	
	public static ArrayList<Integer> mergeSort(ArrayList<Integer> input)
	{
		ArrayList<Integer> sortedList = new ArrayList<Integer>(input);
		mergeSortRecursive(sortedList, 0, input.size()-1);
		return sortedList;
	}
	
	private static void mergeSortRecursive(ArrayList<Integer> sortedList, int beginning, int end)
	{
		if(beginning < end)
		{
			int middle = (beginning + end)/2;
			mergeSortRecursive(sortedList, beginning, middle);
			mergeSortRecursive(sortedList, middle+1, end);
			merge(sortedList, beginning, middle, end);
		}
	}
	
	private static void merge(ArrayList<Integer> sortedList, int beginning, int middle, int end)
	{
		int n1 = middle - beginning +1;
		int n2 = end - middle;
		ArrayList<Integer> Left = new ArrayList<Integer>();
		ArrayList<Integer> Right = new ArrayList<Integer>();
		
		for(int i = 0; i < n1; i++)
		{
			Left.add(sortedList.get(beginning+i));
		}
		for(int j = 0; j < n2; j++)
		{
			Right.add(sortedList.get(middle+j+1));
		}
		
		Left.add(Integer.MAX_VALUE);
		Right.add(Integer.MAX_VALUE);
		
		int i = 0;
		int j =0;
		
		for(int k = beginning; k <= end; k++)
		{
			if(!Left.isEmpty() && !Right.isEmpty())
			{
				if(Left.get(i) <= Right.get(j))
				{
					sortedList.set(k, Left.get(i));
					i++;
				}
				else
				{
					sortedList.set(k, Right.get(j));
					j++;
				}
			}
			else if(Left.isEmpty())
			{
				sortedList.set(k, Right.get(j));
				j++;
			}
			else if(Right.isEmpty())
			{
				sortedList.set(k, Left.get(i));
				i++;
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
