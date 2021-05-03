-module(myqueue).
-export([create/0, enqueue/2, dequeue/1, test/0]).


create() -> [].

enqueue(Queue,Item) ->
    Queue ++ [Item].

dequeue([]) -> empty;
dequeue([Item | Tail]) ->
    {Tail, Item}.

test() ->
    L1 = create(),
    L2 = enqueue(L1,1),
    L3 = enqueue(L2,2),
    L4 = enqueue(L3,3),
    L5 = enqueue(L4,4),
    {[2],1} = dequeue(L3),
    empty = dequeue(L1),
    ok.