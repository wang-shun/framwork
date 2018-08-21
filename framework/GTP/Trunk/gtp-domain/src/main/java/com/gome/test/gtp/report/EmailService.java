package com.gome.test.gtp.report;

import com.gome.test.gtp.dao.GroupInfoDao;
import com.gome.test.gtp.jenkins.JenkinsBo;
import com.gome.test.gtp.model.*;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.*;

/**
 * Created by lizonglin on 2016/4/26/0026.
 */
@Service
public class EmailService {
    @Autowired
    private JenkinsBo jenkinsBo;
    @Autowired
    private Environment env;
    @Autowired
    private GroupInfoDao groupInfoDao;

    public void sendEmail(String htmlfileContent, List<String> mailToList, List<String> ccList, EmailInfo emailInfo) {
        //装载邮件MimeMessage
        try {
            /**
             * 设置公共参数
             */
//            emailInfo = configEmailInfo(emailInfo);
            /**
             * 获取系统环境
             */
            Properties props = new Properties();
            EmailAutherticator auth = new EmailAutherticator(emailInfo.getUsername(), emailInfo.getPassword());
            props.put("mail.smtp.host", emailInfo.getServerHost());
            /**
             * 自动后成发送人邮件!!
             */
            props.put("mail.smtp.starttls.enable", "true");   //gmail发时:
            props.put("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(props, auth);
            /**
             * 设置session,和邮件服务器进行通讯
             */
            MimeMessage message = new MimeMessage(session);
            Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件
            BodyPart bodyPart = new MimeBodyPart();// 正文
            bodyPart.setDataHandler(new DataHandler(htmlfileContent, "text/html;charset=UTF-8"));// 网页格式

            mp.addBodyPart(bodyPart);
            message.setContent(mp);//
            message.setSubject(emailInfo.getSubject());//设置邮件主题
            message.setHeader(emailInfo.getMail_head_info(), emailInfo.getMail_head_value());//设置邮件标题
            message.setSentDate(new Date());//设置邮件发送时期
            Address address = new InternetAddress(emailInfo.getMail_from(), emailInfo.getPersonalName());//发件人地址 发件人名称
            message.setFrom(address);//设置邮件发送者的地址
            if (!mailToList.isEmpty()) {
                for (String mailTo : mailToList) {
                    Address toaddress = new InternetAddress(mailTo); //设置邮件接收者的地址
                    message.addRecipient(Message.RecipientType.TO, toaddress);
                }
            }
            if (!ccList.isEmpty()) {
                for (String cc : ccList) {
                    Address toaddress = new InternetAddress(cc); //设置邮件抄送者的地址
                    message.addRecipient(Message.RecipientType.CC, toaddress);
                }
            }
            //发送邮件
            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAGReport(AGReportEmail agReportEmail) {
        //邮件内容
        String emailContent = getAGReportEmailContent(agReportEmail);
        //邮件服务相关公共配置
        EmailInfo emailInfo = configEmailInfo(new EmailInfo());

        //根据是否全部通过、类型、群组等设置邮件主题
        boolean allPassed = true;
        if (agReportEmail.getTotalCaseNum() > 0 && agReportEmail.getTotalPassNum() < agReportEmail.getTotalCaseNum())
            allPassed = false;
        String subject = String.format("【GTP每日报告】--%s测试(%d)--%s", agReportEmail.getReportType(), agReportEmail.getDate(), allPassed ? "全部用例通过" : "部分用例失败");
        emailInfo.setSubject(subject);

        emailInfo.setMail_head_info(env.getProperty("gtp.ag.report.email.api.headinfo"));
        emailInfo.setMail_head_value(env.getProperty("gtp.ag.report.email.api.headvalue"));

        //收件人和抄送人
        List<String> toList = new ArrayList<String>();
        List<String> ccList = new ArrayList<String>();

        GroupInfo groupInfo = groupInfoDao.getEmailByGroupName(agReportEmail.getGroupName());
        emailInfo = configEmailInfo(emailInfo);
        if (groupInfo != null) {
            toList.add(groupInfo.getEmail());
            toList.add(groupInfo.getLeader());
            ccList.add(env.getProperty("gtp.email.cclist"));
            sendEmail(emailContent, toList, ccList, emailInfo);
        } else {
            toList.add(env.getProperty("gtp.email.cclist"));
            ccList.add(env.getProperty("gtp.email.cclist"));
            emailInfo.setSubject("群组收件人有误-" + emailInfo.getSubject());
            sendEmail(emailContent, toList, ccList, emailInfo);
        }
    }

    public EmailInfo configEmailInfo(EmailInfo emailInfo) {
        emailInfo.setMail_from(env.getProperty("gtp.email.from").trim());
        emailInfo.setPersonalName(env.getProperty("gtp.email.personalname").trim());
        emailInfo.setServerHost(env.getProperty("gtp.email.serverhost").trim());
        emailInfo.setUsername(env.getProperty("gtp.email.username").trim());
        emailInfo.setPassword(env.getProperty("gtp.email.password").trim());
        return emailInfo;
    }

    private String getAGReportEmailContent(AGReportEmail agReportEmail){
        String emailContent = "";
        Map<String, String> repMap = new HashMap<String, String>();

        repMap.put(Constant.AG_REPORT_EMAIL_TEST_TYPE, agReportEmail.getReportType());
        repMap.put(Constant.AG_REPORT_EMAIL_GROUP, agReportEmail.getGroupName());
        repMap.put(Constant.AG_REPORT_EMAIL_TOTAL, String.valueOf(agReportEmail.getTotalCaseNum()));
        repMap.put(Constant.AG_REPORT_EMAIL_PASS, String.valueOf(agReportEmail.getTotalPassNum()));
        repMap.put(Constant.AG_REPORT_EMAIL_FAIL, String.valueOf(agReportEmail.getTotalFailNum()));
        repMap.put(Constant.AG_REPORT_EMAIL_PASSRATE, agReportEmail.getPassRate());

        StringBuilder personalList = new StringBuilder();
        for (AGReport agReport : agReportEmail.getPersonalReportList()) {
            personalList.append("<tr>");
            personalList.append("<td>");
            personalList.append(agReport.getOwner());
            personalList.append("</td>");
            personalList.append("<td>");
            personalList.append(agReport.getCaseNum());
            personalList.append("</td>");
            personalList.append("<td><b><font color='green'>");
            personalList.append(agReport.getPassNum());
            personalList.append("</font></b></td>");
            personalList.append("<td><b><font color='red'");
            personalList.append(agReport.getCaseNum() - agReport.getPassNum());
            personalList.append("</font></b></td>");
            personalList.append("<td>");
            personalList.append(Util.computePercent(agReport.getPassNum(), agReport.getCaseNum(), 2));
            personalList.append("</td>");
            personalList.append("<td>");
            personalList.append(breakPersonalFailCases(agReport.getFailCase()));
            personalList.append("</td>");
            personalList.append("</tr>");
        }

        repMap.put(Constant.AG_REPORT_EMAIL_PERSONALLIST, personalList.toString());
        try {
            emailContent = jenkinsBo.replaceFileFromResourceName(repMap, Constant.AG_REPORT_EMAIL_TEMPLATE);
            return emailContent;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return emailContent;
    }

    private String breakPersonalFailCases(String personalFailCases) {
        StringBuilder breakCases = new StringBuilder();
        String[] failCases = personalFailCases.split(",");
        for (String failCase : failCases) {
            breakCases.append("<p>");
            breakCases.append(failCase);
            breakCases.append("</p>");
        }
        return breakCases.toString();
    }
}
