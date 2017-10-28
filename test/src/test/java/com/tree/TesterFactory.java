package com.tree;

public class TesterFactory {

    public static com.demo.GraphTester getTester(){
        return new com.demo.WebGraphTester();
    }
}
