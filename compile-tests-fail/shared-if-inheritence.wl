class A {
    main() : A{
        k:int<-0;
        aleph:int<-0;
        k:C<-null;
        {
            if isnull k then new B else new C fi;
        }
    }
}
class B {
         func(x : int) : boolean{
             ~x>2/5=false
         }
    
}
class C inherits B{

}
