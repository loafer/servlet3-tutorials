package com.github.loafer.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * @author zhaojh
 */
public class User implements HttpSessionBindingListener{
    private static final Logger logger = LoggerFactory.getLogger(User.class);
    private long id;
    private String name;
    private int age;

    public User(String name) {
        this.id = System.currentTimeMillis();
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        logger.info("用户 [{}] 的信息将被绑定在session[{}]中", this.name, event.getSession().getId());
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        logger.info("用户 [{}] 的信息已被从session[{}]中删除", this.name, event.getSession().getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
