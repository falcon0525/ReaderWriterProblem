package com.example.falcon.readerwriterproblem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            ReaderWritersProblem.main();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ReaderWritersProblem {
        static java.util.concurrent.Semaphore readLock = new java.util.concurrent.Semaphore(1);
        static java.util.concurrent.Semaphore writeLock = new java.util.concurrent.Semaphore(1);
        public static final int MAX_PRIORITY = 10;
        public static final int MIN_PRIORIT = 1;
        public static final int NORM_PRIORITY = 5;
        static int readCount = 0;

        static class Read implements Runnable
        {
            public void run()
            {
                try {
                    ReaderWritersProblem.readLock.acquire();
                    ReaderWritersProblem.readCount += 1;

                    if (ReaderWritersProblem.readCount == 1) {
                        //writer waiting when reader is running
                        ReaderWritersProblem.writeLock.acquire();
                    }
                    if (ReaderWritersProblem.readCount != 0 ) {
                        ReaderWritersProblem.readLock.release();

                        System.out.println("Thread " + Thread.currentThread().getName() + " is READING");
                        Thread.sleep(1500L);
                        System.out.println("Thread " + Thread.currentThread().getName() + " has FINISHED READING");
                    }/*
                    else {
                        ReaderWritersProblem.readCount = 0;
                        ReaderWritersProblem.readLock.release();
                        System.out.println("Too many readers,Thread " + Thread.currentThread().getName() + " is WAITING");
                    }*/

                    ReaderWritersProblem.readLock.acquire();

                    if (ReaderWritersProblem.readCount == 0) {
                        ReaderWritersProblem.writeLock.release();
                    }
                    ReaderWritersProblem.readLock.release();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        static class Write implements Runnable
        {
            public void run()
            {
                try {
                    ReaderWritersProblem.writeLock.acquire();
                    System.out.println("Thread " + Thread.currentThread().getName() + " is WRITING");
                    Thread.sleep(2500L);
                    System.out.println("Thread " + Thread.currentThread().getName() + " has finished WRITING");
                    ReaderWritersProblem.writeLock.release();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        public static void main()
                throws Exception
        {
            Read read = new Read();
            Write write = new Write();
            Thread t1 = new Thread(read);
            t1.setName("reader1");
            Thread t2 = new Thread(read);
            t2.setName("reader2");
            Thread t3 = new Thread(write);
            t3.setName("write3");
            Thread t4 = new Thread(read);
            t4.setName("reader4");
            Thread t5 = new Thread(read);
            t5.setName("reader5");
            Thread t6 = new Thread(read);
            t6.setName("reader6");
            t1.start();
            t3.start();
            t2.start();
            t4.start();
            t5.start();
            t6.start();
        }
    }

}
