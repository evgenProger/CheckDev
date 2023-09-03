package ru.job4j.ci.service;

import org.junit.Test;

import static org.junit.Assert.*;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class MD5Test {
    @Test
    public void encode() throws Exception {
        String root = MD5.encode("root");
        System.out.println(root);
    }

}