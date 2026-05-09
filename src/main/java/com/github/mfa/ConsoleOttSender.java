package com.github.mfa;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConsoleOttSender implements OneTimeTokenGenerationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(ConsoleOttSender.class);

    private final OneTimeTokenGenerationSuccessHandler redirect =
            new RedirectOneTimeTokenGenerationSuccessHandler("/login/ott");

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken ott)
            throws IOException, ServletException {
        log.info("""
                \n==============================================
                  OTT gerado para: {}
                  Token:           {}
                  Expira em:       {}
                ==============================================
                """, ott.getUsername(), ott.getTokenValue(), ott.getExpiresAt());
        redirect.handle(request, response, ott);
    }
}