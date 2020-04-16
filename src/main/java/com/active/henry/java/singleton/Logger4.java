package com.active.henry.java.singleton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger4 {
    private FileWriter writer;
    private static final Logger4 instance = new Logger4();

    private Logger4() {
        File file = new File("/Users/wangzheng/log.txt");
        try {
            writer = new FileWriter(file, true); //true表示追加写入
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger4 getInstance() {
        return instance;
    }

    public void log(String message) throws IOException {
        writer.write(message);
    }
}

// Logger类的应用示例：
 class UserController2 {
    public void login(String username, String password) throws IOException {
        // ...省略业务逻辑代码...
        Logger4.getInstance().log(username + " logined!");
    }
}

 class OrderController2 {

    public void create(OrderVo order) throws IOException {
        // ...省略业务逻辑代码...
        Logger4.getInstance().log("Created a order: " + order.toString());
    }
}
