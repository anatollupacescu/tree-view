package com.tree;

import com.demo.ClassGraphTester;
import com.demo.GraphTester;

public class TesterFactory {

    public static GraphTester getTester() {
        return new ClassGraphTester();
    }
}