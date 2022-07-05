package com.joshgm3z.data;

import java.util.Timer;
import java.util.TimerTask;

public class ProgressTimer {

    private Timer mTimer;
    private ProgressTask mProgressTask;

    public ProgressTimer() {
    }

    private class ProgressTask extends TimerTask {
        @Override
        public void run() {
            System.out.print(".");
        }
    }

    public void start() {
        if (mTimer == null) {
            mTimer = new Timer();
            mProgressTask = new ProgressTask();
            mTimer.schedule(mProgressTask, 0, 1000);
        }
    }

    public void stop() {
        if (mTimer != null) {
            mTimer.cancel();
            mProgressTask.cancel();
            mTimer = null;
            mProgressTask = null;
        }
    }

}
