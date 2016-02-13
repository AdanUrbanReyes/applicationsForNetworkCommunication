/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#ifndef _CALCULADORA_H_RPCGEN
#define _CALCULADORA_H_RPCGEN

#include <rpc/rpc.h>


#ifdef __cplusplus
extern "C" {
#endif


struct twoOperandos {
	double oo;
	double ot;
};
typedef struct twoOperandos twoOperandos;

#define CALCULADORA 0x31111111
#define AYAN 1

#if defined(__STDC__) || defined(__cplusplus)
#define SUMA 1
extern  double * suma_1(twoOperandos *, CLIENT *);
extern  double * suma_1_svc(twoOperandos *, struct svc_req *);
#define RESTA 2
extern  double * resta_1(twoOperandos *, CLIENT *);
extern  double * resta_1_svc(twoOperandos *, struct svc_req *);
#define MULTIPLICACION 3
extern  double * multiplicacion_1(twoOperandos *, CLIENT *);
extern  double * multiplicacion_1_svc(twoOperandos *, struct svc_req *);
#define DIVICION 4
extern  double * divicion_1(twoOperandos *, CLIENT *);
extern  double * divicion_1_svc(twoOperandos *, struct svc_req *);
#define SENO 5
extern  double * seno_1(double *, CLIENT *);
extern  double * seno_1_svc(double *, struct svc_req *);
#define COSENO 6
extern  double * coseno_1(double *, CLIENT *);
extern  double * coseno_1_svc(double *, struct svc_req *);
#define TANGENTE 7
extern  double * tangente_1(double *, CLIENT *);
extern  double * tangente_1_svc(double *, struct svc_req *);
#define COTANGENTE 8
extern  double * cotangente_1(double *, CLIENT *);
extern  double * cotangente_1_svc(double *, struct svc_req *);
#define SECANTE 9
extern  double * secante_1(double *, CLIENT *);
extern  double * secante_1_svc(double *, struct svc_req *);
#define COSECANTE 10
extern  double * cosecante_1(double *, CLIENT *);
extern  double * cosecante_1_svc(double *, struct svc_req *);
#define RAIZ 11
extern  double * raiz_1(double *, CLIENT *);
extern  double * raiz_1_svc(double *, struct svc_req *);
#define LOGARITMONEPEREANO 12
extern  double * logaritmonepereano_1(double *, CLIENT *);
extern  double * logaritmonepereano_1_svc(double *, struct svc_req *);
extern int calculadora_1_freeresult (SVCXPRT *, xdrproc_t, caddr_t);

#else /* K&R C */
#define SUMA 1
extern  double * suma_1();
extern  double * suma_1_svc();
#define RESTA 2
extern  double * resta_1();
extern  double * resta_1_svc();
#define MULTIPLICACION 3
extern  double * multiplicacion_1();
extern  double * multiplicacion_1_svc();
#define DIVICION 4
extern  double * divicion_1();
extern  double * divicion_1_svc();
#define SENO 5
extern  double * seno_1();
extern  double * seno_1_svc();
#define COSENO 6
extern  double * coseno_1();
extern  double * coseno_1_svc();
#define TANGENTE 7
extern  double * tangente_1();
extern  double * tangente_1_svc();
#define COTANGENTE 8
extern  double * cotangente_1();
extern  double * cotangente_1_svc();
#define SECANTE 9
extern  double * secante_1();
extern  double * secante_1_svc();
#define COSECANTE 10
extern  double * cosecante_1();
extern  double * cosecante_1_svc();
#define RAIZ 11
extern  double * raiz_1();
extern  double * raiz_1_svc();
#define LOGARITMONEPEREANO 12
extern  double * logaritmonepereano_1();
extern  double * logaritmonepereano_1_svc();
extern int calculadora_1_freeresult ();
#endif /* K&R C */

/* the xdr functions */

#if defined(__STDC__) || defined(__cplusplus)
extern  bool_t xdr_twoOperandos (XDR *, twoOperandos*);

#else /* K&R C */
extern bool_t xdr_twoOperandos ();

#endif /* K&R C */

#ifdef __cplusplus
}
#endif

#endif /* !_CALCULADORA_H_RPCGEN */
