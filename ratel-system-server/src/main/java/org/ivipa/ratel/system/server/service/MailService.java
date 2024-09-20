package org.ivipa.ratel.system.server.service;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.system.common.utils.SystemError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Duration;

@Service
@Slf4j
public class MailService {

    public final static String KEY_MAIL_VERIFICATION_FOLDER = "mail-verification";
    public final static int MAIL_VERIFICATION_EXPIRE_PERIOD = 600;
    public final static String VERIFICATION_MAIL_SUBJECT = "Mail verification";

    @Resource(name = "systemRedisTemplate")
    protected RedisTemplate systemRedisTemplate;

    @Value("${spring.mail.from}")
    private String from;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;



    public void sendVerificationCode(String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
        String code = String.valueOf(RandomUtil.randomInt(123456, 999999));

        log.info("Code is {}", code);
        Context context = new Context();
        context.setVariable("emailAddress", to);
        context.setVariable("verificationCode", code);
        String content = templateEngine.process("/mail", context);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(VERIFICATION_MAIL_SUBJECT);
        mimeMessageHelper.setText(content, true);
        mailSender.send(mimeMessageHelper.getMimeMessage());
        systemRedisTemplate.opsForValue().set(KEY_MAIL_VERIFICATION_FOLDER + ":" + to, code, Duration.ofMillis(MAIL_VERIFICATION_EXPIRE_PERIOD * 1000));
        log.info("Mail is sent to {},with code = {} and content = {}", to, code, content);
    }

    public void verifyMail(String to, String verificationCode) {
        if(verificationCode == null || verificationCode.isEmpty() || verificationCode.length() != 6) {
            throw SystemError.VERIFICATION_CODE_IS_INVALID.newException();
        }
        String code = (String)systemRedisTemplate.opsForValue().get(KEY_MAIL_VERIFICATION_FOLDER + ":" + to);
        if(!verificationCode.equals(code)) {
            throw SystemError.VERIFICATION_CODE_NOT_FOUND.newException();
        }
    }

}
