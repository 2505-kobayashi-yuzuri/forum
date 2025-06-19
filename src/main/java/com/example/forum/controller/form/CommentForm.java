package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
//Viewにアクセスするときに一時保管するbeanのようなもの
@Getter
@Setter
public class CommentForm {

    private int id;
    private int message_id;
    @NotBlank(message ="コメントを入力してください")
    private String content;
}