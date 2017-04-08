package email;

public class MainSendEmail {
	public void sendEmail(){
		MailBean mb = new MailBean();
        mb.setHost("smtp.sina.cn");                        
        mb.setUsername("15754602009m0@sina.cn");                
        mb.setPassword("31415926");                        
        mb.setFrom("15754602009m0@sina.cn");            // 
        mb.setTo("15754602009@163.com");                // 
        mb.setSubject("Code EMail");                    //
        mb.setContent("check code now");        //

//        mb.attachFile("E://sample.doc");            
//        mb.attachFile("E:/test.txt");
//        mb.attachFile("E:/test.xls");
        
        SendMail sm = new SendMail();
        System.out.println("开始发送邮件");
        
        if(sm.sendMail(mb))                                //
            System.out.println("发送成功!");
        else
            System.out.println("发送失败!");
    
	}
}
