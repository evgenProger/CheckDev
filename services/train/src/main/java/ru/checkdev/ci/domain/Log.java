package ru.checkdev.ci.domain;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Log {
    private final boolean success;
    private final String log;

    public Log(boolean success, String log) {
        this.success = success;
        this.log = log;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getLog() {
        return log;
    }
}
