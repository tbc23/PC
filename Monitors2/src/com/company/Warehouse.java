package com.company;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.*;

public class Warehouse {

    private class Product {
        int quantity;
        Condition cond = lock.newCondition();
    }
    private Lock lock = new ReentrantLock();
    private Map<String, Integer> map = new HashMap<>();

    private Product get(String item) {
        Product p = map.get(item);
        if (p == null) {
            p = new Product();
            map.put(item,p);
        }
        return p;
    }

    public void supply(String item, int quantity) {
        lock.lock();
        try {
            Product p = get(item);
            p.quantity += quantity;
            p.cond.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void consume_greedy(String[] items) {
        lock.lock();
        try {
            for (String s : items) {
                Product p = get(s);
                while (p.quantity == 0)
                    p.cond.await();
                p.quantity -= 1;
            }
        } catch (InterruptedException e) { }
        finally {
            lock.unlock();
        }
    }

    public void consume(String[] items) throws InterruptedException{
        lock.lock();
        try {
            Product[] ps = new Product[items.length];
            for (int i = 0; i < items.length; ++i)
                ps[i] = get(items[i]);
            while (true) {
                boolean missing = false;
                for (Product p : ps) {
                    if (p.quantity == 0) {
                        p.cond.await();
                        missing = true;
                        break;
                    }
                }
                if(!missing)
                    break;
            }
            for(Product p : ps)
                p.quantity -= 1;
        } finally {
            lock.unlock();
        }
    }
}

/*
  private Product missing(Product[] ps) {

    for (Product p : ps) {

      if (p.quantity == 0)

        return p;

    }

    return null;

  }



  public void consume(String[] items) throws InterruptedException {

    lock.lock();

    try {

      Product[] ps = new Product[items.length];

      for (int i = 0; i < items.length; ++i)

        ps[i] = get(items[i]);

      while (true) {

        Product p = missing(ps);

        if (p == null)

          break;

        p.cond.await();

      }

      for (Product p : ps)

        p.quantity -= 1;



    } finally {

      lock.unlock();

    }

  }
 */
