/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 */

package com.mnubo.platform.android.sdk.internal.connect;

import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboBadCredentialsException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboClientException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboCredentialsExpiredException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboDuplicateAttributeException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboExpiredAccessException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidConfirmPasswordException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidObjectException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidPreviousPasswordException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidRegistrationTokenException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidResetPasswordTokenException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidUUIDException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboObjectAlreadyExistsException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboObjectNotFoundException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboResetPasswordDisabledException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboSensorNotFoundException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboUnknownUserException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboUserAlreadyExistsException;
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

import static com.mnubo.platform.android.sdk.exceptions.client.MnuboBadCredentialsException.BAD_CREDENTIALS;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboCredentialsExpiredException.USER_CREDENTIALS_EXPIRED;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboDuplicateAttributeException.DUPLICATE_ATTRIBUTE;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboExpiredAccessException.EXPIRED_REFRESH_TOKEN;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidConfirmPasswordException.INVALID_CONFIRM_PASSWORD;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidPreviousPasswordException.INVALID_PREVIOUS_PASSWORD;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidRegistrationTokenException.REGISTRATION_INVALID_TOKEN;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidResetPasswordTokenException.PASSWORD_RESET_INVALID_TOKEN;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboObjectAlreadyExistsException.OBJECT_ALREADY_EXISTS;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboObjectNotFoundException.OBJECT_NOT_FOUND;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboResetPasswordDisabledException.RESET_PASSWORD_DISABLED;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboUserDisabledException.USER_DISABLED;

public class MnuboAPIErrorHandler extends DefaultResponseErrorHandler {


    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = response.getStatusCode();
        String status = response.getStatusText();
        byte[] body = getResponseBody(response);
        MediaType contentType = response.getHeaders().getContentType();
        Charset charset = contentType != null ? contentType.getCharSet() : null;


        Map<String, Object> errorMap = extractErrorDetailsFromResponse(body);
        if (errorMap != null) {

            String errorMessage = extractErrorMessageFromMap(errorMap);
            if (statusCode.series() == HttpStatus.Series.SERVER_ERROR) {

                if (errorMessage == null) {

                    throw new MnuboServerException(String.format("Unable to parse error message. [%s]", errorMap),
                            new HttpClientErrorException(statusCode, status, body, charset));
                }

                handleServerErrors(statusCode, errorMessage);

                throw new MnuboServerException(errorMessage, new HttpServerErrorException(statusCode, status, body, charset));

            } else if (statusCode.series() == HttpStatus.Series.CLIENT_ERROR) {

                if (errorMessage == null) {

                    throw new MnuboClientException(String.format("Unable to parse error message. [%s]", errorMap),
                            new HttpClientErrorException(statusCode, status, body, charset));
                }

                handleClientErrors(statusCode, errorMessage);

                throw new MnuboClientException(errorMessage, new HttpClientErrorException(statusCode, status, body, charset));
            }


        }
    }

    private void handleClientErrors(HttpStatus statusCode, String errorMessage) throws IOException {

        if (statusCode == HttpStatus.UNAUTHORIZED) {
            handleUnauthorize(errorMessage);
        } else if (statusCode == HttpStatus.BAD_REQUEST) {
            handleBadRequest(errorMessage);
        }

    }

    private void handleServerErrors(HttpStatus statusCode, String errorMessage) throws IOException {

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
        } else if (MnuboUnknownUserException.matches(errorMessage)) {
            throw new MnuboUnknownUserException();
        } else if (TextUtils.equals(REGISTRATION_INVALID_TOKEN, errorMessage)) {
            throw new MnuboInvalidRegistrationTokenException();
        } else if (TextUtils.equals(OBJECT_NOT_FOUND, errorMessage)) {
            throw new MnuboObjectNotFoundException();
        } else if (TextUtils.equals(INVALID_PREVIOUS_PASSWORD, errorMessage)) {
            throw new MnuboInvalidPreviousPasswordException();
        } else if (MnuboUserAlreadyExistsException.matches(errorMessage)) {
            throw new MnuboUserAlreadyExistsException();
        } else if (TextUtils.equals(PASSWORD_RESET_INVALID_TOKEN, errorMessage)) {
            throw new MnuboInvalidResetPasswordTokenException();
        } else if (TextUtils.equals(USER_CREDENTIALS_EXPIRED, errorMessage)) {
            throw new MnuboCredentialsExpiredException();
        } else if (TextUtils.equals(INVALID_CONFIRM_PASSWORD, errorMessage)) {
            throw new MnuboInvalidConfirmPasswordException();
        } else if (TextUtils.equals(DUPLICATE_ATTRIBUTE, errorMessage)) {
            throw new MnuboDuplicateAttributeException();
        } else if (TextUtils.equals(OBJECT_ALREADY_EXISTS, errorMessage)) {
            throw new MnuboObjectAlreadyExistsException();
        } else if (MnuboInvalidUUIDException.matches(errorMessage)) {
            throw new MnuboInvalidUUIDException();
        } else if (MnuboSensorNotFoundException.matches(errorMessage)) {
            throw new MnuboSensorNotFoundException();
        } else if (MnuboInvalidObjectException.matches(errorMessage)) {
            throw new MnuboInvalidObjectException();
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
