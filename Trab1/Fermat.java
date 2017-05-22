import java.math.BigInteger;
import java.util.Scanner;
import java.security.SecureRandom;

public class Fermat{
	public static void main(String[] args) {
		/*
		 * n = número a ser testado se é primo
		 * k = quantidade de iterações
		 */
		Scanner io = new Scanner(System.in);
		System.out.println("Informe o número primo");
		String aux = io.next();
		System.out.println("Informe a quantidade de iterações");
		int k = io.nextInt();
		BigInteger n = new BigInteger(aux);
		int nbits = n.bitLength()+1;
		if(fermat(n, k, nbits)) {
			System.out.println("É primo");
		} else {
			System.out.println("Não é primo");
		}
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
}