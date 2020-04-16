package com.active.henry.java.singleton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger2 {
    private FileWriter writer;

    public Logger2() throws IOException {
        File file = new File("/Users/wangzheng/log.txt");
        writer = new FileWriter(file, true); //true表示追加写入
    }

    public void log(String message) throws IOException {
        synchronized(this) {
            writer.write(message);
        }
    }
}