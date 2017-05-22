import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class LinearCongruential{

	

	public static void main(String[] args) {
		BigInteger seed, a, m, c;
		Scanner io = new Scanner(System.in);
		System.out.println("Informe a quantidade de bits");
		int nbits = io.nextInt();
		BigInteger dois = new BigInteger("2");
		m = dois.pow(nbits);
		// a-1 precisa ser relativamente primo a m
		a = m.divide(new BigInteger("8")).add(BigInteger.ONE); 
		c = m.divide(dois).add(BigInteger.ONE); // c precisa ser relativamente primo a m
		c = BigInteger.ZERO;
		seed = new BigInteger(Long.toString(System.nanoTime()));
		do{
			seed = generator(a, m, c, seed);
		} while(seed.bitLength() != nbits);
		System.out.println(seed);
	}

	public static BigInteger generator(BigInteger a, BigInteger m, BigInteger c, BigInteger seed) {
		return seed.multiply(a).add(c).mod(m);
	}

}