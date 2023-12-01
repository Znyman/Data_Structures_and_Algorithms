package zacheryNyman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class GasStation
{
	public static void main(String[] args) throws FileNotFoundException
	{
		String carName;
		String filename = "sample.txt";
		ArrayList<Car> minHeap = new ArrayList<Car>(100);
		int numPumps, arrivalTime, refuelPriority, time = 0;
		
		for(int i = 0; i < 4; i++)
		{
			if(i == 1)
			{
				time = 0;
				filename = "vehicle_3_25.txt";
			}
			if(i == 2)
			{
				time = 0;
				filename = "vehicle_5_25.txt";
			}
			if(i == 3)
			{
				time = 0;
				filename = "vehicle_5_100.txt";
			}
			
			Scanner inFile = new Scanner(new FileInputStream(filename));
			PrintWriter outFile = new PrintWriter(new FileOutputStream(filename + ".csv"));
			//outFile.println("Vehicle-ID,Arrival-Time,Refuel-Priority,Time-Processed,Pump-Number");
			
			numPumps = inFile.nextInt();
			
			carName = inFile.next();
			arrivalTime = inFile.nextInt();
			refuelPriority = inFile.nextInt();
			insert(minHeap, new Car(carName, arrivalTime, refuelPriority));
			
			while(!minHeap.isEmpty())
			{
				if(arrivalTime == time && !carName.equals(minHeap.get(0).getVID()))//if a car was unable to be added from the previous iteration
				{																//due to it not being received at the correct time, then it's processed here
					insert(minHeap, new Car(carName, arrivalTime, refuelPriority));
				}
				while(inFile.hasNext() && arrivalTime == time)//makes sure the cars that arrive at the same time are put in the queue in their correct position
				{
					carName = inFile.next();
					arrivalTime = inFile.nextInt();
					refuelPriority = inFile.nextInt();
					if(arrivalTime == time)
					{
						insert(minHeap, new Car(carName, arrivalTime, refuelPriority));
					}
				}
				//System.out.println("Before: " + minHeap);
				for(int j = 0; j < numPumps; j++)//processes cars through the gas station depending on number of pumps
				{
					if(!minHeap.isEmpty())
					{
						//System.err.println(extractMin(minHeap) + " " + time + " " + j);
						outFile.println(extractMin(minHeap) + "," + time);
					}
				}
				//System.out.println("After: " + minHeap);
				time++;
			}
			outFile.close();
			inFile.close();
		}
	}
	
	
	//Beginning of Minimum Priority Queue
	public static void insert(ArrayList<Car> A, Car key)
	{
		A.add(new Car("dummyCar", Integer.MAX_VALUE, Integer.MAX_VALUE));
		decreaseKey(A, A.size()-1, key);
		buildMinHeap(A);
	}
	private static void decreaseKey(ArrayList<Car> A, int i, Car key)
	{
		Car temp;
		if(key.getRFP() > A.get(i).getRFP())
		{
			System.err.println("New key is invalid.");
			System.exit(0);
		}
		A.set(i, key);
		while(i > 0 && A.get(parent(i)).getRFP() > A.get(i).getRFP())
		{
			temp = A.get(i);
			A.set(i, A.get(parent(i)));
			A.set(parent(i), temp);
			i = parent(i);
		}
	}
	public static Car minimum(ArrayList<Car> A)
	{
		return A.get(0);
	}
	public static Car extractMin(ArrayList<Car> A)
	{
		Car min;
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
	private static void minHeapify(ArrayList<Car> A, int i)
	{
		int leftChildIndex = left(i), rightChildIndex = right(i), smallest;
		
		if(leftChildIndex <= A.size()-1 && A.get(leftChildIndex).getRFP() < A.get(i).getRFP())//smallest priority gets set to smallest
		{
			smallest = leftChildIndex;
		}
		else if(leftChildIndex <= A.size()-1 && A.get(leftChildIndex).getRFP() == A.get(i).getRFP()
				&& A.get(leftChildIndex).getAT() < A.get(i).getAT())//if priorities are equal, then arrival time is compared
		{
			smallest = leftChildIndex;
		}
		else
		{
			smallest = i;
		}
		
		if(rightChildIndex <= A.size()-1 && A.get(rightChildIndex).getRFP() < A.get(smallest).getRFP())//smallest priority gets set to smallest
		{
			smallest = rightChildIndex;
		}
		else if(rightChildIndex <= A.size()-1 && A.get(rightChildIndex).getRFP() == A.get(smallest).getRFP()
				&& A.get(rightChildIndex).getAT() < A.get(smallest).getAT())//if priorities are equal, then refuel arrival time is compared
		{
			smallest = rightChildIndex;
		}
		
		if(smallest != i)
		{
			Car temp = A.get(i);
			A.set(i, A.get(smallest));
			A.set(smallest, temp);
			minHeapify(A, smallest);
		}
	}
	public static void buildMinHeap(ArrayList<Car> A)
	{
		for(int i = A.size()/2; i >= 0; i--)
		{
			minHeapify(A, i);
		}
	}
	//End of Heap Methods
	
	private static class Car
	{
		private String vehicleID;
		private int arrivalTime;
		private int refuelPriority;
		
		public Car(String vID, int arrTime, int rfPriority)
		{
			vehicleID = vID;
			arrivalTime = arrTime;
			refuelPriority = rfPriority;
		}
		
		public String getVID()
		{
			return vehicleID;
		}
		public int getAT()
		{
			return arrivalTime;
		}
		public int getRFP()
		{
			return refuelPriority;
		}
		public String toString()
		{
			return vehicleID + "," + arrivalTime;
		}
	}
	
	
	
	
	
	
	
}
