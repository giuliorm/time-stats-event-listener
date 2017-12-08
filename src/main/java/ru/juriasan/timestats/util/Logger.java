package ru.juriasan.timestats.util;

public class Logger {

    private final static String logFormat = "%s: %s";
    private final static String ERROR = "ERROR";
    private final static String INFO = "INFO";

    private volatile static Logger instance;

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null)
                    instance = new Logger();
            }
        }
        return instance;
    }

    private void log(String logType, String message) {
        System.out.println(String.format(logFormat, logType, message));
    }

    public void error(String message) {
        log(ERROR, message);
    }

    public void info(String message) {
        log(INFO, message);
    }
}
