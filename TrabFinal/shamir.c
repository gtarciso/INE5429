#include <stdio.h>
#include <math.h>
#include <stdlib.h>

// generate the polinomial
void generatePolinomial(int *a, int k, int s, int nbits) {
	// a0 = secret
	a[0] = s;
	int i, max = (int)pow(2, nbits);
	// find the other k-1 random numbers
	for(i = 1; i < k; i++) {
		a[i] = rand() % max;
	}
}

void generatePoints(int *a, int *points, int p, int n, int k) {
	int i, j;
	int soma;
	// for to generate each point
	for(i = 0; i < n; i++) {
		soma = 0;
		// generate the point using the polinomial
		for(j = 0; j < k; j++) {
			soma += ((int)pow(i+1,j)%p * (a[j]%p));
		}
		points[i] = soma % p;
	}
}


int lagrangeInterpolation(int *pointsX, int *pointsY, int k, int p) {
	double sum = 0.0;
	int i, j, m, n;
	double f;
	long int aux, l;
	l = 0;
	
	for (j = 0; j < k; j++) {
		aux = 1;
		for (m = 0; m < k;m++) {
			if (m != j) {
				// sub = xm - xj
				int sub = pointsX[m]-pointsX[j];
				if(sub < 0) {
					sub += p;
				}
				// use the inverse of (xm - xj) * xm to get the interpolation in finite field	
				aux *= pointsX[m]*euclides(sub, p);
				aux = aux % p;
			}
		}
		l += aux*pointsY[j];
		l = l % p;
	}
	
	if(l < 0)
		return (int)l+p;
	return (int)l;

}

// extended euclides to get the multiplicative modular inverse
int euclides(int x, int p) {
	int r = x, r1 = p, u = 1, u1 = 0;
	int rs, us, q;
	while(r1 != 0) {
		q = r/r1;
		rs = r;
		us = u;
		r = r1;
		u = u1;
		r1 = rs - q * r1;
		u1 = us - q * u;
	}
	return u;
}


void restoreSecret() {
	/*
		k = number of parts necessary to restore the secret
		p = prime used to create the finite field
		secret = secret to be restored
		pointsX, pointsY = tuple of values that represents the point (part of the secret)
		(pointsX, pointsY) = (x, f(x) mod p)
	*/
	int *pointsX, *pointsY;
	int k, i, p;
	int secret;
	printf("informe a quantidade de partes necessárias para restaurar o segredo\n");
	scanf("%d", &k);
	pointsX = malloc(k*sizeof(int));
	pointsY = malloc(k*sizeof(int));
	printf("informe o primo\n");
	scanf("%d", &p);
	printf("informe as tuplas no formato n n\n");
	for(i = 0; i < k; i++) {
		scanf("%d %d", &pointsX[i], &pointsY[i]);
	}
	secret = lagrangeInterpolation(pointsX, pointsY, k, p);
	printf("%d\n", secret);
}

int createSecret() {
	/* 
		n = parts that the secret is shared
		k = subset sufficient to reconstruct the secret
		s = secret
		nbits = number of bits to generate the random numbers
		p = prime number to make the finite field
	*/
	int n, k, s, nbits, p;
	// array to put random numbers to generate the polinomial
	int *a;
	int *points;
	printf("Informe a quantidade de partes\n");
	scanf("%d", &n);
	printf("Informe a quantidade de partes suficientes para reconstruir o segredo\n");
	scanf("%d", &k);
	printf("Informe o segredo\n");
	scanf("%d", &s);
	printf("Informe o número primo\n");
	scanf("%d", &p);
	printf("Informe a quantidade de bits para gerar o polinomio\n");
	scanf("%d", &nbits);
	a = malloc(k*sizeof(int));
	points = malloc(n*sizeof(int));	
	generatePolinomial(a, k, s, nbits);
	int j;
	printf("polinomio: ");
	for(j = 0; j < k; j++) {
		printf("%dx^%d ", a[j], j);
	}
	printf("\n");
	generatePoints(a, points, p, n, k);
	int i;
	for(i = 0; i < n; i++) {
		printf("(%d, %d)\n", i+1, points[i]);
	}
	
}

void main() {
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