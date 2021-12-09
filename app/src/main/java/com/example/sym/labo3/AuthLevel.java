package com.example.sym.labo3;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AuthLevel {

    public static final List<String> NFC_MESSAGES = Arrays.asList("test", "1 2 3 4", "é è ê ë ē", "♤ ♡ ♢ ♧");

    private static final int TIME_BEFORE_AUTH_LEVEL_DECREASE = 5000;
    private static final int MAX_AUTH_LEVEL_VALUE = 10;
    private static final int MIN_AUTH_LEVEL_VALUE = 0;

    private final Timer timer;
    private final Handler handler;
    private final TextView securityLevel;

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

    public AuthLevel(Handler handler, TextView securityLevel) {
        authLevelValue = MAX_AUTH_LEVEL_VALUE;
        timer = new Timer();
        timer.schedule(createTimerTask(), TIME_BEFORE_AUTH_LEVEL_DECREASE, TIME_BEFORE_AUTH_LEVEL_DECREASE);
        this.handler = handler;
        this.securityLevel = securityLevel;
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
                if (authLevelValue > MIN_AUTH_LEVEL_VALUE) {
                    authLevelValue--;
                }
                handler.post(() -> {
                    securityLevel.setText(String.format("Security lvl: %d/%d", authLevelValue, MAX_AUTH_LEVEL_VALUE));
                });
            }
        };
    }
}
