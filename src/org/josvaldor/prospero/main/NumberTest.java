package org.josvaldor.prospero.main;

public class NumberTest {
	public static void main(String[] args) {
		double number = 109.0125;
		double remainder = 0;
		int numberInt = (int)number;
		remainder = number%numberInt;
		System.out.println(remainder);
		if(remainder < 0.01) {
			System.out.println("true");
		}
	}
}
