package zacheryNyman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MaximumSubarray
{
	public static void main(String[] args) throws FileNotFoundException
	{
		int iter = 256;
		double maxSubarrayTime;
		String filename = "Data_8.txt";
		ArrayList<Integer> array =  new ArrayList<Integer>();
		PrintWriter outFile = new PrintWriter(new FileOutputStream("output.csv"));
		
		for(int i = 0; i < 6; i++)
		{
			if(i == 1)
			{
				filename = "Data_16.txt";
			}
			if(i == 2)
			{
				filename = "Data_64.txt";
			}
			if(i == 3)
			{
				filename = "Data_1024.txt";
			}
			if(i == 4)
			{
				filename = "Data_4096.txt";
			}
			if(i == 5)
			{
				filename = "Data_65536.txt";
			}
			
			Scanner inFile = new Scanner(new FileInputStream(filename));
			while(inFile.hasNextInt())
			{
				array.add(inFile.nextInt());
			}
			inFile.close();
			
			CpuTimer BFTimer = new CpuTimer();
			if(array.size() <= 1024)
			{
				for(int j = 0; j < iter; j++)
				{
					bruteForceMaxSubarray(array);
				}
				maxSubarrayTime = BFTimer.getElapsedCpuTime()/iter;
			}
			else
			{
				bruteForceMaxSubarray(array);
				maxSubarrayTime = BFTimer.getElapsedCpuTime();
			}
			outFile.println(array.size() + ",BF," + maxSubarrayTime);
			
			CpuTimer DACTimer = new CpuTimer();
			for(int j = 0; j < iter; j++)
			{
				divideAndConquerMaxSubarray(array);
			}
			maxSubarrayTime = DACTimer.getElapsedCpuTime()/iter;
			outFile.println(array.size() + ",DAC," + maxSubarrayTime);
			
			CpuTimer KADTimer = new CpuTimer();
			for(int j = 0; j < iter*2; j++)
			{
				kadaneMaxSubarray(array);
			}
			maxSubarrayTime = KADTimer.getElapsedCpuTime()/iter;
			outFile.println(array.size() + ",KAD," + maxSubarrayTime);
			
			array.clear();
		}
		outFile.close();
	}
	
	public static int bruteForceMaxSubarray(ArrayList<Integer> array)
	{
		int subarraySum, maxSubarraySum = 0, leftIndex = 0, rightIndex = 0;
		for(int i = 0; i < array.size(); i++)
		{
			subarraySum = array.get(i);
			for(int j = i+1; j < array.size(); j++)
			{
				subarraySum += array.get(j);
				if(subarraySum > maxSubarraySum)
				{
					maxSubarraySum = subarraySum;
					leftIndex = i;
					rightIndex = j;
				}
			}
		}
		System.err.println("BF: " + leftIndex + ", " + rightIndex + ", " + maxSubarraySum);
		return maxSubarraySum;
	}
	
	public static void divideAndConquerMaxSubarray(ArrayList<Integer> array)
	{
		MaxSubarrayRecursive(array, 0, array.size()-1);
	}
	private static void MaxSubarrayRecursive(ArrayList<Integer> array, int beginning, int end)
	{
		if(beginning < end)
		{
			int middle = (beginning + end)/2;
			MaxSubarrayRecursive(array, beginning, middle);
			MaxSubarrayRecursive(array, middle+1, end);
			findMaxSubarray(array, beginning, middle, end);
		}
	}
	
	private static void findMaxSubarray(ArrayList<Integer> array, int low, int middle, int high)
	{
		int leftSum = 0, rightSum = 0, totalSum, sum = 0, leftIndex = 0, rightIndex = 0;
		
		for(int i = middle; i >= low; i--)
		{
			sum += array.get(i);
			if(sum > leftSum)
			{
				leftSum = sum;
				leftIndex = i;
			}
		}
		
		sum = 0;
		for(int i = middle+1; i <= high; i++)
		{
			sum += array.get(i);
			if(sum > rightSum)
			{
				rightSum = sum;
				rightIndex = i;
			}
		}
		totalSum = leftSum + rightSum;
		//System.err.println("DAC" + leftIndex + ", " + rightIndex + ", " + totalSum);
	}
	
	public static int kadaneMaxSubarray(ArrayList<Integer> array)
	{
		int maxSumSoFar = 0, maxSumTok = 0;
		for(int k = 0; k < array.size(); k++)
		{
			maxSumTok = maxSumTok + array.get(k);
			if(maxSumTok < 0)
			{
				maxSumTok = 0;
			}
			if(maxSumSoFar < maxSumTok)
			{
				maxSumSoFar = maxSumTok;
			}
		}
		return maxSumSoFar;
	}
}
