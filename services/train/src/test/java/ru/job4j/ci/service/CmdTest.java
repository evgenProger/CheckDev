package ru.job4j.ci.service;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.ci.domain.Log;

import java.util.Arrays;

import static org.junit.Assert.*;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Ignore
public class CmdTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Test
    public void whenCdCommandThenMoveUp() {
        Cmd cmd = new Cmd();
        Log log = cmd.exec("c:\\tmp\\students", Arrays.asList("cmd", "/c", "dir"), "");
        LOG.info(log.getLog());
    }

    @Test
    public void when() {
        Cmd cmd = new Cmd();
        String command = "curl -H \"Content-Type:application/json\" -POST -k -vi job4j:password@46.4.53.141:9000/registration -d \"{\\\"username\\\":\\\"username\\\",\\\"email\\\":\\\"parsentev@yandex.ru\\\",\\\"password\\\":\\\"password\\\"}\"";
        Log log = cmd.exec("c:\\tmp\\students",  Arrays.asList(command.split(" ")), System.getProperty("line.separator"));
        LOG.info(log.getLog());
    }
}