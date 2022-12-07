class Pizza {
    toppings : List; # This is a comment
    (* this is also a (*test*) *)
    temp : int <- 50;
    deliver(address : Str) : boolean {
        tmpAddr : Str <- address;
          {
              (* this is also a
# weird commend. Why would you (*(*do *)*) this*)
            if (address = tmpAddr) then 
                print("Hello") else temp<-4 fi;
            tempAddr <- "Chiekc12   \n pizza";
          }
    }
}
