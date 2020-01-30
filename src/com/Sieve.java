package com;

public class Sieve {
	static int N;
	static boolean [] b;
	Sieve(){
		N = 1000;
		b = new boolean[N];
	}
	
	
	public static void sieveOfEratosthenes(int N) {
		int n = 1000000;
		int [] a = new int[n];
		for (int j = 0; j < n; j++)
			a[j] = 1;
		a[0]= 0; a[1]=0; a[2]= 1;
		for (int i = 2; i < n; i++) {
			if(a[i] == 0) continue;
			for (int j = i*2, k = 3; j < n; j = i*k,k++) {
				a[j] = 0;
			}
		}
		int count = 0;
		for (int j = 0; j < n; j++) {
			if (a[j] == 1) {
				count++;
			}
		}
		System.out.println("count = " + count);
	}
	public static void main (String args[]) {
		
		System.out.println("Sieves");
		sieveOfEratosthenes(N);
	}
	
	
	
	

}
