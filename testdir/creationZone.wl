class TestClass inherits IO{
	n : int <- 52;
	b : int <- add(n,3);
	f : int <- 53-2;
	initTest : int;
	initTest2 : boolean;
	initTest3 : Str;
	initTest4 : A;
	testtrue : boolean <- 5>12 - f;
	testfalse : boolean <- b<5+3;
	negTest : boolean <- ~testtrue;
	intNegTest : int <--n;
	#gol : GameOfLife <- new GameOfLife;
	
	aObject : A <- new A;
	testObj : Test <- new Test;
	testObjEqTrue : boolean <- aObject = aObject;
	testBoolEqFalse : boolean <- testtrue = ~testtrue;
	testBoolEqFalse2 : boolean <- testtrue ~= testtrue;
	testBoolEqTrue : boolean <- testfalse = testfalse;
	test() : int {2}
	add(a:int,b:int) : int {a+b}
	addPrint(a:int,b:int) : int {
		c : int <- a+b;
		{
		 outStr("Result: ");
		 outInt(c);
		 outStr("\n");
	         c; 
		}
		}
	add2(b:int) : int {b+n}
	helloWorld() : Object {
		a : boolean <- true;
		b : boolean <- true;
		n : int <- 0;
		{
		testObj.run();
		n<- n + 1;
		outInt(n);
		n<-n+1;
		outInt(n);
		aObject.outStr("Hello World!");
		outStr("This is awesome\n");
		if (a=b) then outStr("Yup\n") else outStr("Nope\n") fi; 
		}			
		}
	test12() : int {
	if 3>5 then 1 else 0 fi
}
	testLoop() : Object {
	while true loop outStr("Hey\n") pool
# null	
}
	testAssn() : int {n<-12}
	testLocalAssn(base:int) : int {a:int<-4; a<-add(base,a)}
}
class A inherits IO {
	doesitcompile : int <-12;
	c : boolean <- true;
foreverPrint() : Object {

#while c loop outStr("Hi\n") pool
null
}

}

class Test
{
    assert(expect : boolean, actual : boolean) : boolean {
        if expect = actual
        then
            true
        else
        {
            abort();
            false;
        }
        fi
    } 
   run() : boolean {
        {
            assert(true, true);
        }
    }
}

class Alpha {
	a : int <- 4;
}
class Lambda inherits Alpha {
}
class Beta inherits Alpha{
	b : int <- 5;
	acopy : Alpha;
	acopy2 : Alpha;
	test() : Alpha { l : Lambda <- new Lambda; l }
}
class Gamma {
	bObj :Beta <- new Beta;
	run() : Beta {
		bObj2 :Beta <- new Beta;
		bObj2
	}
}


class Expr inherits IO {
    print() : int {
        0
    }
    
    derive() : Expr {
        null
    }
    
    evaluate(valForX : int) : int {
        0
    }
}

class AddExpr inherits Expr {
    term1: Expr;
    term2: Expr;

    setTerms(t1 : Expr, t2 : Expr) : int {
        {
            term1 <- t1;
            term2 <- t2;
            1;
        }
    }
    

    print() : int {
        {
            outStr("(");
            term1.print();
            outStr("+");
            term2.print();
            outStr(")");
            1;
        }
    }
    
    derive() : Expr {
        # (y+z)' = y'+z'
        result : AddExpr <- new AddExpr;
        {
            result.setTerms(term1.derive(), term2.derive());
            result;
        }
    }

    evaluate(valForX : int) : int {
        term1.evaluate(valForX) + term2.evaluate(valForX)
    }
}

class MultExpr inherits Expr {
    term1: Expr;
    term2: Expr;
    
    setTerms(t1 : Expr, t2 : Expr) : int {
        {
            term1 <- t1;
            term2 <- t2;
            1;
        }
    }
    
    print() : int {
        {
            outStr("(");
            term1.print();
            outStr("*");
            term2.print();
            outStr(")");
            1;
        }
    }
    
    derive() : Expr {
        # (y*z)' = y'*z + y*z'
        addTerm1 : MultExpr <- new MultExpr;
        addTerm2 : MultExpr <- new MultExpr;
        result : AddExpr <- new AddExpr;
        {
            addTerm1.setTerms(term1.derive(), term2);
            addTerm2.setTerms(term1, term2.derive());
            result.setTerms(addTerm1, addTerm2);
            result;
        }
    }

    

