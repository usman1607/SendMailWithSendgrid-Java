package com.goodgold.sendgrid.services;

import com.goodgold.sendgrid.Models.MailRequest;
import com.sendgrid.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.io.IOException;

@Service
public class MailService implements IMailService {

    private final Environment env;
    private final ServletContext context;

    public MailService(Environment env, ServletContext context) {
        this.env = env;
        this.context = context;
    }

    @Override
    public boolean sendMail(HttpServletRequest requestUrl, MailRequest request) throws IOException {
        Email from = new Email(request.getSenderEmail());
        Email to = new Email(request.getReceiverEmail());
        String token = UUID.randomUUID().toString();
        var url = ServletUriComponentsBuilder.fromRequestUri(requestUrl)
                .replacePath(null)
                .build()
                .toUriString();
        var contentPath = url+"/users/resetPassword?token="+token;
        String body = "<html>" +
                "<body><p>Dear "+request.receiverName+"</p><p>Please click the link below to reset your password.</p>" +
                "<a href='"+contentPath+"'>Reset password</a></body></html>";
        Content content = new Content("text/html", body);
        Mail mail = new Mail(from, request.getSubject(), to, content);


        /*if(request.getAttachments().size() != 0){
            for(var file : request.getAttachments()){
                Attachments attachment = new Attachments();
                Base64 x = new Base64();
                String fileDataString = x.encodeAsString(file.getBytes());
                attachment.setContent(fileDataString);
                attachment.setType(file.getContentType());
                attachment.setFilename(file.getName());
                attachment.setDisposition("attachment");
                attachment.setContentId("Banner");
                mail.addAttachments(attachment);
            }
        }*/

        String key = env.getProperty("SENDGRID_API_KEY");
        SendGrid sg = new SendGrid(key);
        Request mailRequest = new Request();
        try {
            mailRequest.setMethod(Method.POST);
            mailRequest.setEndpoint("mail/send");
            mailRequest.setBody(mail.build());
            Response response = sg.api(mailRequest);


            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());

        } catch (IOException ex) {
            throw ex;
        }
        return true;
    }
}
