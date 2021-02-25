package com.company;

class MyThread extends Thread {  // Maneira 1 de criar threads
    public void run() {
        for (int i = 0; i < 100; ++i)
            System.out.println("Bruh");
    }
}

class MyRunnable implements Runnable {  // Maneira 2 de criar threads
    public void run() {
        try {
            for (int i = 0; i < 100; ++i){
                System.out.println("Bruh");
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread interrompida");
        }
    }
}

class Printer extends Thread {
    final int I;
    final int id;
    public Printer(int I, int id) { this.I = I; this.id = id;}
    public void run() {
        for (int i = 0; i < I; ++i ) {
            System.out.println("Thread: " + i + "Num: " + 1);
        }
    }
}
class Counter {
    private long i;
    void increment() { i = i+1; }
    public long value() { return i; }
}

class Incrementer extends Thread {
    final Counter c;
    final int I;
    public Incrementer(int I, Counter c) { this.I = I; this.c = c;}
    public void run() {
        for (int i = 0; i < I; ++i ) {
            c.increment();
        }
    }
}

class Main {
    public static void main (String[] args) throws InterruptedException {
        final int N = Integer.parseInt(args[0]);
        final int I = Integer.parseInt(args[1]);
        Counter c = new Counter();
        Thread a[] = new Thread[N];
        for (int i=0; i<N; i++)
            a[i] = new Incrementer(I,c);
        for (int i = 0 ; i<N; i++)
            a[i].start();
        for (int i = 0 ; i<N; i++)
            a[i].join();
        System.out.println(c.value());
    }
}