    evaluate(valForX : int) : int {

        term1.evaluate(valForX) * term2.evaluate(valForX)

    }

}

class Constant inherits Expr {
    value : int;
    
    setValue(v : int) : int {
        {
            value <- v;
            1;
        }
    }
    
    print() : int {
        {
            outInt(value);
            1;
        }
    }
    

    derive() : Expr {
        result : Constant <- new Constant;
        {
            result.setValue(0);
            result;
        }
    }

    

    evaluate(valForX : int) : int {

        value

    }

}

class X inherits Expr {

    print() : int {

        {

            outStr("x");

            1;

        }

    }

    

    derive() : Expr {

        result : Constant <- new Constant;

        {

            result.setValue(1);

            result;

        }

    }

    

    evaluate(valForX : int) : int {

        valForX

    }

}




class TestDerivatives {

    mainn() : int {

        # (5 + x)*(3*x + 6) at x=1 is 54

        # ((5 + x)*(3*x + 6))' is 1*(3*x + 6) + (5 + x)*3, evaluated at x=2 would be 33

        testTerm : MultExpr <- new MultExpr;

        derivedTerm : Expr;

        

        fivePlusX : AddExpr <- new AddExpr;

        threeXPlusSix : AddExpr <- new AddExpr;

        threeTimesX : MultExpr <- new MultExpr;

        three : Constant <- new Constant;

        five : Constant <- new Constant;

        six : Constant <- new Constant;

        

        io : IO <- new IO;

        {

            three.setValue(3);

            five.setValue(5);

            six.setValue(6);

            threeTimesX.setTerms(three, new X);

            fivePlusX.setTerms(five, new X);

            threeXPlusSix.setTerms(threeTimesX, six);

            

            testTerm.setTerms(fivePlusX, threeXPlusSix);

            io.outStr("f(x) = ");

            testTerm.print();

            io.outStr("\n");

            

            io.outStr("f(1) = ");

            io.outInt(testTerm.evaluate(1));

            io.outStr("\n");

            

            io.outStr("f'(x) = ");

            derivedTerm <- testTerm.derive();

            derivedTerm.print();

            io.outStr("\n");

            

            io.outStr("f'(2) = ");

            io.outInt(derivedTerm.evaluate(2));

            io.outStr("\n");

            

            1;

        }

    }

}

class NestedSelect {

    run(): int {

        (* Should return 22 *)

        f(1, 1) + f(1, 3) + f(2, 3) + f(2, 1) + f(3, 9)

    }
    
    slect(value: int):int{
    select 
	value < 0 : -1;
        value = 1 : 1;
	value = 2 : 2;
	end
    }
   slect2(value: int):int{
	if value < 0 then -1 else if value =1 then 1 else if value=2 then 2 else 0 fi fi fi
    }

    

    f(mode: int, value: int): int {

        select

            mode = 1: select

                value = 1: 5;

                value = 2: 10;

                value = 3: 7;

            end;

            mode = 2: value * value;

        end

    }

}


class Parent {}
class Child inherits Parent {}
class B {
	childAndThis(): Parent {
		if true then (new Child) else (new Parent) fi
	}
}


