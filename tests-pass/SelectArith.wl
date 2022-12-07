

class SelectArith inherits SomeClass

{

    a: int <- 2 - 3;

    b: int <- (2+ 4) * (-3 /3);

    c: Str <- "Hello \t World \n";

    d: int <- a - b + c;

    e: boolean <- a < b;

    f: boolean <- false;

    g: boolean <- e = f;

    

    fib(n: int): int #comment here

    {

        select 

            n < 0: 0;

            n = 0: 1;

            n = 1: 1;

            n > 1: fib(n-1) + fib(n-2);

        end

    }

}


