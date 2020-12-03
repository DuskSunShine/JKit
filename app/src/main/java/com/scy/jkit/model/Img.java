package com.scy.jkit.model;

import java.io.Serializable;

/**
 * @author: SCY
 * @date: 2020/11/27   15:11
 * @version:
 * @desc:
 */
public class Img implements Serializable {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Img{" +
                "id=" + id +
                '}';
    }
}
