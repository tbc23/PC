package com.company;
import java.util.concurrent.Semaphore;

class BoundedBuffer{

    private int[] a;
    private Semaphore mut = new Semaphore(1);
    private Semaphore items = new Semaphore(0);
    private Semaphore slots;
    private final int N;

    private int iget = 0;
    private int iput = 0;

    public BoundedBuffer (int N) {
        this.N = N;
        a = new int[N];
        slots = new Semaphore(N);
    }

    public int get() throws InterruptedException {
        items.acquire();
        int res = a[iget];
        iget = (iget + 1) % N;
        slots.release();
        return res;
    }

    public void put(int v) throws InterruptedException {
        mut.acquire();
        slots.acquire();
        a[iput] = v;
        iput = (iput + 1) % N;
        mut.release();
        items.release();

    }
}

class Produtor extends Thread {
    BoundedBuffer b;
    Produtor(BoundedBuffer b) { this.b  = b; }
    public void run() {
        for (int i = 1; i < 10000000; ++i) {
            try {
                System.out.println("Vou fazer put");
                Thread.sleep(300);
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
