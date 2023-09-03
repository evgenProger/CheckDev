package ru.job4j.ci.service;

import com.google.common.base.Throwables;
import ru.job4j.ci.domain.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Cmd {

    public Log exec(String path, List<String> commands, String separator) {
        StringBuilder log = new StringBuilder();
        boolean success = true;
        try {
            log.append(commands).append(separator);
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commands);
            builder.directory(new File(path));
            builder.redirectErrorStream(true);
            final Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                log.append(line).append(separator);
            }
            process.waitFor();
            success = process.exitValue() == 0;
        } catch (Exception e) {
           log.append(Throwables.getStackTraceAsString(e));
        }
        return new Log(success, log.toString());
    }
}
