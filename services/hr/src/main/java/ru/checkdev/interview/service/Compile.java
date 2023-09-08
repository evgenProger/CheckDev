package ru.checkdev.interview.service;

import java.util.ArrayList;
import java.util.List;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Compile {
    public static class Source {
        private String name;
        private String code;

        public Source(String name, String code) {
            this.name = name;
            this.code = code;
        }
    }

    public LoadTest.ResultInfo task(Source source, Source test) {
        if (source.code.contains("java.io") || source.code.contains("java.net") || source.code.contains("java.sql")) {
            return new LoadTest.ResultInfo(
                    "Вы не можете использовать классы из пакетов java.io, java.net, java.sql, java.nio",
                    false
            );
        }
        final List<InMemoryCompiler.IMCSourceCode> classSources = new ArrayList<>();
        classSources.add(new InMemoryCompiler.IMCSourceCode(source.name, source.code));
        classSources.add(new InMemoryCompiler.IMCSourceCode(test.name, test.code));

        final InMemoryCompiler uCompiler = new InMemoryCompiler(classSources);
        final CompilerFeedback compilerFeedback = uCompiler.compile();
        if (compilerFeedback != null && compilerFeedback.success) {
            return new LoadTest().execute(uCompiler.getCompiledClass(test.name));
        } else {
            return new LoadTest.ResultInfo(compilerFeedback.toString(), false);
        }
    }
}
