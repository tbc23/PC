-module(login_manager).
-export([start/0, call/1, stop/0, create_account/2, close_account/2, login/2, logout/1, online/0, create_login/0]).

start() -> register(?MODULE, spawn(fun() -> loop(dict:new()) end)).

call(Request) ->
    ?MODULE ! {Request, self()},
    receive {Res, ?MODULE} -> Res end.

stop() -> call(stop).

create_account(User,Passw) -> call({create_account, User, Passw}).

close_account(User,Passw) -> call({close_account, User, Passw}).

login(User,Passw) -> call({login, User, Passw}).

logout(User) -> call({logout, User}).

online() -> call(online).

create_login() ->
    create_account("Rasta","bruh"),
    create_account("Bro","bruh"),
    login("Rasta","bruh"),
    login("Bro","bruh").

loop(Map) ->
    receive
        {{create_account, User, Passw}, From} ->
            case dict:find(User, Map) of
                error -> 
                    From ! {ok, ?MODULE},
                    loop(dict:store(User, {Passw, false}, Map));
                _ ->
                    From ! {user_exists, ?MODULE},
                    loop(Map)
            end;
        {{close_account, User, Passw}, From} ->
            case dict:find(User, Map) of
                {ok, {Passw, _}} -> 
                    From ! {ok, ?MODULE},
                    loop(dict:erase(User, Map));
                _ -> 
                    From ! {invalid, ?MODULE},
                    loop(Map)
            end;
        {{login, User, Passw}, From} ->
            case dict:find(User, Map) of
                {ok, {Passw, false}} ->
                    From ! {ok, ?MODULE},
                    loop(dict:store(User, {Passw, true}, Map));
                _ ->
                    From ! {invalid, ?MODULE},
                    loop(Map)
            end;
        {{logout, User}, From} ->
            case dict:find(User, Map) of
                {ok, {Passw, _}} ->
                    From ! {ok, ?MODULE},
                    loop(dict:store(User, {Passw, false}, Map));
                _ ->
                    From ! {invalid, ?MODULE},
                    loop(Map)
            end;
        {online, From} ->
            From ! {[User || [{User, {_,true}}] <- dict:to_list(Map)]}, 
            loop(Map);
        {stop, From} ->
            From ! {ok, ?MODULE}
    
    end.
