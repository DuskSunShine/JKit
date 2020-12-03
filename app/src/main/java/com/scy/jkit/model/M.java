package com.scy.jkit.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author: SCY
 * @date: 2020/11/23   11:27
 * @version:
 * @desc:
 */
public class M implements Serializable {

    private int id;
    private String course_title;
    private int education_id;
    private int pro_id;
    private int course_id;
    private String create_time;
    private List<M> list;

    public List<M> getList() {
        return list;
    }

    public void setList(List<M> list) {
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public int getEducation_id() {
        return education_id;
    }

    public void setEducation_id(int education_id) {
        this.education_id = education_id;
    }

    public int getPro_id() {
        return pro_id;
    }

    public void setPro_id(int pro_id) {
        this.pro_id = pro_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "M{" +
                "id=" + id +
                ", course_title='" + course_title + '\'' +
                ", education_id=" + education_id +
                ", pro_id=" + pro_id +
                ", course_id=" + course_id +
                ", create_time='" + create_time + '\'' +
                ", list=" + list +
                '}';
    }
}
