package com.tree;

import com.demo.ClassGraphTester;
import com.demo.GraphTester;
import com.demo.WebGraphTester;

public class TesterFactory {

    public static GraphTester getTester(){
        return new ClassGraphTester();
    }
}