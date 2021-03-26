package com.company;
import java.util.Arrays;

class InvalidAccount extends Exception {
    int id;
    InvalidAccount(int id) { this.id = id; }
}

class NotEnoughFunds extends Exception { }

interface Bank {
    int createAccount (int initialBalance);
    void closeAccount (int id) throws InvalidAccount;
    void deposit(int id, int val) throws InvalidAccount;
    void withdraw(int id, int val) throws InvalidAccount, NotEnoughFunds;
    int totalBalance(int accounts[]) throws InvalidAccount;
    void transfer(int i, int j, int value) throws InvalidAccount, NotEnoughFunds;
    int totalBalance (int i, int accounts[]) throws InvalidAccount;
}

class BankImpl implements Bank {

    private class Account {
        int balance;
    }

    final int N;
    private final Account acs[];

    BankImpl(int N) {
        acs = new Account[N];
        this.N = N;
        for (int i = 0; i < N ; ++i ) {
            acs[i] = new Account();
        }
    }

    public void deposit (int id, int val) throws InvalidAccount {
        if (id < 0 || id >= N)
            throw new InvalidAccount(id);
        Account c = acs[id];
        synchronized(c) {
            c.balance += val;
        }
    }

    public void withdraw(int id, int val) throws InvalidAccount, NotEnoughFunds {
        if (id < 0 || id >= N)
            throw new InvalidAccount(id);
        Account c = acs[id];
        synchronized(c) {
            if (c.balance < val)
                throw new NotEnoughFunds();
            c.balance -= val;
        }
    }

    @Override
    public int totalBalance(int[] accounts) throws InvalidAccount {
        return 0;
    }

    public int totalBalance (int i, int accounts[]) throws InvalidAccount {
        accounts = accounts.clone();
        Arrays.sort(accounts);
        return totalBalance(0,accounts);
    }
/*
    public int totalBalance(int i, int accounts[]) throws InvalidAccount {
        Account a = acs[accounts[i]];
        synchronized (a) {
            if (i < accounts.length - 1)
                return totalBalance(i + 1, accounts);
            int sum = 0;
            for (int id : accounts) {
                Account c = acs[id];
                sum += c.balance;
            }
            return sum;
        }
    } */

    public void transfer(int from, int to, int val) throws InvalidAccount, NotEnoughFunds {
        if (from < 0 || from >= N)
            throw new InvalidAccount(from);
        if (to < 0 || to >= N)
            throw new InvalidAccount(to);
        Account cfrom = acs[from];
        Account cto = acs[to];
        Account c1, c2;
        if(from < to){
            c1 = cfrom;
            c2 = cto;
        }
        else{
            c2 = cfrom;
            c1 = cto;
        }
        synchronized(c1) {
            synchronized(c2) {
                if (cfrom.balance < val) throw new NotEnoughFunds();
                cfrom.balance -= val;
                cto.balance += val;
            }
        }
    }
}