class FizzBuzz inherits IO {
	run(): int { 
		n: int <- 0;
		{
			while n < 100 loop {
				if n / 3 * 3 = n then
					{
						outStr("fizz");
						if n / 5 * 5 = n then
							outStr("buzz")
						else
							null
						fi;
						outStr("\n");
					}
				else
					if n / 5 * 5 = n then
						outStr("buzz\n")
					else 
						# Test the targeted method call here, too
						{this.outInt(n); this.outStr("\n");}
					fi
				fi;
				n <- n + 1;
			} pool;
			0;
		}
	}
}
class HaveAllBuiltIns inherits IO {
	# output tests
	outStr: IO <- (new IO).outStr("hello");
	outInt: IO <- (new IO).outInt(5);
	outBool: IO <- (new IO).outBool(true);
	ignore: IO <- (new IO).outStr("\n'hello5true' should appear above this'\n");	# Object tests
	typeName: Object <- {
		this.outStr((new Object).typeName());
		this.outStr("\nEnsure 'Object' was printed above. Cannot check at runtime...\n");
	};
	copy: Object <- (new Object).copy();
	#copySelf: HaveAllBuiltIns <- this.copy();	# Str tests
	length: Object <- printOkMessage("length", "hello".length() = 5);
	concat: Object <- {
		this.outStr("Ensure that 'hello world' follows this: ");
		this.outStr("hello".concat(" world\n"));
	};
	substr: Object <- {
		this.outStr("Ensure that 'he' follows this: ");
		this.outStr("hello".substr(0, 2)); this.outStr("\n");
	};	# input tests
	inStr: Object <- {
		this.outStr("\nEnter a message: ");
		this.outStr((new IO).inStr().concat("\nEnsure your message matches... cannot be checked at runtime\n"));
	};
	inInt: Object <- {
		this.outStr("Enter the number '5': ");
		this.printOkMessage("inInt", (new IO).inInt() = 5);
	};
	inBool: Object <- {
		this.outStr("Enter the value 'false': ");
		this.printOkMessage("inBool", ~(new IO).inBool());
	};	# abort test
	abort: Object <- {this.outStr("Program will abort now...\n"); (new Object).abort();};	printOkMessage(name: Str, isOk: boolean): Object {
		this.outStr(if isOk then name.concat(" is ok\n") else name.concat(" is not ok\n") fi)
	}
}


class GameOfLife inherits IO {
	row1: int <- 0010000;
	row2: int <- 0001000;
	row3: int <- 0111000;
	row4: int <- 0000000;
	row5: int <- 0000000;
	row6: int <- 0000000;
	row7: int <- 0000000;

	main(): int {
		(new GameOfLife).run()
	}

	run(): int {
		list: LinkedList <- new LinkedList;
		n: int <- 0;
		{
			this.setup(list);
			this.printBoard(list);
			this.outStr("\n");
			# Run for 16 generations
			while n < 16 loop {
				list <- this.runGeneration(list);
				this.printBoard(list);
				this.outStr("\n");
				n <- n + 1;
			} pool;
			0;
		}
	}

	setup(list: LinkedList): int {
		n: Integer <- new Integer;
		{
			n.set(row1);
			list.add(n.copy());
			n.set(row2);
			list.add(n.copy());
			n.set(row3);
			list.add(n.copy());
			n.set(row4);
			list.add(n.copy());
			n.set(row5);
			list.add(n.copy());
			n.set(row6);
			list.add(n.copy());
			n.set(row7);
			list.add(n.copy());
			0;
		}
	}

	runGeneration(list: LinkedList): LinkedList {
		res: LinkedList <- new LinkedList;
		i: int;
		j: int;
		boardSize: int <- list.size();
		currentRow: int;
		currentCell: int;
		nextCell: int;
		outRow: int;
		outRowWrapped: Integer;
		numAdjacent: int;
		{
			while i < boardSize loop {
				currentRow <- list.get(i).get();
				j <- boardSize-1;
				outRow <- 0;
				while j >= 0 loop {
					currentCell <- getNthCell(currentRow, j);
					numAdjacent <- getNumAdjacent(list, i, j);
					nextCell <- currentCell;
					#this.outInt(i); this.outStr(", "); this.outInt(j); this.outStr(" => ");
					#this.outInt(numAdjacent); this.outStr("\n");
					if and(currentCell = 0, numAdjacent = 3) then
						nextCell <- 1
					else
						if currentCell = 1 then
							if or(numAdjacent < 2, numAdjacent > 3) then
								nextCell <- 0
							else
								0
							fi
						else
							0
						fi
					fi;
					outRow <- outRow * 10 + nextCell;
					j <- j - 1;
				} pool;
				res.add((new Integer).set(outRow));
				i <- i + 1;
			} pool;
			res;
		}
	}

	getNumAdjacent(list: LinkedList, row: int, col: int): int {
		dRow: int <- -1;
		dCol: int <- -1;
		boardSize: int <- list.size();
		# The current position being observed
		candidateRow: int;
		candidateCol: int;
		# holds the row currently being observed
		rowCursor: int;
		numAdjacent: int;
		{
			while dRow <= 1 loop {
				dCol <- -1;
				while dCol <= 1 loop {
					if and(dCol = 0, dRow = 0) then 0 else {
						candidateRow <- row + dRow;
						candidateCol <- col + dCol;
						#this.outInt(candidateRow); this.outStr(", "); this.outInt(candidateCol); this.outStr("\n");
						# Check if we're out of bounds
						if or(
							or(candidateRow < 0, candidateCol < 0),
							or(candidateRow >= boardSize, candidateCol >= boardSize)
						) then 0
						else {
							rowCursor <- list.get(candidateRow).get();
							if getNthCell(rowCursor, candidateCol) = 1 then
								numAdjacent <- numAdjacent + 1
							else
								0
							fi;
						} fi;
					} fi;
					dCol <- dCol + 1;
				} pool;
				dRow <- dRow + 1;
			} pool;
			numAdjacent;
		}
	}

