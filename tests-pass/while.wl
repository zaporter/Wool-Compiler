class Test {
    a: int <- 0;    method(): Str {{
        (while a < 10 loop a <- a + 1 pool).otherMethod();
    }}   
     otherMethod(): Str { null } # Test
}
(* This is a 
# comment *)

class House inherits Test{
    people : int <- 0;
     func(): Str {
        var :Str <-"People";
             {
                 while (alpha<"episilon") loop {
                     var <- "pizza";
                     print(alpha);
                     this.loopagain();
                 } pool;
                 
             }
     }
}
