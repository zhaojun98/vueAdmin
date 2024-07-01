package com.yl.utils;

import com.yl.model.dto.MailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ：jerry
 * @date ：Created in 2021/10/29 16:17
 * @description：邮件工具类
 * @version: V1.1
 */
@Component
public class MailUtil {
    @Value("${spring.mail.username}")
    private String MAIL_SENDER; //邮件发送者

    @Resource
    private JavaMailSender javaMailSender;

    private Logger logger = LoggerFactory.getLogger(MailUtil.class);

    /**
     * 发送文本邮件
     *
     * @param mail
     */
    public  Boolean sendSimpleMail(MailDto mail) {
        try {
            SimpleMailMessage mailMessage= new SimpleMailMessage();
            mailMessage.setFrom(MAIL_SENDER);
            mailMessage.setTo(mail.getRecipient());
            mailMessage.setSubject(mail.getSubject());
            mailMessage.setText(mail.getContent());
            //mailMessage.copyTo(copyTo);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            logger.error("邮件发送失败", e.getMessage());
            return false;
        }
    }
}