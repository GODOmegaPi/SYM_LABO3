package com.example.sym.labo3;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.time.format.ResolverStyle;
import java.util.Timer;
import java.util.TimerTask;

public class AuthLevel {

    private static final int TIME_BEFORE_AUTH_LEVEL_DECREASE = 5000;
    private static final int MAX_AUTH_LEVEL_VALUE = 10;
    private static final int MIN_AUTH_LEVEL_VALUE = 0;

    private final Timer timer;
    private final Handler handler = new Handler();

    private int authLevelValue;

    public enum Level {
        MAXIMUM(10),
        MEDIUM(5),
        MINIMUM(1);

        private final int level;

        Level(int level) {
            this.level = level;
        }

        public int getValue() {
            return level;
        }
    }

    public AuthLevel() {
        authLevelValue = MAX_AUTH_LEVEL_VALUE;
        timer = new Timer();
        timer.schedule(createTimerTask(), TIME_BEFORE_AUTH_LEVEL_DECREASE, TIME_BEFORE_AUTH_LEVEL_DECREASE);
    }

    public void resetAuthLevelValue() {
        this.authLevelValue = MAX_AUTH_LEVEL_VALUE;
    }

    public boolean isAuthLevelHighEnough(Level level) {
        return authLevelValue >= level.getValue();
    }

    private TimerTask createTimerTask() {
        return new TimerTask() {
            public void run() {
                handler.post(() -> {
                    if (authLevelValue > MIN_AUTH_LEVEL_VALUE) {
                        authLevelValue--;
                    }
                });
            }
        };
    }
}
