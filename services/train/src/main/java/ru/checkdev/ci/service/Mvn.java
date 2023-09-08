package ru.checkdev.ci.service;

import com.google.common.base.Joiner;
import org.apache.maven.shared.invoker.*;
import ru.checkdev.ci.domain.Log;

import java.io.*;
import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Mvn {
    public Log exec(String home, String path, List<String> commands) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean result = false;
        try {
            InvocationRequest request = new DefaultInvocationRequest();
            request.setOutputHandler(new PrintStreamHandler(new PrintStream(baos), true));
            request.setPomFile(
                    new File(
                            Joiner.on("").join(
                                    path,
                                    File.separator,
                                    "pom.xml"
                            )
                    )
            );
            request.setGoals(commands);
            Invoker invoker = new DefaultInvoker();
            invoker.setMavenHome(new File(home));
            InvocationResult invoke = invoker.execute(request);
            result = invoke.getExitCode() == 0;
        } catch (MavenInvocationException e) {
            e.printStackTrace(new PrintWriter(baos));
        }
        return new Log(result, baos.toString().replaceAll(System.getProperty("line.separator"), "<br>"));
    }
}
