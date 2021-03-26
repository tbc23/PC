package com.company;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.*;

class InvalidAccount extends Exception {
    int id;
    InvalidAccount(int id) { this.id = id; }
}

class NotEnoughFunds extends Exception { }

interface Bank {
    int createAccount (int initialBalance);
    int closeAccount (int id) throws InvalidAccount;
    void deposit(int id, int val) throws InvalidAccount;
    void withdraw(int id, int val) throws InvalidAccount, NotEnoughFunds;
    int totalBalance(int accounts[]) throws InvalidAccount;
    void transfer(int i, int j, int value) throws InvalidAccount, NotEnoughFunds;
}

class BankImpl implements Bank {

    private static class Account {
        int balance;
        Lock l = new ReentrantLock();
    }
    private int nextId = 0;
    private final Map<Integer,Account> acs = new HashMap<>();
    // private final Lock l = new ReentrantLock();
    private final ReadWriteLock l = new ReentrantReadWriteLock();
    private final Lock rl = l.readLock();
    private final Lock wl = l.writeLock();

    public int createAccount(int balance) {
        wl.lock();
        Account c = new Account();
        c.balance = balance;
        int id = nextId;
        acs.put(nextId,c);
        nextId++;
        wl.unlock();
        return id;
    }

    public int closeAccount(int id) throws InvalidAccount {
        wl.lock();
        Account c;
        try{
            c = acs.get(id);
            if (c==null)
                throw new InvalidAccount(id);
            acs.remove(id);
            c.l.lock();
        } finally {
            wl.unlock();
        }

        try{
            return c.balance;
        } finally {
            c.l.unlock();
        }
    }

    BankImpl(int N) {
        acs = new Account[N];
        this.N = N;
        for (int i = 0; i < N ; ++i ) {
            acs[i] = new Account();
        }
    }

    public void deposit (int id, int val) throws InvalidAccount {
        Account c;
        rl.lock();
        try {
            c = acs.get(id);
            if (c == null)
                throw new InvalidAccount(id);

            c.l.lock();
        } finally {
            rl.unlock();
        }
        try {
            c.balance += val;
        } finally {
            c.l.unlock;
        }

    }

    public void withdraw(int id, int val) throws InvalidAccount, NotEnoughFunds {
        Account c;
        rl.lock();
        try {
            c = acs.get(id);
            if (c == null)
                throw new InvalidAccount(id);
            c.l.lock;
        } finally {
            rl.unlock();
        }
        try {
            if (c.balance < val)
                throw new NotEnoughFunds();
            c.balance -= val;
        } finally {
            c.l.unlock();
        }

    }

    @Override
    public int totalBalance(int[] accounts) throws InvalidAccount {
        return 0;
    }

    public int totalBalance (int accounts[]) throws InvalidAccount {
        accounts = accounts.clone();
        Arrays.sort(accounts);
        Account[] cs = new Account[accounts.length];
        int sum = 0;
        rl.lock();
        try{
            for (int i = 0 ; i < accounts.length; ++i){
                cs[i] = acs.get(accounts[i]);
                if (cs[i] == null)
                    throw new InvalidAccount(accounts[i]);
            }
            for (Account c: cs) {
                c.l.lock();
            }
        } finally {
            rl.unlock();
        }
        for (Account c: cs) {
            sum += c.balance;
            c.l.unlock();
        }
        return sum;
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
        Account cfrom, cto, c1, c2;
        rl.lock();
        try {
            cfrom = acs.get(from);
            if (cfrom == null)
                throw new InvalidAccount(from);
            cto = acs.get(to);
            if (cto == null)
                throw new InvalidAccount(to);
            if (from < to) {
                c1 = cfrom;
                c2 = cto;
            } else {
                c2 = cfrom;
                c1 = cto;
            }
            c1.l.lock();
            c2.l.lock();
            cfrom.l.lock();
            cto.l.lock();
        } finally {
            rl.unlock();
        }
        try {
            try {
                if (cfrom.balance < val) throw new NotEnoughFunds();
                cfrom.balance -= val;
                cto.balance += val;

            } finally {
                cto.l.unlock();
            }
        }
    }
}