import java.math.BigInteger;
import java.util.Scanner;
import java.util.Random;

public class MillerRabin{
	public static void main(String[] args) {
		Scanner io = new Scanner(System.in);
		/*
		 * n = primo testado
		 * n1 = n-1
		 */
		BigInteger n, n1;
		/*
		 * r = quantidade de vezes que n-1 é divisível por 2
		 * k = quantidade de vezes que o laço principal é executado, alterando
		 * a precisão do algoritmo
		 */
		int k, r, nbits;
		// aux = string auxiliar
		System.out.println("Informe o número primo");
		String aux = io.next();
		System.out.println("Informe a quantidade de rodadas");
		k = io.nextInt();
		n = new BigInteger(aux);
		n1 = n.subtract(BigInteger.ONE); 
		nbits = n.bitLength()+1;
		r = modulo(n1);
		boolean verify = miller(n, n1, r, k, nbits);
		if(verify) {
			System.out.println("É primo");
		} else {
			System.out.println("Não é primo");
		}
	}
	// compareTo, n > n1 = 1
	// compareTo, n < n1 = -1
	// compareTo, n == n1 = 0
	// @param n = numero primo, n1 = n-1
	private static boolean miller(BigInteger n, BigInteger n1, int r, int k, int nbits) {
		/*
		 * n2 = n-2
		 * a = inteiro aleatório entre [2, n-2]
		 * d = resto da divisão de n-1 por 2^r
		 * x = a^d mod n
		 */
		BigInteger n2, a, d, x;
		boolean verify = false;
		BigInteger dois = new BigInteger("2");
		if(n.compareTo(dois) == 0) {
			return true;
		}
		d = n1;
		int s;
		while(d.remainder(dois).intValue() == 0) {
        	d = d.divide(dois);
      	}
		n2 = n.subtract(dois);
		for(int i = 0; i < k; i++) {
			do {
				a = new BigInteger(nbits, new Random());
			} while (a.intValue() < 0);
			x = a.modPow(d, n); // x = a^d mod n
			verify = false;
			if(x.equals(BigInteger.ONE)){//|| x.equals(n1)) {
				verify = true;
			} else {
				for(int j = 0; j < r-1; j++) {
					if(a.modPow(dois.pow(j).multiply(d), n).equals(n1)) {
						verify = true;
						break;
					} 
				}
			}
			if(!verify){
				return false;
			}
		}
		return true;
	}

	private static int modulo(BigInteger n) {
		int cont = 0;
		BigInteger aux = n;
		BigInteger[] ver = new BigInteger[2];
		do{
			ver = n.divideAndRemainder(new BigInteger("2"));
			n = ver[0];
			cont++;
		} while(ver[1] == BigInteger.ZERO); // while n%2 == 0
		return cont;
	}
}