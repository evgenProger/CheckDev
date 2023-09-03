package ru.job4j.ci.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.security.MessageDigest;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public final class MD5 implements Serializable {
    /**
     * Шифрует строку алгоритмом MD5
     *
     * @param password входная строка
     * @return строка в формате MD5
     */
    public static String encode(String password) {
        ByteArrayOutputStream sourceOutputStream = new ByteArrayOutputStream();
        PrintStream sourcePrintStream = new PrintStream(sourceOutputStream);
        sourcePrintStream.print(password);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(sourceOutputStream.toByteArray());
            byte[] byteDigest = messageDigest.digest();

            if (byteDigest != null) {
                for (byte b : byteDigest)
                    hexDigit(printStream, b);
            }
        } catch (Exception e) {
            System.out.println("!!! Unthrowable exception");
        }

        return outputStream.toString();
    }

    private static void hexDigit(PrintStream p, byte x) {

        char c = (char) (x >> 4 & 0xf);

        if (c > (char) 9) {
            c = (char) (((int) c - 10) + (int) 'a');
        } else {
            c = (char) ((int) c + (int) '0');
        }

        p.write((int) c);

        c = (char) (x & 0xf);

        if (c > (char) 9) {
            c = (char) (((int) c - 10) + (int) 'a');
        } else {
            c = (char) ((int) c + (int) '0');
        }

        p.write((int) c);
    }
}
