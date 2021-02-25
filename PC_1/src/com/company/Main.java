package com.company;
import java.util.Scanner;

public class Main implements Runnable {
    private static int count = 0;

    @Override
    public void run() {
        int max = 5;
        for(int j = 0; j< max; j++) {
            count++;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please input the number of Threads you want to create: ");
        int n = input.nextInt();
        System.out.println("You selected " + n + " Threads");
        for(int i = 0; i < n; i++){
            Thread temp = new Thread(new Main(),"Thread#"+i);
            temp.start();
        }
    }
}