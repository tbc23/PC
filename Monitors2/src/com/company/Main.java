package com.company;
import java.util.concurrent.locks.*;

class BoundedBuffer{

    private int[] a;

    private final int N;

    private int iget = 0;
    private int iput = 0;
    private int nitems = 0;

    Lock lock = new ReentrantLock();
    Condition notEmpty = lock.newCondition();
    Condition notFull = lock.newCondition();

    public BoundedBuffer (int N) {
        this.N = N;
        a = new int[N];
    }

    public int get() throws InterruptedException {
        lock.lock();
        try {
            while (nitems == 0)
                notEmpty.await();
            int res = a[iget];
            iget = (iget + 1) % N;
            nitems -= 1;
            notFull.signal();
            notifyAll();
            return res;
        } finally {
            lock.unlock();
        }
    }

    public void put(int v) throws InterruptedException {
        lock.lock();
        try {
            while (nitems == N)
                notFull.await();
            a[iput] = v;
            iput = (iput + 1) % N;
            nitems += 1;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
}

class Produtor extends Thread {
    BoundedBuffer b;
    Produtor(BoundedBuffer b) { this.b  = b; }
    public void run() {
        for (int i = 1; i < 10000000; ++i) {
            try {
                Thread.sleep(300);
                System.out.println("Vou fazer put de " + i);
                b.put(i);
                System.out.println("Fiz put");
            } catch (Exception e) { }
        }
    }
}

class Consumidor extends Thread {
    BoundedBuffer b;
    Consumidor(BoundedBuffer b) { this.b  = b; }
    public void run() {
        for (int i = 1; i < 10000000; ++i) {
            try {
                Thread.sleep(300);
                System.out.println("Vou fazer put");
                int v = b.get();
                System.out.println("Fiz get de " + v);
            } catch (Exception e) { }
        }
    }
}


class ProdutorConsumidor {

    public static void main(String[] args) {
        BoundedBuffer b = new BoundedBuffer(50);
        Produtor p = new Produtor(b);
        Consumidor c = new Consumidor(b);

	    p.start();
	    c.start();
    }
}
