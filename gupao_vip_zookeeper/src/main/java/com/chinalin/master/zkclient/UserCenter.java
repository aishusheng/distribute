package com.chinalin.master.zkclient;

import java.io.Serializable;

/**
 * @date:2019/02/19 14:14
 * @author:艾书胜
 * 使用zkclient来操作zookeeper实现master选举
 * 当前类模拟一个用户中心应用程序来连接zookeeper中创建的master临时节点
 */
public class UserCenter implements Serializable {

    private static final long serialVersionUID = 6882363771500180475L;

    private int pc_id;//机器信息

    private String pc_name;//机器名称

    public int getPc_id() {
        return pc_id;
    }

    public void setPc_id(int pc_id) {
        this.pc_id = pc_id;
    }

    public String getPc_name() {
        return pc_name;
    }

    public void setPc_name(String pc_name) {
        this.pc_name = pc_name;
    }
}
