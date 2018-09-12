// Need this to use the getline C function on Linux. Works without this on MacOs. Not tested on Windows.
#define _GNU_SOURCE
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <assert.h>
#include <math.h>

#include "token.h"
#include "queue.h"
#include "stack.h"


void computeExpressions(FILE *input);
Queue *stringToTokenQueue(const char *expression);
bool isSymbol(char c);
void printToken(FILE *f, void *e);
Queue *shuntingYard(Queue *infix);
float evaluateExpression(Queue *postfix);
Token *evaluateOperator(Token *arg1, Token *op, Token *arg2);
/** Main function for testing.
 * The main function expects one parameter that is the file where expressions to translate are
 * to be read.
 *
 * @Pre This file must contain a valid expression on each line
 *
 */
int main(int argc, char **argv){
	FILE *input;
	
	if (argc<2) {
		fprintf(stderr,"usage : %s filename\n", argv[0]);
		return 1;
	}
	
	input = fopen(argv[1], "r");

	if ( !input ) {
		perror(argv[1]);
		return 1;
	}

	computeExpressions(input);

	fclose(input);
	return 0;
}

void computeExpressions(FILE *input){
	char *line;
	size_t len;
	int read = 0;
	while ((read = getline(&line, &len, input)) != -1){
		if (read > 3){
			printf("Input\t\t : %s", line);
			printf("Infix\t\t : ");
			queueDump(stdout, stringToTokenQueue(line), printToken);
			printf("\nPostfix\t\t : ");
			queueDump(stdout, shuntingYard(stringToTokenQueue(line)), printToken);
			printf("\nEvaluate\t : %f", evaluateExpression(shuntingYard(stringToTokenQueue(line))));
			printf("\n\n");
		}
	}
	free(line);
}

Queue *stringToTokenQueue(const char *expression){
	Queue *ret = createQueue();			//Token queue
	const char *curpos = expression;	//Cursor
	int numLen = 0;						//Length of token
	while (*curpos != '\0'){
		if (*curpos == ' ' || *curpos == '\n'){
			curpos++;	//shift the cursor
		}else if (isSymbol(*curpos)){
			/* add a symbol token to queue */
			numLen++;
			ret = queuePush(ret, createTokenFromString(curpos, numLen));
			curpos++;
			numLen = 0;
		}else{
			/* add a number token to queue */
			while(*(curpos+numLen) != ' ' && *(curpos+numLen) != '\n' && *(curpos+numLen) != '\0' && !isSymbol(*(curpos+numLen))){
				numLen++;	//Increment length for each digits
			}
			ret = queuePush(ret, createTokenFromString(curpos, numLen));
			curpos += numLen;
			numLen = 0;
		}
	}
	return ret;
}

bool isSymbol(char c){
	return (c=='+' || c=='-' || c=='*' || c=='/' || c=='^' || c=='(' || c==')');
}

void printToken(FILE *f, void *e){
	Token *ptrTok = (Token*)e;
	tokenDump(f, ptrTok);
}

Queue *shuntingYard(Queue *infix){
	Queue *out = createQueue();
	Stack *ops = createStack(10);
	Token *tok;
	while (!queueEmpty(infix)){
		tok = (Token*)queueTop(infix);
		if (tokenIsNumber(tok)){
			queuePush(out, tok);
		}
		if (tokenIsOperator(tok)){
			while ( !stackEmpty(ops) && !tokenIsParenthesis(stackTop(ops)) && ( tokenGetOperatorPriority(stackTop(ops)) > tokenGetOperatorPriority(tok) || (tokenGetOperatorPriority(stackTop(ops)) == tokenGetOperatorPriority(tok) && tokenOperatorIsLeftAssociative(stackTop(ops))) ) ){
					queuePush(out, stackTop(ops));
					stackPop(ops);
			}
			stackPush(ops, tok);
		}
		if (tokenIsParenthesis(tok)){
			if (tokenGetParenthesisSymbol(tok) == '('){
				stackPush(ops, queueTop(infix));
			}
			if (tokenGetParenthesisSymbol(tok) == ')'){
				while (!stackEmpty(ops) && !tokenIsParenthesis(stackTop(ops))){
					queuePush(out, stackTop(ops));
					stackPop(ops);
				}
				stackPop(ops);
			}
		}
		queuePop(infix);
	}
	if (queueEmpty(infix)){
		while (!stackEmpty(ops)){
			queuePush(out, stackTop(ops));
			stackPop(ops);
		}
	}
	return out;
}

float evaluateExpression(Queue *postfix){
	float result = 0.f;
	Token *arg1;		//Will contain the first operand
	Token *arg2;		//Will contain the second operand
	Token *tok;			//Current Token
	Stack *stackResult = createStack(20);	//Traitment stack
	while (!queueEmpty(postfix)){
		tok = queueTop(postfix);
		if (tokenIsOperator(tok)){
			arg2 = stackTop(stackResult);
			stackPop(stackResult);
			arg1 = stackTop(stackResult);
			stackPop(stackResult);
			stackPush(stackResult, evaluateOperator(arg1, tok, arg2));
		}
		else if (tokenIsNumber(tok)){
			stackPush(stackResult, tok);
		}
		queuePop(postfix);
	}
	result = tokenGetValue(stackTop(stackResult));
	stackPop(stackResult);
	return result;
}

Token *evaluateOperator(Token *arg1, Token *op, Token *arg2){
	assert(tokenIsNumber(arg1) && tokenIsNumber(arg2) && tokenIsOperator(op));
	float lval = tokenGetValue(arg1);	//left value
	float rval = tokenGetValue(arg2);	//right value
	switch (tokenGetOperatorSymbol(op)){
		case '+':
			return createTokenFromValue(lval + rval);	
		case '-':
			return createTokenFromValue(lval - rval);
		case '*':
			return createTokenFromValue(lval * rval);
		case '/':
			return createTokenFromValue(lval / rval);
		case '^':
			return createTokenFromValue(pow(lval, rval));
		default:
			return createTokenFromValue(0.f);		
	}
}
