-module(myeffqueue).
-export([create/0, enqueue/2, dequeue/1, test/0]).


create() -> {[],[]}.

% reverse([Item | Tail]) -> reverse(Tail) ++ [Item].  está definido em erlang

enqueue({In,Out},Item) -> {[Item | In], Out} .

dequeue({[],[]}) -> empty;
dequeue({In,[Item | Tail]}) -> {{In, Tail},Item};
dequeue({In,[]}) -> dequeue({[],lists:reverse(In)}).


test() ->
    L1 = create(),
    L2 = enqueue(L1,1),
    L3 = enqueue(L2,2),
    L4 = enqueue(L3,3),
    L5 = enqueue(L4,4),
    {L6,1} = dequeue(L5),
    {L7,2} = dequeue(L4),
    empty = dequeue(L1),
    ok.