class Undefined {
	nope(): int {
		# ACHTUNG: UNDEFINED USAGE
		x: int <- y;
		y: int <- 5;
		
		x
	}
}