package com.sdlc.pro.mymbstu.controller;

import com.sdlc.pro.mymbstu.service.DiaryImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/import-diary")
public class DiaryImportController {

    @Autowired
    private DiaryImportService diaryImportService;

    @GetMapping
    public String importCsv() {
        //diaryImportService.importCsv();
        return "CSV Import Completed Successfully!";
    }
}