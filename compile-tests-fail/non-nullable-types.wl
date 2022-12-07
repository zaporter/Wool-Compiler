class A {
    main() : A {
        k:int<-0;
        aleph:int<-0;
        k:C<-null;
        {
            if isnull aleph then aleph<-1 else aleph<-2 fi;
            this;
        }
    }
}
class B{
         func(x : int) : boolean{
             ~x>2/5=false
         }
    
}
class C inherits B{

}
