package com.company;

class Barrier {

    private int c = 0;
    private int e = 0;

    private final int N;

    public Barrier (int N) {
        this.N = N;
    }

    public synchronized void await() throws InterruptedException {
        int current_e = e;
        c += 1;
        if (c == N) {
            notifyAll();
            e += 1;
            c = 0;
        } else while (current_e == e)
            wait();
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
                System.out.println("vou fazer await");
                b.await();
                System.out.println("await retornou");
            } catch (Exception e) { }
        } ).start();

    }
}