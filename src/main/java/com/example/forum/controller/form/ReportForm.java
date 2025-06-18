package com.example.forum.controller.form;

import lombok.Getter;
import lombok.Setter;
//Viewにアクセスするときに一時保管するbeanのようなもの
@Getter
@Setter
public class ReportForm {

    private int id;
    private String content;
    private String createdDate;
}
