package ru.checkdev.interview.service;

import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.StringWriter;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class LoadTest {

    final String ln = System.getProperty("line.separator");

    public ResultInfo execute(Class clTest) {
        StringWriter result = new StringWriter();
        Computer computer = new Computer();
        JUnitCore jUnitCore = new JUnitCore();
        Result data = jUnitCore.run(computer, clTest);
        result.append("Общее число тестов : ").append(String.valueOf(data.getRunCount())).append(ln);
        if (data.getFailureCount() > 0) {
            result.append("Тесты не прошли : ").append(String.valueOf(data.getFailureCount())).append(ln).append(ln);
            for (Failure failure : data.getFailures()) {
                result.append(failure.toString()).append(ln).append(ln);
            }
        } else {
            result.append("Все тесты прошли успешно.");
        }
        return new ResultInfo(result.toString(), data.getFailureCount() == 0);
    }

    public static final class ResultInfo {
        private final String info;
        private final boolean success;

        public ResultInfo(String info, boolean success) {
            this.info = info;
            this.success = success;
        }

        public String getInfo() {
            return info;
        }

        public boolean isSuccess() {
            return success;
        }
    }
}
