class Iterable{
    loopPos : int <- 0;
    hasNext(): boolean {loopPos < getSize()}

    next() : Object {
        {
            loopPos <- loopPos+1;
            get(loopPos);
        }
        }
    get(pos_ : int) : Object{ null }
    getSize() : int {0}
    resetIndex() : Iterable {
        {
            loopPos<-0;
            this;
        }
    } 
}

class Array inherits Iterable {
    (*Todo
    
    I am thinking that the best way to implement this is a really wide tree. 
    A binary tree might be fine... With some math, it might be faster to do something like
    a 6 or 8 child per node diagram. On large trees this would have space improvements
    and might even be able to lower the insert and read time.

    Not sure yet. It would be nice if the language spec included arrays. I guess that forces us to create our own... thats pretty cool too. 


    *)


}
