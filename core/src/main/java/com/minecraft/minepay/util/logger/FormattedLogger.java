package com.minecraft.minepay.util.logger;

import lombok.Setter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FormattedLogger {

    private static final String LOG_PREFIX_FORMAT = "[%s]";

    private final Logger handle;
    private final FormattedLogger parent;
    private String prefix;
    private String logPrefix;

    @Setter
    private boolean debug;

    public FormattedLogger(Logger handle, String prefix) {
        this.handle = handle;
        this.parent = null;
        this.prefix = prefix;

        this.logPrefix = buildPrefix(new StringBuilder());
    }

    public FormattedLogger(FormattedLogger parent, String prefix) {
        this.handle = parent.handle;
        this.parent = parent;
        this.prefix = prefix;

        this.logPrefix = buildPrefix(new StringBuilder());
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.logPrefix = buildPrefix(new StringBuilder());
    }

    private String buildPrefix(StringBuilder builder) {
        if (prefix != null) {
            builder.insert(0, String.format(LOG_PREFIX_FORMAT, prefix));
        }
        return parent == null ? builder.isEmpty() ? "" : builder + " " : parent.buildPrefix(builder);
    }

    public void log(String format, Object... args) {
        log(Level.INFO, null, String.format(format, args));
    }

    public void log(String message) {
        log(Level.INFO, null, message);
    }

    public void log(Level level, String format, Object... args) {
        log(level, null, String.format(format, args));
    }

    public void log(Level level, String message) {
        log(level, null, message);
    }

    public void log(Level level, Throwable ex, String format, Object... args) {
        log(level, ex, String.format(format, args));
    }

    public void log(Level level, Throwable ex, String message) {
        handle.log(level, logPrefix + message, ex);
    }

    public void error(String message, Throwable throwable) {
        log(Level.SEVERE, throwable, message);
    }

    public void error(String message) {
        log(Level.SEVERE, message);
    }

    public void error(String message, Object... args) {
        log(Level.SEVERE, String.format(message, args));
    }

    public void warn(String message, Throwable throwable) {
        log(Level.WARNING, throwable, message);
    }

    public void warn(String format, Object... args) {
        log(Level.WARNING, String.format(format, args));
    }

    public void warn(String format, Throwable throwable, Object... args) {
        log(Level.WARNING, throwable, String.format(format, args));
    }

    public void warn(Throwable throwable, String format, Object... args) {
        log(Level.WARNING, throwable, String.format(format, args));
    }

    public void debug(String message) {
        if (!debug)
            return;

        this.log(Level.WARNING, message);
    }
}
