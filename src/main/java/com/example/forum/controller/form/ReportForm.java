package com.example.forum.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

//Viewにアクセスするときに一時保管するbeanのようなもの
@Getter
@Setter
public class ReportForm {

    private int id;
    private String content;
    private Date createdDate;
}
