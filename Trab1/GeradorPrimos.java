import java.math.BigInteger;
import java.util.Scanner;
import java.security.SecureRandom;
import java.util.Random;

public class GeradorPrimos{
	public static void main(String[] args) {
		/*
		 * n = número a ser testado se é primo
		 * k = quantidade de iterações
		 */
		Scanner io = new Scanner(System.in);
		System.out.println("Informe a quantidade de bits");
		int nbits = io.nextInt();
		int k = 20;

		BigInteger x;

		do {
			x = new BigInteger(nbits, new Random());
			do {
				x = mix(x, nbits);
			} while(x.bitLength() != nbits);
		} while(!fermat(x, k, nbits));
		System.out.println(x);
	}

	private static boolean fermat(BigInteger n, int k, int nbits) {
		if(n == BigInteger.ONE) {
			return false;
		}
		for(int i = 0; i < k; i++) {
			/* 
			 * n1 = n-1
			 * a = rand % (n-1) + 1
			 */
			BigInteger n1 = n.subtract(BigInteger.ONE);
			SecureRandom rnd = new SecureRandom(); // gera um número aleatório
			BigInteger a = new BigInteger(nbits, rnd);
			a = a.remainder(n1).add(BigInteger.ONE);
			// if (a^b) mod c = (a^n-1) mod n
			BigInteger x = a.modPow(n1, n);
			if(!x.equals(BigInteger.ONE)) {
				return false;
			}
		}
		return true;
	}

	public static BigInteger mix(BigInteger x, int nbits) {
		BigInteger dois = new BigInteger("2");
		BigInteger m = dois.pow(nbits).subtract(BigInteger.ONE);
		x = x.xor(x.shiftRight(29)).mod(m);
		x = x.xor(x.shiftLeft(37)).mod(m);
		x = x.xor(x.shiftRight(13)).mod(m);
		return x;
	}
}