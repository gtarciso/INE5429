import java.math.BigInteger;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class PrimitiveRoot {
	private static final BigInteger zero = new BigInteger("0"); // constant zero
	private static final BigInteger one = new BigInteger("1"); // constant one
	private static final BigInteger two = one.add(one); // constant two

	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);
		String number = scn.next();
		BigInteger n = new BigInteger(number);
		List<BigInteger> primes = new ArrayList<BigInteger>(); 
		primes = getComposition(n.subtract(one));

		BigInteger nRoots = numberOfPrimitiveRoots(n, primes);
		String nPrimes = Integer.toString(primes.size());
		BigInteger seed = getSeed(primes, n);

		List<BigInteger> primitiveRoots = new ArrayList<BigInteger>();
		primitiveRoots = findAll(seed, n);

		Collections.sort(primitiveRoots);

		System.out.println("O número de raízes primitivas é: "+ nRoots.toString()+"\n e elas são as seguintes: ");

		for(BigInteger pr : primitiveRoots) {
			if(!pr.equals(primitiveRoots.get(primitiveRoots.size()-1)))
				System.out.print(pr+", ");
			else
				System.out.println(pr);
		}
		

	}

	private static BigInteger numberOfPrimitiveRoots(BigInteger x, List<BigInteger> primes) {
		/*
		 * t1 = totient of x 
		 * t2 = totient of t1
		 * aux = p-1, to get totient x*prod(p-1/p)
		 */
		BigInteger t1 = x.subtract(one);
		BigInteger t2 = t1;
		BigInteger aux, mult = one, div = one;
		for(int i = 0; i < primes.size(); i++) {
			aux = primes.get(i).subtract(one);
			mult = mult.multiply(aux);
			div = div.multiply(primes.get(i)); 
		}
		t2 = t2.multiply(mult);
		t2 = t2.divide(div);
		return t2;
	}

	private static List<BigInteger> getComposition(BigInteger x) {
		List<BigInteger> primes = new ArrayList<BigInteger>();
		BigInteger value = two;
		while(x.compareTo(value) >= 0) {
			if(x.mod(value).equals(zero)) {
				if(!primes.contains(value)) {
					primes.add(value);
				}
				x = x.divide(value);
			} else {
				if(value.equals(two)) {
					value = value.add(one);
				} else {
					value = value.add(two);
				}
			}
		}
		return primes;
	}


	// get the first primitive root as seed to find the others
	private static BigInteger getSeed(List<BigInteger> primes, BigInteger x) {
		BigInteger t1 = x.subtract(one), p;
		BigInteger seed = two;
		int cont = 0;
		while(true) {
			for(int i = 0; i < primes.size(); i++) {
				p = t1.divide(primes.get(i));
				if(!seed.modPow(p, x).equals(one)) 
					cont++;
			}
			if(cont == primes.size())
				return seed;
			else {
				seed = seed.add(one);
				cont = 0;
			}
		}
	}

	private static BigInteger euclides(BigInteger t1, BigInteger x) {
		if(x.equals(zero)) {
			return t1;
		} else {
			return euclides(x, t1.mod(x));
		}
	}


	// find all the other primitive roots according to the found seed
	private static List<BigInteger> findAll(BigInteger seed, BigInteger p) {
		List<BigInteger> primitiveRoots = new ArrayList<BigInteger>();
		primitiveRoots.add(seed);
		BigInteger d, prRoot;
		for(BigInteger i = two; !i.equals(p); i = i.add(one)) {
			d = euclides(p.subtract(one), i);
			if(d.equals(one)) {
				prRoot = seed.modPow(i, p);
				primitiveRoots.add(prRoot);
			}
		}
		return primitiveRoots;
	}
}