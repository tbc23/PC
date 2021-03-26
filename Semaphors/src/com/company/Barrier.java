package com.company;

import java.util.concurrent.Semaphore;

class Barrier {

    private int c = 0;
    private final int N;
    Semaphore mut = new Semaphore(1);
    Semaphore gate = new Semaphore(0);

    public Barrier (int N) {
        this.N = N;
    }
    public void await() throws InterruptedException {
        mut.acquire();
        c += 1;
        if (c == N){
            c = 0;
            for (int i = 0; i < N ; ++i) {
                //completar
            }

        }
        mut.release();
    }
}

class T extends Thread {
    public void run() {

    }
}

class TestBarrier{
    public static void main(String[] args) {
        Barrier b = new Barrier(3);

        new Thread(() -> {
            try {
                Thread.sleep(100);
                System.out.println("vou fazer await");
                b.await();
                System.out.println("await retornou");
            } catch (Exception e) { }
        } ).start();

        new Thread(() -> {
            try {
                Thread.sleep(100);
                System.out.println("vou fazer await");
                b.await();
                System.out.println("await retornou");
            } catch (Exception e) { }
        } ).start();

        new Thread(() -> {
            try {
                Thread.sleep(100);
                System.out.println("vou fazer await");
                b.await();
                System.out.println("await retornou");
            } catch (Exception e) { }
        } ).start();

    }
}