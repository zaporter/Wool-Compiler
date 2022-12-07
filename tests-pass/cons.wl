(*CREDIT: Wool manual*)

class Cons inherits List {
    xcar : int;
    xcdr:List;
    isNil() : boolean {false}
    init(hd : int, tl: List) : Cons {
        pizza : boolean <- false;
        chicken : List <- null;
        {
            xcar <- hd;
            xcdr <- tl;
            this;
        }
    }

}
