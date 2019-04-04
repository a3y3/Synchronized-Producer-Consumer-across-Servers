/*
 * Semaphore.java
 *
 * Version:
 *      1.0
 *
 * Revisions:
 *      None
 */

/**
 * A custom written Semaphore. Includes commonly known P() and V() methods,
 * renamed acquire() and release() respectively.
 *
 * @author Soham Dongargaonkar
 * @author Satwik Mishra
 */
@SuppressWarnings("unused")
class Semaphore {
    private int permits;

    /**
     * Initializes a new Semaphore with the number of permits passed.
     *
     * @param permits the number of permits you want to start the Semaphore
     *                with. If you start it with 5 permits, acquire() can be
     *                called 5 times safely. The next time the calling thread
     *                will be forced to go in wait until someone notifies it.
     */
    Semaphore(int permits) {
        this.permits = permits;
    }

    /**
     * Try and acquire the lock (whilst also decreasing the number of permits).
     * If the number of permits is not available, the thread will go into wait.
     *
     * @throws InterruptedException if interrupted while in wait state.
     */
    synchronized void acquire() throws InterruptedException {
        if (permits <= 0) {
            wait();
        }
        permits --;
    }

    /**
     * Try and acquire the lock while decreasing the passed amount of permits.
     *
     * @param permits the number of permits to be acquired
     * @throws InterruptedException if interrupted while waiting.
     */
    synchronized void acquire(int permits) throws InterruptedException {
        while (this.permits - permits < 0) {
            if (this.permits <= 0 || this.permits - permits < 0) {
                wait();
            }
        }
        this.permits -= permits;
    }

    /**
     * Release the lock and notify a waiting thread (if any). Also increases
     * the number of available permits.
     */
    synchronized void release() {
        if (++permits > 0) {
            notify();
        }
    }

    /**
     * Releases the lock and notifies any waiting thread. Also, increases the
     * number of available number of permits by the amount passed.
     *
     * @param permits the number of permits to release.
     */
    synchronized void release(int permits) {
        if (this.permits + permits > 0) {
            notify();
        }
        this.permits += permits;
    }
}
