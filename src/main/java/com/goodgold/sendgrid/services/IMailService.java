package com.goodgold.sendgrid.services;

import com.goodgold.sendgrid.Models.MailRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface IMailService {
    boolean sendMail(HttpServletRequest requestUrl, MailRequest request) throws IOException;
}