	printBoard(list: LinkedList): Object {
		i: int <- 0;
		j: int <- 0;
		currentRow: int <- 0;
		boardSize: int <- list.size();
		while i < boardSize loop {
			currentRow <- list.get(i).get();
			# Board is assumed to be square
			j <- boardSize - 1;
			while j >= 0 loop {
				#this.outInt(currentRow); this.outStr("\n");
				#this.outInt(j); this.outStr("\n");
				this.outStr(
					if getNthCell(currentRow, j) = 1 then "#" else "." fi
				);
				j <- j - 1;
			} pool;
			this.outStr("\n");
			i <- i + 1;
		} pool

	}

	getNthCell(row: int, n: int): int {
		divisor: int <- pow(10, n);
		m: int <- row / divisor;
		m - m/10*10
	}

	lsbIsOn(n: int): boolean {
		n/10 * 10 ~= n
	}

	pow(base: int, power: int): int {
		result: int <- 1;
		count: int <- power;
		{
			while count > 0 loop {
				result <- result * base;
				# Why would I ever write it like this? To try and trick my parser :)
				count <- -1 + count;
			} pool;
			result;
		}
	}

	and(a: boolean, b: boolean): boolean {
		if a then
			if b then true else false fi
		else
			false
		fi
	}

	or(a: boolean, b: boolean): boolean {
		if a then
			true
		else
			if b then true else false fi
		fi
	}
}

class LinkedList {
	head : Node <- new Node ; #null head

	(*Adds an object to the tail of the linked list *)
	add(d: Integer): Integer
	{
		tail: Node <- getTail() ;
		newNode: Node <- new Node ;
		{
			newNode.setData(d);
			tail.setNext(newNode);
			d;
		}
	}

	(*Gets the value of the object at a particular location in the linked list
	or null if it doesn't exist *)
	get(loc: int): Integer
	{
		curPos: int <- 0;
		curNode: Node <- head.getNext(); #getTail().getNext();
		stillLoop: boolean <- true; #wool doesn't have and, so I do this instead
		{
			if loc < 0 then #just to make sure the function terminates and returns null if loc < 0
			{
				stillLoop <- false;
				curNode <- null;
			}
			else null fi;
			while stillLoop
			loop
				if isnull curNode then stillLoop <- false
				else if curPos >= loc then stillLoop <- false
					else{
						curNode <- curNode.getNext();
						curPos <- curPos + 1;
						true;
					}
					fi
				fi
			pool;
			if isnull curNode then null
				else
				curNode.getData()
			fi;
		}
	}

	(*Gets the tail from the linked list *)
	getTail(): Node
	{
		tail: Node <- head ;
		{
			while ~isnull tail.getNext()
			loop tail <- tail.getNext()
			pool; #once the tail's next is null, then it's the tail
			tail; #returns the tail
		}
	}

	(*gets the size of the linked list*)
	size(): int
	{
		curNode: Node <- head ;
		curPos: int <- 0 ;
		{
			while ~isnull curNode.getNext()
			loop
			{
				curNode <- curNode.getNext();
				curPos <- curPos + 1;
			}
			pool; #once the tail's next is null, then it's the tail
			curPos; #returns the position
		}
	}
}

class Node
{
	data: Integer ;
	next: Node ;

	setData(d: Integer): Integer
	{
		data <- d #should just return d
	}

	getData(): Integer
	{
		data #Shouldn't need a semicolon since ID alone is an expression
	}

	setNext(n: Node): Node
	{
		next <- n
	}

	getNext(): Node
	{
		next
	}
}


class Integer {
	value: int;

	get(): int {
		value
	}

	set(v: int): Integer {
		{
			value <- v;
			this;
		}
	}
}

