package com.company;
import java.util.concurrent.Semaphore;

class BoundedBuffer{

    private int[] a;

    private final int N;

    private int iget = 0;
    private int iput = 0;
    private int nitems = 0;

    public BoundedBuffer (int N) {
        this.N = N;
        a = new int[N];
    }

    public synchronized int get() throws InterruptedException {
        while (nitems == 0)
            wait();
        int res = a[iget];
        iget = (iget + 1) % N;
        nitems -= 1;
        notifyAll();
        return res;
    }

    public synchronized void put(int v) throws InterruptedException {
        while (nitems == N)
            wait();
        a[iput] = v;
        iput = (iput + 1) % N;
        nitems += 1;
        notifyAll();
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
