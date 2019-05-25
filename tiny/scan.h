/****************************************************/
/* File: scan.h                                     */
/* The scanner interface for the TINY compiler      */
/* Compiler Construction: Principles and Practice   */
/* Kenneth C. Louden                                */
/****************************************************/

#ifndef SCAN_H
#define SCAN_H

/* MAXTOKENLEN is the maximum size of a token */
#define MAXTOKENLEN 40

/* tokenString array stores the lexeme of each token */
extern char tokenString[MAXTOKENLEN+1];

/* to scan file, this function should be called only once,
 * and initialize the static variables which helps getToken()
 */
void getTokenInit(void);

/* function getToken returns the 
 * next token in source file
 */
TokenType getToken(void);

#endif
