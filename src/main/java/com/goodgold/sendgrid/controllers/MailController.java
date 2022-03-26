package com.goodgold.sendgrid.controllers;

import com.goodgold.sendgrid.Models.MailRequest;
import com.goodgold.sendgrid.services.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class MailController {
    final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/compose")
    public String create() {
        return "mail/compose";
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String send(HttpServletRequest requestUrl, Model model, MailRequest request) throws IOException {

        var result = mailService.sendMail(requestUrl, request);
        if(result){
            return "mail/success";
        }
        return "redirect:/compose";
    }
}
