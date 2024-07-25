package com.example.CiryllicToLatin.controller;

import com.example.CiryllicToLatin.dto.ClientDto;
import com.example.CiryllicToLatin.parser.FromXmlToTxt;

import com.example.CiryllicToLatin.service.FilesService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Controller
@RequestMapping("/v1/converter")
public class CiryllicToLatynConventerController {
    @Autowired
    FromXmlToTxt fromXmlToTxt;
    @Autowired
    FilesService filesService;

    @GetMapping
    public String converter(Model model) {
        model.addAttribute("canDownload", false);
        return "converter";
    }

    @PostMapping
    public String converterPost(Model model, @RequestParam MultipartFile file) throws IOException {
        filesService.saveFile(file);
        List<ClientDto> clients = fromXmlToTxt.convert("files/" + file.getOriginalFilename());
        List<ClientDto> convertedClients = filesService.test(clients);

        filesService.writeIntoTxt(convertedClients);

        model.addAttribute("canDownload", true);
        return "converter";
    }

    @GetMapping("/download")
    public String download(
            HttpServletResponse response
    ) throws IOException {
        filesService.download(response);
        return "redirect:converter";
    }



}
