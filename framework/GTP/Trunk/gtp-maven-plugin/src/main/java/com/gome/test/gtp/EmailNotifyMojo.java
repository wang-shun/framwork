package com.gome.test.gtp;

import com.gome.test.gtp.utils.Constant;
import com.gome.test.utils.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.springframework.boot.test.SpringApplicationConfiguration;

/**
 * @Mojo(name = "gtp")
 * @goal email
 * @requiresDependencyResolution compile+runtime
 * @requiresProject false
 * Created by lizonglin on 2016/1/22/0022.
 */
@SpringApplicationConfiguration(classes = Application.class)
public class EmailNotifyMojo extends AbstractMojo{
    /**
     * @parameter
     * expression="${taskListId}"
     */
    private int taskListId;

    /**
     * @parameter
     * expression="${taskType}"
     */
    private int taskType;

    private TaskInfoBo taskInfoBo = new TaskInfoBo();
    private EmailNotifyBo emailNotifyBo = new EmailNotifyBo();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {


        /**
         * 根据所有拆分的TaskList状态决定是否发送邮件
         */
        if (Constant.END_STATUS_SET.contains(taskInfoBo.getRealStatusByTaskListId(taskListId))) {
            try {
                emailNotifyBo.endMojoSendEmail(taskListId, taskType);
                emailNotifyBo.updateTaskListLogAfterEmail(taskListId);
            } catch (Exception e) {
                Logger.error("发送邮件失败！");
                Logger.error(e.getMessage());
//                System.out.println("发送邮件失败！");
//                log.append(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String []args ){
        EmailNotifyMojo emailNotifyMojo = new EmailNotifyMojo();
        emailNotifyMojo.taskListId=19658;
        emailNotifyMojo.taskType=2;
        try {
            emailNotifyMojo.execute();
        } catch (MojoExecutionException e) {
            e.printStackTrace();
        } catch (MojoFailureException e) {
            e.printStackTrace();
        }
    }
}
