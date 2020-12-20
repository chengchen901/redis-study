package com.study.redis.apply.lesson7_performance;

import java.io.IOException;
import java.net.Socket;

/**
 * Redis客户端实现
 *
 * @author Hash
 * @since 2020/12/20
 */
public class RedisClient {

    Socket connection;

    public RedisClient(String host, int port) throws IOException {
        connection = new Socket(host, port);
        // 数据发送，读取 基于连接
    }

    // set key value
    public String set(String key, String value) throws IOException {
        // 构建数据包
        StringBuilder command = new StringBuilder();
        command.append("*3").append("\r\n"); // 第一部分描述整个请求包含几个参数

        command.append("$3").append("\r\n");// 第一个参数的长度 --- 命令的名称
        command.append("SET").append("\r\n");// 第一个参数的值

        command.append("$").append(key.getBytes().length).append("\r\n");// 第2个参数的长度
        command.append(key).append("\r\n");// 第2个参数的值

        command.append("$").append(value.getBytes().length).append("\r\n");// 第3个参数的长度
        command.append(value).append("\r\n");// 第2个参数的值

        // 发送命令请求，发给redis服务器
        connection.getOutputStream().write(command.toString().getBytes());
        // 读取redis服务器的响应
        byte[] response = new byte[1024];
        connection.getInputStream().read(response);
        return new String(response);
    }

    // get key
    public String get(String key) throws IOException {
        // 构建数据包
        StringBuilder command = new StringBuilder();
        command.append("*2").append("\r\n"); // 第一部分描述整个请求包含几个参数

        command.append("$3").append("\r\n");// 第一个参数的长度 --- 命令的名称
        command.append("GET").append("\r\n");// 第一个参数的值

        command.append("$").append(key.getBytes().length).append("\r\n");// 第2个参数的长度
        command.append(key).append("\r\n");// 第2个参数的值

        System.out.println(command.toString());

        // 发送命令请求，发给redis服务器
        connection.getOutputStream().write(command.toString().getBytes());
        // 读取redis服务器的响应
        byte[] response = new byte[1024];
        connection.getInputStream().read(response);
        return new String(response);
    }
}
