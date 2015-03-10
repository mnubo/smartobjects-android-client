package com.mnubo.platform.android.sdk.internal.connect;

import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnubo.platform.android.sdk.exceptions.MnuboException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboAccessDeniedException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboBadCredentialsException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboClientException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboExpiredAccessException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidRegistrationTokenException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboResetPasswordDisabledException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboUnknownUserException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboUserDisabledException;
import com.mnubo.platform.android.sdk.exceptions.server.MnuboServerException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

public class MnuboAPIErrorHandler extends DefaultResponseErrorHandler {

    private static String BAD_CREDENTIALS = "Bad credentials";
    private static String EXPIRED_REFRESH_TOKEN = "Invalid refresh token (expired):";
    private static String RESET_PASSWORD_DISABLED = "Reset Password Disabled";
    private static String UNKNOWN_USER = "Unknown User";
    private static String USER_DISABLED = "User is disabled";
    private static String REGISTRATION_INVALID_TOKEN = "Two Steps User Registration: Invalid Token received for the User.";

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = response.getStatusCode();

        if (statusCode.series() == HttpStatus.Series.SERVER_ERROR) {
            handleServerErrors(response);
        } else if (statusCode.series() == HttpStatus.Series.CLIENT_ERROR) {
            handleClientErrors(response);
        }

        throw new MnuboException();
    }

    private void handleClientErrors(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = response.getStatusCode();
        String status = response.getStatusText();
        byte[] body = getResponseBody(response);
        MediaType contentType = response.getHeaders().getContentType();
        Charset charset = contentType != null ? contentType.getCharSet() : null;


        Map<String, Object> errorMap = extractErrorDetailsFromResponse(body);
        if (errorMap != null) {

            String errorMessage = extractErrorMessageFromMap(errorMap);
            if (errorMessage == null) {
                throw new MnuboException(String.format("Unable to parse error message. [%s]", errorMap));
            }

            if (statusCode == HttpStatus.UNAUTHORIZED) {
                handleUnauthorize(errorMessage);
            } else if (statusCode == HttpStatus.BAD_REQUEST) {
                handleBadRequest(errorMessage);
            } else if (statusCode == HttpStatus.FORBIDDEN) {
                throw new MnuboAccessDeniedException(new HttpClientErrorException(statusCode, status, body, charset));
            }
        }

        throw new MnuboClientException(new HttpClientErrorException(statusCode, status, body, charset));

    }

    private void handleServerErrors(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = response.getStatusCode();
        String status = response.getStatusText();
        byte[] body = getResponseBody(response);
        MediaType contentType = response.getHeaders().getContentType();
        Charset charset = contentType != null ? contentType.getCharSet() : null;

        throw new MnuboServerException(new HttpServerErrorException(statusCode, status, body, charset));
    }

    private void handleUnauthorize(final String errorMessage) {

        if (errorMessage.startsWith(EXPIRED_REFRESH_TOKEN)) {
            throw new MnuboExpiredAccessException();
        } else if (TextUtils.equals(errorMessage, USER_DISABLED)) {
            throw new MnuboUserDisabledException();
        }
    }


    private void handleBadRequest(final String errorMessage) {
        if (TextUtils.equals(BAD_CREDENTIALS, errorMessage)) {
            throw new MnuboBadCredentialsException();
        } else if (TextUtils.equals(RESET_PASSWORD_DISABLED, errorMessage)) {
            throw new MnuboResetPasswordDisabledException();
        } else if (errorMessage.startsWith(UNKNOWN_USER)) {
            throw new MnuboUnknownUserException();
        } else if (TextUtils.equals(REGISTRATION_INVALID_TOKEN, errorMessage)) {
            throw new MnuboInvalidRegistrationTokenException();
        }
    }

    // From TwitterErrorHandler
    private Map<String, Object> extractErrorDetailsFromResponse(byte[] body) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        try {
            return mapper.readValue(body, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonParseException e) {
            return null;
        }
    }

    private String extractErrorMessageFromMap(Map<String, Object> errorMap) {
        String errorText = null;
        if (errorMap.containsKey("message")) {
            errorText = (String) errorMap.get("message");
        } else if (errorMap.containsKey("error_description")) {
            errorText = (String) errorMap.get("error_description");
        }
        return errorText;
    }

    private byte[] getResponseBody(ClientHttpResponse response) {
        try {
            InputStream responseBody = response.getBody();
            if (responseBody != null) {
                return FileCopyUtils.copyToByteArray(responseBody);
            }
        } catch (IOException ex) {
            // ignore
        }
        return new byte[0];
    }

}
