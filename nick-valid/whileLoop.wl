class While {
	countToTen(): Object {
		x: int <- 0;
		while x < 0 loop x <- x + 1 pool
	}
}