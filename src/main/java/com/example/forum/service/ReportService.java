package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    /*
     * レコード全件取得処理
     */
    public List<ReportForm> findAllReport(String start, String end) throws ParseException {
        String setStart = null;
        String setEnd = null;
        SimpleDateFormat simpleDefault= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(StringUtils.isBlank(start)) {
            setStart = "2020-01-01 00:00:00";
        } else {
            setStart = start + " 00:00:00";
        }

        if(StringUtils.isBlank(end)) {
            Date nowDate = new Date();
            setEnd = simpleDefault.format(nowDate);
        } else {
            setEnd = end + " 23:59:59";
        }

        Date startDate = simpleDefault.parse(setStart);
        Date endDate = simpleDefault.parse(setEnd);
        //作成日時を絞り込みかつ、更新日時での降順に設定
        List<Report> results = reportRepository.findAllByCreatedDateBetweenOrderByUpdatedDateDesc(startDate, endDate);
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }
    /*
     * DBから取得したデータをFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setCreatedDate(result.getCreatedDate());
            report.setUpdatedDate(result.getUpdatedDate());
            report.setContent(result.getContent());
            reports.add(report);
        }
        return reports;
    }

    /*
     * レコード追加
     */
    public void saveReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        reportRepository.save(saveReport);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setContent(reqReport.getContent());
        report.setCreatedDate(reqReport.getCreatedDate());
        report.setUpdatedDate(reqReport.getUpdatedDate());
        return report;
    }

    /*
     *  削除処理を追加
     */
    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }

    /*
     * 編集するテキストをデータベースから拾い上げ、controllerに送るメソッド
     */
    public ReportForm editReport(Integer id) {
        List<Report> results = new ArrayList<>();
        results.add((Report) reportRepository.findById(id).orElse(null));
        List<ReportForm> reports = setReportForm(results);
        return reports.get(0);
    }
}
