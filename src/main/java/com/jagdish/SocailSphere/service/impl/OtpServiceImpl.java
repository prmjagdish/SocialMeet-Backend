package com.jagdish.SocailSphere.service.impl;

import com.jagdish.SocailSphere.model.dto.AuthRequest;
import com.jagdish.SocailSphere.model.entity.User;
import com.jagdish.SocailSphere.repository.UserRepository;
import com.jagdish.SocailSphere.service.OtpService;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpServiceImpl implements OtpService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, AuthRequest> signupStorage = new ConcurrentHashMap<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${FROM_EMAIL}")
    private String fromEmail;

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Override
    public String generateOtp() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    @Override
    public void saveSignupAndSendOtp(AuthRequest request) {
        String otp = generateOtp();

        otpStorage.put(request.getEmail(), otp);
        signupStorage.put(request.getEmail(), request);

        Email from = new Email(fromEmail);
        Email to = new Email(request.getEmail());
        Content content = new Content("text/plain",  "Hi,\n\n"
                + "Someone tried to sign up for a Social Meet account\n"
                + "with " + request.getEmail() + ". If it was you, enter this\n"
                + "confirmation code in the app:\n\n"
                + otp + "\n\n"
                + "This code will expire in 5 minutes.");
        Mail mail = new Mail(from, otp + " is your Social Meet code", to, content);
        SendGrid sg = new SendGrid(sendGridApiKey);

        try {
            Request requestMail = new Request();
            requestMail.setMethod(Method.POST);
            requestMail.setEndpoint("mail/send");
            requestMail.setBody(mail.build());

            Response response = sg.api(requestMail);
            System.out.println("Email Status: " + response.getStatusCode());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyOtpAndRegister(String email, String otp) {
        if (!otp.equals(otpStorage.get(email))) {
            return false;
        }

        AuthRequest request = signupStorage.get(email);

        otpStorage.remove(email);
        signupStorage.remove(email);

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(newUser);

        return true;
    }
}
