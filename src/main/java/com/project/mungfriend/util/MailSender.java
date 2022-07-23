package com.project.mungfriend.util;

import com.project.mungfriend.enumeration.MailType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

// 회원 가입 시 signup 로직에 해당 static 메서드 호출 필요! (회원가입 축하 메일)
@Component
public class MailSender {

    private static final String user = "mungfriend_official@naver.com";
    private static String password;

    @Value("${spring.mail.password}")
    public void setPassword(String value){
        password = value;
    }

    // 해당 메서드의 파라미터에는 받는 사람의 이메일 주소, 메일 제목, 메일 본문을 넣어준다.
    public static void sendMail(String receiverMailAddr, String subject, String receiverNickname, MailType type){
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.naver.com");
        prop.put("mail.smtp.port", 587);
        prop.put("mail.smtp.auth", "true");
        // ssl protocols 설정 해줘야함
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        prop.put("mail.smtp.tls.enable", "true");

        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));

            //수신자 메일 주소
            System.out.println("receiverMailAddr = " + receiverMailAddr);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverMailAddr));
            //제목
            message.setSubject(subject);

            String html = setContent(receiverNickname, type);

            //본문 HTML, TEXT
            Multipart mParts = new MimeMultipart();
            MimeBodyPart mTextPart = new MimeBodyPart();
            MimeBodyPart mFilePart = null;

            mTextPart.setText(html, "UTF-8", "html");
            mParts.addBodyPart(mTextPart);
            message.setContent(mParts);
//            message.setText(text);

            // MIME 타입 설정
            MailcapCommandMap mailCapCmdMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mailCapCmdMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mailCapCmdMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mailCapCmdMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mailCapCmdMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mailCapCmdMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mailCapCmdMap);

            //메일 보내기
            Transport.send(message);
            System.out.println("메일 전송 완료!");

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public static String setContent(String receiverNickname, MailType type){

        StringBuffer sb = new StringBuffer();

        // 타입에 따라 메일에 출력할 HTML 추가
        if (type == MailType.NORMAL_SIGNUP){
            sb.append("<img src='https://ifh.cc/g/46C0XH.jpg'>\n");
            sb.append("<h4>")
                    .append("마이페이지 > 프로필수정 > 휴대폰번호 인증 진행 후, 반려견 산책 매칭 서비스 이용이 가능합니다.")
                    .append("</h4>\n");
            sb.append("<h5>")
                    .append("반려견 산책 매칭 서비스 멍친구와 함께 즐거운 추억을 만들어보세요😊!")
                    .append("</h5>\n");
            sb.append("<a href='https://mungfriend.com' style=\"text-decoration-line:none\">🐶 멍친구로 바로가기</a>\n");
        } else if (type == MailType.SOCIAL_SIGNUP){
            sb.append("<img src='https://ifh.cc/g/46C0XH.jpg'>\n");
            sb.append("<h4>")
                    .append("마이페이지 > 프로필수정 > 휴대폰번호, 주소, 전체 약관 동의 체크 진행 후, 반려견 산책 매칭 서비스 이용이 가능합니다.")
                    .append("</h4>\n");
            sb.append("<h5>")
                    .append("반려견 산책 매칭 서비스 멍친구와 함께 즐거운 추억을 만들어보세요😊!")
                    .append("</h5>\n");
            sb.append("<a href='https://mungfriend.com' style=\"text-decoration-line:none\">🐶 멍친구로 바로가기</a>\n");
        } else if (type == MailType.MATCH_COMPLETED){
            sb.append("<img src='https://ifh.cc/g/063rFs.jpg'>\n");
            sb.append("<h4>")
                    .append("[매칭 후 이용 안내]").append("</h4>\n");
            sb.append("<h4>")
                    .append("메인 페이지 우측 상단의 말풍선을 클릭하여 매칭된 상대방과의 채팅을 통해 상세 일정을 조율해주세요.😉").append("</h4>\n");
            sb.append("<h4>")
                    .append("개인 사정 상 산책을 못하게 될 경우 채팅을 통해 멍친구에게 사전에 반드시 알려주세요.😥").append("</h4>\n");
            sb.append("<br>");
            sb.append("<h5>")
                    .append("[산책 시 유의 사항]").append("</h5>\n");
            sb.append("<h5>")
                    .append("‼산책은 반드시 신청자 본인이 직접 해주세요.").append("</h5>\n");
            sb.append("<h5>")
                    .append("‼산책시켜 줄 멍멍이의 멍프로필을 클릭하여 특이 사항을 꼭 읽어주세요.").append("</h5>\n");
            sb.append("<h5>")
                    .append("‼멍멍이가 산책 중 아무거나 먹지 않도록 주의해주세요.").append("</h5>\n");
            sb.append("<h5>")
                    .append("‼멍멍이의 안전과 사고 예방을 위해 리드줄을 꼭 착용해주세요.").append("</h5>\n");
            sb.append("<h5>")
                    .append("‼산책 시 멍멍이가 배변을 하면 꼭 배변 봉투에 담아 처리를 해주세요.").append("</h5>\n");
            sb.append("<h5>")
                    .append("‼멍멍이가 입질이 심한 경우 입마개 착용을 권장합니다.").append("</h5>\n");
            sb.append("<a href='https://mungfriend.com' style=\"text-decoration-line:none\">🐶 멍친구로 바로가기</a>\n");
        } else if (type == MailType.MATCH_CANCELED){
            sb.append("<img src='https://ifh.cc/g/VFOWvF.png'>\n");
            sb.append("<h4>")
                    .append("하단의 링크를 통해 다른 멍친구들의 요청글을 확인해보세요.🐶")
                    .append("</h4>\n");
            sb.append("<a href='https://mungfriend.com' style=\"text-decoration-line:none\">🐶 멍친구로 바로가기</a>\n");
        }

        return sb.toString();
    }
}
