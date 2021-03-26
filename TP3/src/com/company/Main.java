package com.company;
import java.util.Random;

class Transferer extends Thread {
    final Bank b;
    public void run() {
        try {
            Random r = new Random();
            while (true) {
                int i = r.nextInt(5);
                int j = r.nextInt(5);
                b.transfer(i, j, 1);
            }
        } catch (Exception e) { }
    }
}

class Checker extends Thread {
    final Bank b;
    Checker(Bank b) { this.b = b; }
    public vpod run() {
        try {
            int[] a = {0,1,2,3,4};
            while (true) {

            }
        } catch (Exception e) { }
    }
}


class TesteBanco {
    public static void main(String[] args) {
	int N = 10;
	Bank b = new BankImpl(N);
	for (int i = 0; i < N ; ++i)
	    b.deposit(i,10000);
    }
}
