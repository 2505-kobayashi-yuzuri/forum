package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
//サーブレットのようなもので処理の中心になる
@Controller
public class ForumController {
    @Autowired
    ReportService reportService;

    @Autowired
    CommentService commentService;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top(@ModelAttribute("start") String start, @ModelAttribute("end")  String end) throws ParseException {
        ModelAndView mav = new ModelAndView();
        // 投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport(start, end);
        //返信の全件取得
        List<CommentForm> commentData = commentService.findAllComment();
        CommentForm commentForm = new CommentForm();
        mav.addObject("commentModel", commentForm);
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        // 返信データのオブジェクトを保管
        mav.addObject("comments", commentData);
        return mav;
    }

    @PostMapping("/addComment")
    public ModelAndView addComment(@Validated @ModelAttribute("commentModel") CommentForm commentForm, BindingResult result,
                                   @ModelAttribute("start") String start, @ModelAttribute("end")  String end) throws ParseException {
        ModelAndView mav = new ModelAndView();
        if(result.hasErrors()) {
            List<ReportForm> contentData = reportService.findAllReport(start, end);
            //返信の全件取得
            List<CommentForm> commentData = commentService.findAllComment();
            mav.addObject("commentModel", commentForm);
            // 画面遷移先を指定
            mav.setViewName("/top");
            // 投稿データオブジェクトを保管
            mav.addObject("contents", contentData);
            // 返信データのオブジェクトを保管
            mav.addObject("comments", commentData);
            return mav;
        }
        // コメントをテーブルに格納
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@Validated @ModelAttribute("formModel") ReportForm reportForm, BindingResult result) {
        // 投稿をテーブルに格納
        if(result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/new");
            return mav;
        }
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        reportService.deleteReport(id);
        return new ModelAndView("redirect:/");
    }

    /*
     * コメント削除処理
     */
    @DeleteMapping("/deleteComment/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return new ModelAndView("redirect:/");
    }

    /*
     *　編集画面の表示
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id){
        ModelAndView mav = new ModelAndView();
        //idに結びついている投稿内容を取得
        ReportForm report = reportService.editReport(id);
        //取得した投稿内容を加え
        mav.addObject("formModel", report);
        //画面遷移とともに投稿内容を送る
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * 編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent ( @PathVariable Integer id,
                                       @ModelAttribute("formModel") @Validated ReportForm report, BindingResult result) {
        if(result.hasErrors()) {
            return new ModelAndView("/edit");
        }
        // UrlParameterのidを更新するentityにセット
        report.setId(id);
        // 編集した投稿を更新
        reportService.saveReport(report);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     *　コメント編集画面の遷移
     */
    @GetMapping("/editComment/{id}")
    public ModelAndView editComment(@PathVariable Integer id){
        ModelAndView mav = new ModelAndView();
        //idに結びついている投稿内容を取得
        CommentForm comment = commentService.editComment(id);
        //取得した投稿内容を加え
        mav.addObject("commentModel", comment);
        //画面遷移とともに投稿内容を送る
        mav.setViewName("/editComment");
        return mav;
    }

    /*
     *　コメント編集を受け取りserviceに流す処理
     */
    @PutMapping("/editUpdate/{id}")
    public ModelAndView updateContent (@PathVariable Integer id,
                                       @ModelAttribute("commentModel") @Validated CommentForm comment, BindingResult result) {
        if(result.hasErrors()) {
            return new ModelAndView("/editComment");
        }
        // UrlParameterのidを更新するentityにセット
        comment.setId(id);
        // 編集した投稿を更新
        commentService.saveComment(comment);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}
