package com.example.forum.controller;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.ReportService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
//サーブレットのようなもので処理の中心になる
@Controller
public class ForumController {
    @Autowired
    ReportService reportService;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        // 投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        return mav;
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
    public ModelAndView addContent(@ModelAttribute("formModel") ReportForm reportForm){
        // 投稿をテーブルに格納
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
    public ModelAndView updateContent (@PathVariable Integer id,
                                       @ModelAttribute("formModel") ReportForm report) {
        // UrlParameterのidを更新するentityにセット
        report.setId(id);
        // 編集した投稿を更新
        reportService.saveReport(report);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
    /*
     * 返信をHTMLから受け取るメソッド
     */
    //@PostMapping("/comment")
    //public ModelAndView commentContent(@ModelAttribute("commentModel") ReportForm reportForm){
        // 投稿をテーブルに格納
        //reportService.saveReport(reportForm);
        // rootへリダイレクト
        //return new ModelAndView("redirect:/");
//    }

}
