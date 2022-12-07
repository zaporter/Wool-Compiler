/* 
 RESOURCES CONSULTED:
 Wool Manual v 1.5
 http://2.droppdf.com/files/jgTvu/the-definitive-antlr-4-reference.pdf
 https://stackoverflow.com/questions/19236797/capturing-string-literals-with-escaped-quotes-in-antlr
 */
 
 /*
 * **MODIFICATIONS OF MY GRAMMAR BASED ON YOUR GRAMMAR **
 * - Reduced the number of redundant non-terminals in the Expr section
 * - Simplified operator graph regarding +/-*
 * - Added assoc=right to certain expressions
 * - Worked on precedence. 
 * 
 */
 

grammar Wool;
@header {
package wool.lexparse;
}

program  :   classes+=classDec* EOF ;       // Non-greedy just to not have a warning
TYPEINT : 'int';
TYPEBOOLEAN : 'boolean';
//TYPESTR : 'Str';
typeBuiltIn : TYPEINT
			| TYPEBOOLEAN
           // | TYPESTR
           ;
 
                                             
LCURLY  : '{';
RCURLY  : '}';
LSQUARE : '[';
RSQUARE : ']';
LPAR    : '(';
RPAR    : ')';
COMMA   : ',';
COLON   : ':';
ASSIGN  : '<-';
CLASS   : 'class';
ELSE    : 'else';
END     : 'end';
FI      : 'fi';
IF      : 'if';
INHERITS: 'inherits';
LOOP    : 'loop';
NULL    : 'null';
POOL    : 'pool';
SELECT  : 'select';
THEN    : 'then';
WHILE   : 'while';

EOL     : ';';
DOT     : '.';
NEW     : 'new';
THIS    : 'this';
ISNULL  : 'isnull';

MINUS   : '-';
MULT_SYM: '*';
PLUS    : '+';
DIV_SYM : '/';

LESS    : '<';
LESS_EQ : '<=';
EQUAL   : '=';
NEQUAL  :  '~=';
GREATER_EQ: '>=';
GREATER : '>';
UNARYOP : '~'; // Skipping MINUS 


fragment QUOTE_LITERAL : '\\"';
STRING_CONSTANT : '"' (QUOTE_LITERAL | ~('\n') )*? '"';

NUM_CONSTANT : [0-9]+;
BOOL_CONSTANT : 'true' | 'false';
CLASS_NAME:[A-Z][a-zA-Z0-9_]*;
VAR_NAME:[a-z][a-zA-Z0-9_]*;



typeName : typeBuiltIn 
 			| CLASS_NAME;



varDecLine :  varNameT=VAR_NAME  COLON  typeNameT=typeName  (ASSIGN  expr)? EOL;
varAssn : varNameT=VAR_NAME  ASSIGN  expr; 
//varDecLine : varNameT=varDec EOL;
param : varNameT=VAR_NAME COLON typeNameT=typeName;

classDec : CLASS className=CLASS_NAME ( INHERITS  parent=CLASS_NAME )? classBody;
classBody : LCURLY (variables+=varDecLine | methods+=methodDec )* RCURLY ;

expr :  expr DOT funcName=VAR_NAME parameterListCall   # FullMethodCallExpression 
            | funcName=VAR_NAME parameterListCall            # LocalMethodCallExpression
            | IF expr THEN expr ELSE expr FI                 # IfExpression 
            | WHILE expr LOOP expr POOL                      # LoopExpression
            | LCURLY (expr EOL)+ RCURLY                      # BlockExpression
            | SELECT (expr COLON expr EOL)+ END              # SelectExpression
            | NEW  classNameT=CLASS_NAME  # NewExpression
            | <assoc=right> MINUS expr               # NegateExpression
            | ISNULL expr          # IsNullExpression
            | expr multSymbol=(MULT_SYM | DIV_SYM) expr # MultExpression
            | expr diffSymbol=(PLUS | MINUS) expr # PlusExpression
            | expr compSymbol=(LESS|LESS_EQ|GREATER|GREATER_EQ) expr #CompExpression
            | <assoc=right> expr eqSymbol=(EQUAL|NEQUAL) expr # EqualityExpression
            | <assoc=right> UNARYOP expression=expr             # LogicalNegationExpression
            | LPAR expr RPAR                                 # ParenExpression
            | varName=VAR_NAME ASSIGN expr                   # AssignExpression
            | VAR_NAME                 # VariableExpression  
            | THIS                        # ThisExpression
            | NULL                        # NullExpression
            | NUM_CONSTANT                # IntConstantExpression
            | BOOL_CONSTANT               # BoolConstantExpression
            | STRING_CONSTANT             # StringConstantExpression
          
            ;

methodDec : methodNameT=VAR_NAME params=parameterListDec  COLON  returnType=typeName block=methodBlock;
methodBlock : LCURLY (lines+=varDecLine)* expr RCURLY ;
			
parameterListDec : LPAR  (param (COMMA param)*)?  RPAR;
parameterListCall : LPAR (expr (COMMA expr)*)?  RPAR;

COMMENT_ML : ('(*' (COMMENT_ML|.)*? '*)') -> skip;
COMMENT_L : '#' .*? '\n' ->skip;
WS : [ \n\t\r] ->skip;
