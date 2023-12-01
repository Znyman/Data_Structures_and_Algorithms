package zacheryNyman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Multiplication
{
	public static void main(String args[]) throws FileNotFoundException
	{
		
		double bruteForceTime, DACTime;
		Scanner integerLineInt;
		int size, filenameCounter = 2;
		String filename, integerLine, newLineChar;
		PrintWriter outFile = new PrintWriter(new FileOutputStream("output.csv"));
		
		for(int i = 0; i < 9; i++)
		{
			filenameCounter *= 2;
			filename = "p4_" + filenameCounter + ".txt";
			Scanner inFile = new Scanner(new FileInputStream(filename));
			
			size = inFile.nextInt();
			ArrayList<Integer> bruteU = new ArrayList<Integer>(size);
			ArrayList<Integer> bruteV = new ArrayList<Integer>(size);
			ArrayList<Integer> bruteResult = new ArrayList<Integer>(size*2);
			int[] DACU = new int[size];
			int[] DACV = new int[size];
			int[] DACResult = new int[size*2];
			
			newLineChar = inFile.nextLine();
			integerLine = inFile.nextLine();
			integerLineInt = new Scanner(integerLine);
			
			int j = DACU.length-1;
			while(integerLineInt.hasNextInt())
			{
				int value = integerLineInt.nextInt();
				bruteU.add(0, value);
				DACU[j] = value;
				j--;
			}
			
			integerLine = inFile.nextLine();
			integerLineInt = new Scanner(integerLine);
			
			int k = DACV.length-1;
			while(integerLineInt.hasNextInt())
			{
				int value = integerLineInt.nextInt();
				bruteV.add(0, value);
				DACV[k] = value;
				k--;
			}
			
			CpuTimer bruteForceTimer = new CpuTimer();
			bruteResult = bruteForceMultiply(bruteU, bruteV);
			bruteForceTime = bruteForceTimer.getElapsedCpuTime();
			integerLine = resultToString(bruteResult);
			
			CpuTimer DACTimer = new CpuTimer();
			DACResult = DACMultiply(DACU, DACV);
			DACTime = DACTimer.getElapsedCpuTime();
			
			outFile.println("n,Algorithm,CPU-Seconds,Result");
			outFile.println(filenameCounter + "," + "M," + bruteForceTime + "," + integerLine);
			
			outFile.print(filenameCounter + "," + "R," + DACTime + ",");
			for(int p = DACResult.length - 1; p >= 0; p--)
			{
				if(p == 0)
				{
					outFile.print(DACResult[p] + "\"");
				}
				else if(p == DACResult.length - 1)
				{
					outFile.print("\"" + DACResult[p] + " ");
				}
				else
				{
					outFile.print(DACResult[p] + " ");
				}
				
			}
			outFile.println();
			inFile.close();
		}
		outFile.close();
	}
	
	public static ArrayList<Integer> bruteForceMultiply(ArrayList<Integer> U, ArrayList<Integer> V)
	{
		int c, t, k, p = 0, n = U.size();
		ArrayList<Integer> W = new ArrayList<Integer>(n*2);
		for(int i = 0; i < n*2; i++)
		{
			W.add(0);
		}
		for(int j = 0; j < n; j++)
		{
			c = 0;
			for(int i = 0; i < n; i++)
			{
				t = U.get(i)*V.get(j) + W.get(i+j) + c;
				W.set(i+j, t % 256);
				c = t/256;
				p = i+1;
			}
			k = p+j;
			while(k <= 2*n && c != 0)
			{
				t = W.get(k) + c;
				W.set(k, t % 256);
				c = t/256;
				k++;
			}
		}
		return W;
	}
	
	public static int[] DACMultiply(int[] U, int[] V)
	{
		return multiplyRecursive(U, V);
	}
	
	private static int[] multiplyRecursive(int[] U, int[] V)
	{
		int USign, VSign;
		assert U.length == V.length;
		int n = U.length;
		if(n == 1)
		{
			int[] product = new int[n*2];
			product[1] = U[0]*V[0];
			product[0] = product[1] % 256;
			product[1] = product[1] / 256;
			return product;
		}
		int[] W = new int[n*2];
		int[] U1 = new int[n/2];
		int[] U2 = new int[n/2];
		int[] V1 = new int[n/2];
		int[] V2 = new int[n/2];
		int[] UDiff = new int[n/2];
		int[] VDiff = new int[n/2];
		for(int k = 0; k < n/2; k++)
		{
			U1[k] = U[k];
			UDiff[k] = U[k+n/2];
			U2[k] = U[k+n/2];
			VDiff[k] = V[k];
			V1[k] = V[k];
			V2[k] = V[k+n/2];
			W[k+n] = 0;
			W[k+n+n/2] = 0;
		}
		USign = shiftedSubtractFrom(UDiff, U1, 0);
		VSign = shiftedSubtractFrom(VDiff, V2, 0);
		int[] W1 = new int[n];
		int[] W2 = new int[n];
		int[] W3 = new int[n];
		W1 = multiplyRecursive(U2, V2);
		W2 = multiplyRecursive(UDiff, VDiff);
		W3 = multiplyRecursive(U1, V1);
		for(int i = 0; i < n; i++)
		{
			W[i] = W3[i];
		}
		shiftedAddTo(W, W3, n/2);
		shiftedAddTo(W, W1, n/2);
		shiftedAddTo(W, W1, n);
		if(USign == 0 && USign == VSign || USign != 0 && VSign != 0)
		{
			shiftedAddTo(W, W2, n/2);
		}
		else
		{
			shiftedSubtractFrom(W, W2, n/2);
		}
		return W;
	}
	
	private static void shiftedAddTo(int[] A, int[] B, int k)
	{
		int i, t, c = 0;
		for(i = 0; i < B.length; i++)
		{
			t = A[i+k] + B[i] + c;
			A[i+k] = t % 256;
			c = t/256;
		}
		while(c != 0 && i+k < A.length)
		{
			t = A[i+k] + c;
			A[i+k] = t % 256;
			c = t/256;
			i++;
		}
		assert c == 0;
		return;
	}
	
	private static int shiftedSubtractFrom(int[] A, int[] B, int k)
	{
		int t, borrow = 0;
		int[] C = new int[A.length];
		for(int i = 0; i < B.length; i++)
		{
			C[i] = B[i];
		}
		for(int i = 0; i + k < C.length; i++)
		{
			t = A[i+k] - C[i] - borrow;
			if(t >= 0)
			{
				A[i+k] = t;
				borrow = 0;
			}
			else
			{
				A[i+k] = t + 256;
				borrow = 1;
			}
		}
		
		/*
		i -= 1;
		while(borrow != 0 && i+k <= A.length)
		{
			if(A[i+k] != 0)
			{
				A[i+k] = 0;
				borrow = 0;
			}
			else
			{
				A[i+k] = 1;
			}
			i++;
		}*/
		if(borrow != 0)
		{
			Complement(A);
		}
		return borrow;
	}
	
	private static void Complement(int[] A)
	{
		int c, t;
		t = 256 - A[0];
		A[0] = t % 256;
		c = t/256;
		for(int i = 1; i < A.length; i++)
		{
			t = 255 - A[i] + c;
			A[i] = t % 256;
			c = t/256;
		}
	}
	
	public static String resultToString(ArrayList<Integer> input)
	{
		String result = "";
		Collections.reverse(input);
		result += "\"";
		for(int i = 0; i < input.size(); i++)
		{
			result += input.get(i);
			if(i < input.size()-1)
			{
				result += " ";
			}
		}
		result += "\"";
		return result;
	}
	
	
	
	
	
	
	
	
	
	
}
