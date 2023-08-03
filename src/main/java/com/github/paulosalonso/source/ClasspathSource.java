package com.github.paulosalonso.source;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class ClasspathSource {

    private ClasspathSource() {}

    public static BufferedReader getReader(String sourcePath) {
        final var inputStream = ClasspathSource.class.getResourceAsStream(sourcePath);
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
