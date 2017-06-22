#include <stdio.h>
#include <math.h>
#include <stdlib.h>

#define MAX 512

int satisfyTest(int k, int n, int *m) {
	// inequation m(0) * m(n-k+2) * ... * m(n) < m(1) * ... * m(k)
	// left = left side of inequation
	// right = right side of inequation
	long int left = m[0];
	long int right = 1;
	int i;
	for(i = (n-k)+2; i <= n; i++) {
		left *= m[i];
	}
	for(i = 1; i <= k; i++) {
		right *= m[i];
	}
	printf("%d < %d\n", left, right);
	if(left < right) {
		return 1;
	}
	else {
		return 0;
	}
}

int isPrime(int n) {
	int i;
	if(n % 2 == 0)
		return 0;
	for(i = 3; i < (int)sqrt((double)n); i+= 2) {
		if (n % i == 0)
			return 0; 
	}
	return 1;
}
// generate the coprimes sequence that will be used to create the shares
void generateCoprimes(int *m, int n) {
	int i, aux;
	for(i = 0; i <= n; i++) {
		do {
			aux = rand() % MAX; 
		} while(isPrime(aux) != 1);
		m[i] = aux;
	}
}

// b = begin of array
// regular quicksort
void sort(int *m, int n, int b) {
	if((n-b) > 1) {
		int pivot = m[b], left = b, right = n;
		while(left <= right) {
			while(m[left] < pivot) {
				left++;
			}
			while(m[right] > pivot) {
				right--;
			}
			if(left <= right) {
				int aux = m[left];
				m[left] = m[right];
				m[right] = aux;
				left++;
				right--;
			}
		}
		sort(m, right, 0);
		sort(m, n, left);
	}
}

// just a method to certify that the array is really sorted
int sorted(int *m, int n) {
	int i;
	for(i = 0; i < n; i++) {
		if(m[i] > m[i+1])
			return 0;
	}
	return 1;
}
// generate de random coprime sequence necessary to create the shares
void generateSequence(int *m, int n, int k) {
	do {
		generateCoprimes(m, n+1);
		// sort the sequence to fill the inequation cited above
		do {
			sort(m, n+1, 0);
		} while(sorted(m, n) != 1);
	} while(satisfyTest(k, n, m) == 0);

}


/*
	alpha is a random number such that Secret+(alpha*m[0]) < m[i] * ... * m[k]
	then we use secret + (alpha*m[0]) to compute the shares using
	share[i] = (secret + (alpha*m[0])) modulo m[i]
*/
int generateAlpha(int s, int *m, int k) {
	long int aux = 1;
	int i, alpha;
	for(i = 1; i <= k; i++) {
		aux *= m[i];
	}
	do {
		alpha = rand()%aux;
	} while(s+(alpha*m[0]) > aux);
	return alpha;
}

/*
	generate shares using share[i] = (secret + (alpha*m[0])) modulo m[i]
*/
void generateParts(int *m, int alpha, int n, int secret, int *parts) {
	int aux = secret+alpha*m[0];
	int i, j;
	for(i = 1; i <= n; i++) {
		parts[i] = aux % m[i];
	}
}


int extendedEuclides(int x, int p) {
	int r = x, r1 = p, v = 0, v1 = 1;
	int rs, vs, q;
	while(r1 != 0) {
		q = r/r1;
		rs = r;
		vs = v;
		r = r1;
		v = v1;
		r1 = rs - q * r1;
		v1 = vs - q * v1;
	}
	return v;
}

// parts and modulo have size k+1
// modulo[0] constains m0
int reconstruct(int *parts, int k, int *modulo) {
	int i;
	// x0 = e[i] * inverse(m, m/parts[i])
	int x0 = 0;
	// m = m[1] * ... * m[k]
	int m = 1;
	for(i = 1; i <= k; i++) {
		m *= modulo[i];
	}
	int *e = malloc((k+1) * sizeof(int));
	// generate e[i] such that e[i] = modular inverse * M/m[i]
	// M = m[1] * ... * m[k]
	// then we compute the sum of all e[i]
	for(i = 1; i <=k; i++) {
		int aux = m/modulo[i];
		e[i] = extendedEuclides(modulo[i], aux)*aux;
		x0 += e[i]*parts[i];
	}
	// if we take the sum of e[i] and apply modulo M we get
	// the S + (alpha*m[0]) back
	// and then we take this value and apply modulo m[0] to get the secret back
	int secret = x0 % m;
	if(x0 < 0) {
		secret += m;
	}
	printf("secret = %d\n", secret);
	secret = secret % modulo[0];
	return secret;
}

void restoreSecret() {
	int k, *modulo, *parts;
	printf("informe a quantidade de partes suficientes para restaurar o segredo\n");
	scanf("%d", &k);
	modulo = malloc((k+1)*sizeof(int));
	parts = malloc((k+1)*sizeof(int));
	printf("informe o m0\n");
	scanf("%d", &modulo[0]);
	int i;
	for(i = 1; i < k+1; i++) {
		printf("informe as tuplas no formato shares(i) m(i)\n");
		scanf("%d %d", &parts[i], &modulo[i]);
	}
	printf("%d\n", reconstruct(parts, k, modulo));

}

void createSecret() {
	int *m, *parts;
	int n, alpha, k, secret;
	printf("informe a quantidade de partes a serem divididas\n");
	scanf("%d", &n);
	printf("informe a quantidade necessária de partes para reconstruir o segredo\n");
	scanf("%d", &k);
	m = malloc((n+1)*sizeof(int));
	parts = malloc((k+1)*sizeof(int));
	int i;
	printf("informe o segredo\n");
	scanf("%d", &secret);
	generateSequence(m, n, k);
	alpha = generateAlpha(secret, m, k);
	printf("alpha = %d\n", alpha);
	generateParts(m, alpha, n, secret, parts);
	printf("o m0 é: %d\n", m[0]);
	printf("a sequencia e as partes geradas foram\n");
	for(i = 1; i <= n; i++) {
		printf("(%d, %d)\n", parts[i], m[i]);
	}
}

int main() {
	int val;
	printf("1 - criar pontos\n2 - recuperar segredo\n");
	scanf("%d", &val);
	if(val == 1)
		createSecret();
	else if( val == 2)
		restoreSecret();
	else
		return 0;
}