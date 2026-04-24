package hu.kalmancheysandor.applications.dominions.servers.mailing.service;


import hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.dto.SiteAccountRecoveryVerificationMailEnqueueRequest;
import hu.kalmancheysandor.applications.dominions.apis.server.mailing.shared.dto.SiteAccountSignUpVerificationMailEnqueueRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@Transactional
@Service
public class SiteAccountMailDeliveryService {

    @Autowired
    private JavaMailSender mailSender;

    public void enqueueSiteAccountSignUpVerificationMail(SiteAccountSignUpVerificationMailEnqueueRequest request) {

        String verificationLink = "https://game.dominions.hu/account/signup/verification/" + request.getVerificationToken();
        String htmlTemplate = """
                <html>
                  <body style="margin:0;padding:20px;font-family:Arial,sans-serif;background-color:#f9f9f9;">
                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                      <tr>
                        <td align="center">
                          <table width="600" cellpadding="20" cellspacing="0" border="0" style="background-color:#ffffff;border-radius:8px;">
                            <tr>
                              <td>
                                <p>Dear ${UserName}, to complete your registration, please click the link below!
                                <p>
                                  <a href="${VerificationLink}" style="background-color:transparent;color:#0052cc;padding:10px 20px;text-decoration:none;">${VerificationLink}</a>
                                </p>
                                <hr style="border:none;border-top:1px solid #eee;">
                                <small style="color:#999;">Ez egy automatikus üzenet.</small>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """;

        String htmlContent = htmlTemplate
                .replace("${UserName}", request.getUserName())
                .replace("${VerificationLink}", verificationLink);
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // true: multipart message (HTML + attachments, stb.)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(request.getMailDeliveryData().getRecipientEmail());
            helper.setSubject(request.getMailDeliveryData().getSubject());
            helper.setText(htmlContent, true);
            helper.setFrom(request.getMailDeliveryData().getSenderEmail(), "Dominions");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


    }


    public void enqueueSiteAccountRecoveryVerificationMail(SiteAccountRecoveryVerificationMailEnqueueRequest request) {

        String verificationLink = "https://game.dominions.hu/account/recovery/submit/" + request.getVerificationToken();
        String htmlTemplate = """
                <html>
                  <body style="margin:0;padding:20px;font-family:Arial,sans-serif;background-color:#f9f9f9;">
                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                      <tr>
                        <td align="center">
                          <table width="600" cellpadding="20" cellspacing="0" border="0" style="background-color:#ffffff;border-radius:8px;">
                            <tr>
                              <td>
                                <p>Dear ${UserName}, to continue your account recovery, please click the link below!
                                <p>
                                  <a href="${VerificationLink}" style="background-color:transparent;color:#0052cc;padding:10px 20px;text-decoration:none;">${VerificationLink}</a>
                                </p>
                                <hr style="border:none;border-top:1px solid #eee;">
                                <small style="color:#999;">Ez egy automatikus üzenet.</small>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """;

        String htmlContent = htmlTemplate
                .replace("${UserName}", request.getUserName())
                .replace("${VerificationLink}", verificationLink);
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // true: multipart message (HTML + attachments, stb.)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(request.getMailDeliveryData().getRecipientEmail());
            helper.setSubject(request.getMailDeliveryData().getSubject());
            helper.setText(htmlContent, true);
            helper.setFrom(request.getMailDeliveryData().getSenderEmail(), "Dominions");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


    }


}
