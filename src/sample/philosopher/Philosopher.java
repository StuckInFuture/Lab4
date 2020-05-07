package sample.philosopher;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public class Philosopher extends Thread {
    public ArrayList<Philosopher> table;
    private String index;
    private boolean isHeAte = false;
    public ReentrantLock global = new ReentrantLock();
    public ReentrantLock mtx = new ReentrantLock();

    public Philosopher(String newIndex, ArrayList<Philosopher> newList, ReentrantLock newGlobal) throws InterruptedException {
        index = newIndex;
        table = newList;
        global = newGlobal;
    }

    public boolean getStatus() {
        return isHeAte;
    }

    public void setStatus(boolean status) {
        isHeAte = status;
    }
    @Override
    public void run() {
        int leftIndex;
        int rightIndex;
        int id = 0;
        while (true) {
            try {
                global.tryLock(10000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            for (int i = 0; i < table.size(); i++) {
                if (table.get(i).index == index) id = i;
            }
            if (id == table.size() - 1) {
                leftIndex = 0;
                rightIndex = id - 1;
            } else {
                leftIndex = id + 1;
                rightIndex = id - 1;
            }
            if (id == 0) {
                if (table.size()==1) {leftIndex=0; rightIndex=0;}
                else {leftIndex = id + 1;rightIndex = table.size() - 1; }
            }

            try {
                if (table.get(leftIndex).mtx.isLocked() == false && table.get(rightIndex).mtx.isLocked() == false) {
                    mtx.lock();
                    synchronized (global){
                    global.unlock();}
                    Philosopher.sleep(5000);
                    setStatus(true);
                    mtx.unlock();
                    break;
                }
                else global.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            global.tryLock(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < table.size(); i++)
            if (table.get(i).index == index) id = i;
           System.out.println("Философ " + index + " покушал");
            table.remove(id);
        synchronized (global){
            global.unlock();}
            Thread.currentThread().interrupt();
    }
}

