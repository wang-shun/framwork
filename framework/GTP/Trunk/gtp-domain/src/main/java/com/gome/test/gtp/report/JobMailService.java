package com.gome.test.gtp.report;

import com.gome.test.gtp.dao.GroupInfoDao;
import com.gome.test.gtp.jenkins.JenkinsBo;
import com.gome.test.gtp.model.*;
import com.gome.test.gtp.utils.Constant;
import com.gome.test.gtp.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wjx on 16/8/25.
 */

/**
 *  1 get send to mail for job mail
 *  2 get mail body for job mail
 *  3 verify for job mail
 */
@Component
public class JobMailService {
    @Autowired
    private Environment env;

    public void sendEmail(String htmlfileContent, List<String> mailToList, List<String> ccList, EmailInfo emailInfo) {
        //装载邮件MimeMessage
        try {

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

    public void sendJobReport(JobReport jobReport  , List<String>mailList , String appName) {
        //邮件内容

        //邮件服务相关公共配置
        EmailInfo emailInfo = configEmailInfo(new EmailInfo());

        //根据是否全部通过、类型、群组等设置邮件主题
        boolean allPassed = true;
        if (jobReport.getPass() < 1 ){
            allPassed = false;
        }else if(jobReport.getPass() < jobReport.getTotalCases()){
            allPassed = false;
        }else if(jobReport.getPass() == jobReport.getTotalCases()){
            allPassed = true;
        }else{
            allPassed = false;
        }
        String subject = String.format("【发版前验证测试】");
        if(allPassed){
            subject+="成功";
        }else {
            subject+="失败";
        }
        String emailContent = getAGReportEmailContent(jobReport , allPassed,appName);
        emailInfo.setSubject(subject);

        emailInfo.setMail_head_info(env.getProperty("gtp.ag.report.email.job.headinfo"));
        emailInfo.setMail_head_value(env.getProperty("gtp.ag.report.email.job.headvalue"));

        //收件人和抄送人
        List<String> toList = new ArrayList<String>();
        List<String> ccList = new ArrayList<String>();
        toList.addAll(mailList);
        // add mail list info.
        if (toList.size()>0) {
            ccList.add(env.getProperty("gtp.email.cclist"));
            sendEmail(emailContent, toList, ccList, emailInfo);
        } else {
            toList.add(env.getProperty("gtp.email.cclist"));
            ccList.add(env.getProperty("gtp.email.cclist"));
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

    private String getAGReportEmailContent(JobReport jobReport , boolean pass , String appName){
        StringBuilder personalList = new StringBuilder();
        personalList.append("<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <title>应用服务接口验证报告</title>\n" +
                "    <style type=\"text/css\">\n" +
                "        body {\n" +
                "            font-size: .75em;\n" +
                "            font-family: Verdana, Helvetica, Sans-Serif;\n" +
                "            margin: 10px;\n" +
                "            padding: 10px;\n" +
                "            color: #696969;\n" +
                "        }\n" +
                "\n" +
                "        .page {\n" +
                "            width: 90%;\n" +
                "            margin-left: auto;\n" +
                "            margin-right: auto;\n" +
                "        }\n" +
                "\n" +
                "        #header {\n" +
                "            position: relative;\n" +
                "            margin-bottom: 0px;\n" +
                "            color: #000;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        div#title {\n" +
                "            display: block;\n" +
                "            float: left;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "\n" +
                "        #main {\n" +
                "            padding: 30px 30px 15px 30px;\n" +
                "            background-color: #FFFFFF;\n" +
                "            margin-bottom: 30px;\n" +
                "            _height: 1px; /* only IE6 applies CSS properties starting with an underscore */\n" +
                "        }\n" +
                "\n" +
                "        h1, h2, h3, h4, h5, h6 {\n" +
                "            font-size: 1.5em;\n" +
                "            color: #000;\n" +
                "            font-family: Arial, Helvetica, sans-serif;\n" +
                "        }\n" +
                "\n" +
                "        h1 {\n" +
                "            font-size: 2em;\n" +
                "            padding-bottom: 0;\n" +
                "            margin-bottom: 0;\n" +
                "        }\n" +
                "\n" +
                "        h2 {\n" +
                "            padding: 0 0 10px 0;\n" +
                "        }\n" +
                "\n" +
                "        h3 {\n" +
                "            font-size: 1.2em;\n" +
                "        }\n" +
                "\n" +
                "        table {\n" +
                "            background-color: #FFFFFF;\n" +
                "            border-right: solid 1px #e8eef4;\n" +
                "            border-bottom: solid 1px #e8eef4;\n" +
                "            width: 98%;\n" +
                "        }\n" +
                "\n" +
                "        table td {\n" +
                "            padding: 5px;\n" +
                "            border-left: solid 1px #e8eef4;\n" +
                "            border-top: solid 1px #e8eef4;\n" +
                "        }\n" +
                "\n" +
                "        table th {\n" +
                "            padding: 6px 5px;\n" +
                "            text-align: left;\n" +
                "            background-color: #e8eef4;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"page\">\n" +
                "    <h2>GTP 应用服务接口验证 测试报告</h2>\n" +
                "    <hr><br>\n" +
                "    <table>\n" +
                "        <tr>\n" +
                "            <td colspan=\"9\">\n" +
                "                <h3>汇总结果</h3>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <th>\n" +
                "                任务ID\n" +
                "            </th>\n" +
                "            <th>\n" +
                "                任务名称\n" +
                "            </th>\n" +
                "            <th>\n" +
                "                用例总数\n" +
                "            </th>\n" +
                "            <th>\n" +
                "                通过用例数\n" +
                "            </th>\n" +
                "            <th>\n" +
                "                失败用例数\n" +
                "            </th>\n" +
                "            <th>\n" +
                "                中断用例数\n" +
                "            </th>\n" +
                "            <th>\n" +
                "                开始时间\n" +
                "            </th>\n" +
                "            <th>\n" +
                "                结束时间\n" +
                "            </th>\n" +
                "        </tr>");
        personalList.append(" <tr>\n" +
                "            <td>\n" +
                "                <b>");
        personalList.append(jobReport.getTaskId());
        personalList.append("</b>\n" +
                "            </td>\n" +
                "            <td>\n" +
                "                <b>");
        personalList.append(appName);
        personalList.append("</b>\n" +
                "            </td>\n" +
                "            <td>");
        personalList.append(jobReport.getTotalCases());
        personalList.append(" </td>\n" +
                "            <td>\n" +
                "                <b><font color=\"green\">");
        personalList.append(jobReport.getPass());
        personalList.append(" </font></b>\n" +
                "            </td>\n" +
                "            <td>\n" +
                "                <b><font color=\"red\">");
        personalList.append(jobReport.getFail());
        personalList.append(" </font></b>\n" +
                "            </td>\n" +
                "            <td>");
        personalList.append(jobReport.getAborted());
        personalList.append(" </td>\n" +
                "            <td>");
        personalList.append(timeStampToStr(jobReport.getStartTime()));
        personalList.append("</td>\n" +
                "            <td>");
        personalList.append(timeStampToStr(jobReport.getEndTime()));
        personalList.append("</td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "    <br><hr><br>\n" );
        //if contain fail case then give the detail to mail.
        if(!pass) {
            personalList.append(
                    "    <table>\n" +
                            "        <tr>\n" +
                            "            <td>\n" +
                            "                <h3>失败用例详情</h3>\n" +
                            "            </td>\n" +
                            "        </tr>");
            personalList.append("  <tr>\n" +
                    "            <td>用例名称:</td>\n" +
                    "            <td>失败信息:</td>\n" +
                    "        </tr>");

            CaseResult results[] = jobReport.getDetails();
            if(results!=null && results.length>0) {
                for (CaseResult result : results) {
                    personalList.append(" <tr>\n" +
                            "            <td>");
                    personalList.append(result.getTestCaseName());
                    personalList.append(" </td>\n" +
                            "            <td>");
                    personalList.append(result.getStackTrace());
                    personalList.append("</td>\n" +
                            "        </tr>");
                }
            }
            personalList.append(" </table>\n");

        }

        personalList.append(
                "</div>\n" +
                "</body>\n" +
                "</html>");
        return personalList.toString();
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

    private String timeStampToStr(Long tl){
        Timestamp timestamp= new Timestamp(tl);
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(timestamp);
    }
}
