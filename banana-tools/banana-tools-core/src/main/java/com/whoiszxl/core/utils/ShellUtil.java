package com.whoiszxl.core.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.ssh.ChannelType;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * shell连接工具
 * @author whoiszxl
 */
@Slf4j
public class ShellUtil {

    private Vector<String> stdout;

    Session session;

    /**
     * 构造函数，初始化session连接
     * @param host ip地址
     * @param username 用户名
     * @param password 密码
     * @param port 端口
     */
    public ShellUtil(String host, String username, String password, int port) {
        JSch jsch = new JSch();
        try {
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(1000 * 10);
        } catch (JSchException e) {
            log.error("ShellUtil|远程连接出错|{},{},{}", host, port, username, e);
        }
    }


    public int execute(String command) {
        int code = 0;
        ChannelShell channel = null;
        PrintWriter printWriter = null;
        BufferedReader input = null;
        stdout = new Vector<String>();

        try {
            channel = (ChannelShell) session.openChannel(ChannelType.SHELL.getValue());
            channel.connect();
            input = new BufferedReader(new InputStreamReader(channel.getInputStream()));

            printWriter = new PrintWriter(channel.getOutputStream());
            printWriter.println(command);
            printWriter.println("exit");
            printWriter.flush();

            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
        }catch (Exception e) {
            return -1;
        }finally {
            IoUtil.close(printWriter);
            IoUtil.close(input);
            if(channel != null) {
                channel.disconnect();
            }
        }
        return code;
    }

    public void close(){
        if (session != null) {
            session.disconnect();
        }
    }

    public String executeForResult(String command) {
        execute(command);
        StringBuilder sb = new StringBuilder();
        for (String str : stdout) {
            sb.append(str);
        }
        return sb.toString();
    }

}
