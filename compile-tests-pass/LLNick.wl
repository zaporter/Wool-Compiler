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
