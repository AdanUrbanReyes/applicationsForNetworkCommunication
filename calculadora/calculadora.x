struct twoOperandos{
	double oo;
	double ot;
};
/*
        name program CALCULADORA
        identificador program 0x31111111;
        name version RPC
        identificador version=1
        name firs service ADITION
        name second service RESTA
...     name n service n-service
how compilar?
        rpcgen -C -a file.x
        make -f Makefile.file
*/

program CALCULADORA{
	version AYAN{
		double SUMA(twoOperandos)=1;
		double RESTA(twoOperandos)=2;
		double MULTIPLICACION(twoOperandos)=3;
		double DIVICION(twoOperandos)=4;
		double SENO(double)=5;
		double COSENO(double)=6;
		double TANGENTE(double)=7;
		double COTANGENTE(double)=8;
		double SECANTE(double)=9;
		double COSECANTE(double)=10;
		double RAIZ(double)=11;
		double LOGARITMONEPEREANO(double)=12;
	}=1;
}=0x31111111;
