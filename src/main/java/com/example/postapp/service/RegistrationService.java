package com.example.postapp.service;

import com.example.postapp.email.EmailSenderService;
import com.example.postapp.email.EmailValidator;
import com.example.postapp.entity.UserEntity;
import com.example.postapp.repository.ConfirmationTokenRepository;
import com.example.postapp.repository.UserRepository;
import com.example.postapp.request_response_entity.registration.RegistrationRequest;
import com.example.postapp.request_response_entity.registration.RegistrationServiceResponse;
import com.example.postapp.service.token.ConfirmationToken;
import com.example.postapp.service.token.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.example.postapp.entity.Status.BLOCKED;
import static com.example.postapp.entity.role_permissions.Role.USER;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final UserEntityService userEntityService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderService emailSenderService;

    @Autowired
    public RegistrationService(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               EmailValidator emailValidator,
                               UserEntityService userEntityService,
                               ConfirmationTokenRepository confirmationTokenRepository,
                               EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailValidator = emailValidator;
        this.userEntityService = userEntityService;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSenderService = emailSenderService;
    }

    public RegistrationServiceResponse userExist (String email, String password) {

        UserEntity user = userRepository.findUserByEmail(email)
                .orElseThrow(NoSuchElementException::new);

        if (passwordEncoder.matches(password, user.getPassword()))
            return new RegistrationServiceResponse(user, true);

        return new RegistrationServiceResponse(user, false);
    }

    public ResponseEntity<Object> register (RegistrationRequest request) {

        String email = request.getEmail();

        if (email == null) {
            throw new IllegalStateException("email must be not null");
        }

        if (!emailValidator.isValid(email)) {
            throw new IllegalStateException("invalid email");
        }

        if (!userEntityService.notExist(email)) {
            throw new IllegalStateException("user already exists");
        }

        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(USER);
        user.setStatus(BLOCKED);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15L),
                user
        );

        confirmationTokenRepository.save(confirmationToken);

        final String LINK = "http://localhost:8080/api/registration/confirmToken?token=" + token;

        emailSenderService.send(request.getEmail(), buildEmail(request.getName(), LINK));

        return new ResponseEntity<>("Activate your email", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<Object> confirmToken(String token) {

        ConfirmationToken confirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        boolean isNotConfirmed = confirmationToken.getConfirmedAt() == null;
        boolean notExpired = confirmationToken.getExpiredAt().isBefore(LocalDateTime.now());

        if (!isNotConfirmed) {
            throw new IllegalStateException("token is already confirmed");
        }

        if (notExpired) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenRepository.setConfirmedAt(token, LocalDateTime.now());
        userRepository.enableUser(confirmationToken.getUser().getEmail());

        return new ResponseEntity<>("Confirmed", HttpStatus.ACCEPTED);
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }


}
