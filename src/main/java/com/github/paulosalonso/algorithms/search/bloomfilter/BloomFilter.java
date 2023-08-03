package com.github.paulosalonso.algorithms.search.bloomfilter;

import com.github.paulosalonso.source.ClasspathSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BloomFilter {

    private int bits = 0;
    private final int size;
    private final List<Function<String, Integer>> hashFunctions;

    public static void main(String[] args) throws IOException {
        final List<Function<String, Integer>> hashFunctions = List.of(
                HashFunction.of(7, 31),
                HashFunction.of(9, 42),
                HashFunction.of(2, 19));

        final var bloom = new BloomFilter(2_000_000, hashFunctions);

        for (String word : getWords()) {
            bloom.add(word);
        }

        for (var i = 0; i < 100; i++) {
            System.out.print("|");
            if (bloom.contains("sapo")) {
                System.out.print("X");
            } else {
                System.out.print(" ");
            }
        }

        System.out.println("|");
    }

    private static List<String> getWords() throws IOException {
        try (final var reader = ClasspathSource.getReader("/input/search/bloomfilter/input.txt")) {
            final var result = new ArrayList<String>();
            String line;

            while ((line = reader.readLine()) != null) {
                result.add(line);
            }

            return Collections.unmodifiableList(result);
        }
    }

    private static void printBinary(int value) {
        System.out.println(Integer.toBinaryString(value));
    }

    public BloomFilter (int size, List<Function<String, Integer>> hashFunctions) {
        this.size = size;

        if (hashFunctions == null || hashFunctions.isEmpty()) {
            this.hashFunctions = List.of(value -> value.hashCode() % size);
        } else {
            this.hashFunctions = hashFunctions;
        }
    }

    public void add(String value) {
        for (var hashFunction : hashFunctions) {
            int currentBits = 1 << hashFunction.apply(value) % size;
            printBinary(this.bits);
            printBinary(currentBits);
            this.bits |= currentBits;
            printBinary(this.bits);
        }
    }

    public boolean contains(String value) {
        for (var hashFunction : hashFunctions) {
            if ((bits & 1 << hashFunction.apply(value) % size) == 0) {
                return false;
            }
        }

        return true;
    }

    private static class HashFunction implements Function<String, Integer> {

        private final int initValue;
        private final int multiplicand;

        private HashFunction(int initValue, int multiplicand) {
            this.initValue = initValue;
            this.multiplicand = multiplicand;
        }

        public static HashFunction of(int initValue, int multiplicand) {
            return new HashFunction(initValue, multiplicand);
        }

        @Override
        public Integer apply(String value) {
            int hash = initValue;

            for (int i = 0; i < value.length(); i++) {
                hash = hash * multiplicand + value.charAt(i);
            }

            return hash;
        }
    }
}
