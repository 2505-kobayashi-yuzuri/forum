package com.example.forum.controller.form;

import lombok.Getter;
import lombok.Setter;
//Viewにアクセスするときに一時保管するbeanのようなもの
@Getter
@Setter
public class CommentForm {

    private int id;
    private int message_id;
    private String content;
}