package com.yl.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：jerry
 * @date ：Created in 2021/10/29 16:15
 * @description：邮件Bean
 * @version: V1.1
 */
@Data
public class MailDto implements Serializable {
    private static final long serialVersionUID = -2116367492649751914L;

    private String recipient;//邮件接收人

    private String subject; //邮件主题

    private String content; //邮件内容

}