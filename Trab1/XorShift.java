import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class XorShift{
	public static void main(String[] args) {
		int nbits;
		Scanner io = new Scanner(System.in);
		System.out.println("Informe a quantidade de bits");
		nbits = io.nextInt();
		BigInteger x = new BigInteger(nbits, new Random());
		do {
			x = mix(x, nbits);
		} while(x.bitLength() != nbits);
		System.out.println(x);
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