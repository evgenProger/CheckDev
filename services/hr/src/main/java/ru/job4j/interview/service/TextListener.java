package ru.job4j.interview.service;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
import java.io.PrintWriter;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class TextListener extends RunListener {

    private final PrintWriter writer;

    public TextListener(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void testFinished(Description description) throws Exception {
        this.writer.println("finished " + description);
    }

    @Override
    public void testStarted(Description description) throws Exception {
        this.writer.println("started " + description);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        this.writer.println(failure);
    }
}
