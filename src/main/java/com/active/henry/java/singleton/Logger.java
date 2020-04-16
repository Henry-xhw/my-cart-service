package com.active.henry.java.singleton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private FileWriter writer;

    public Logger() throws IOException {
        File file = new File("/Users/wangzheng/log.txt");
        writer = new FileWriter(file, true); //true表示追加写入
    }

    public void log(String message) throws IOException {
        writer.write(message);
    }
}

// Logger类的应用示例：
 class UserController {
    private Logger logger = new Logger();

    public UserController() throws IOException {
    }

    public void login(String username, String password) throws IOException {
        // ...省略业务逻辑代码...
        logger.log(username + " logined!");
    }
}

 class OrderController {
    private Logger logger = new Logger();

    public OrderController() throws IOException {
    }

    public void create(OrderVo order) throws IOException {
        // ...省略业务逻辑代码...
        logger.log("Created an order: " + order.toString());
    }
}
