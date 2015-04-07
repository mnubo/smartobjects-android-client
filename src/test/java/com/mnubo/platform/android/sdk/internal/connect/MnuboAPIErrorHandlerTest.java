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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpResponse;

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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class MnuboAPIErrorHandlerTest {

    private final MnuboAPIErrorHandler responseErrorHandler = new MnuboAPIErrorHandler();
    private final ObjectMapper mapper = new ObjectMapper();

    private final String INVALID_OBJECT_MODEL = "Validation failed for argument at index 1 in method: public org.springframework.http.ResponseEntity<java.lang.Object> com.mnubo.platform.server.rest.ugoc.objects.ObjectsResource.addObject(org.springframework.web.util.UriComponentsBuilder,com.mnubo.common.models.sdk.ObjectBean,boolean), with 1 error(s): [Field error in object 'objectBean' on field 'objectModelName': rejected value [null]; codes [NotBlank.objectBean.objectModelName,NotBlank.objectModelName,NotBlank.java.lang.String,NotBlank]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [objectBean.objectModelName,objectModelName]; arguments []; default message [objectModelName]]; default message [Missing object model]]";

    @Before
    public void setUp() throws Exception {
        mockStatic(TextUtils.class);

        when(TextUtils.equals(any(CharSequence.class), any(CharSequence.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CharSequence a = (CharSequence) invocation.getArguments()[0];
                CharSequence b = (CharSequence) invocation.getArguments()[1];
                if (a == b) return true;
                int length;
                if (a != null && b != null && (length = a.length()) == b.length()) {
                    if (a instanceof String && b instanceof String) {
                        return a.equals(b);
                    } else {
                        for (int i = 0; i < length; i++) {
                            if (a.charAt(i) != b.charAt(i)) return false;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Test(expected = MnuboBadCredentialsException.class)
    public void testBadCredentialsError() throws Exception {
        ClientHttpResponse response = prepareResponse(BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboExpiredAccessException.class)
    public void testExpiredAccessException() throws Exception {
        ClientHttpResponse response = prepareResponse(EXPIRED_REFRESH_TOKEN, HttpStatus.UNAUTHORIZED);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboInvalidRegistrationTokenException.class)
    public void testInvalidRegistrationTokenError() throws Exception {
        ClientHttpResponse response = prepareResponse(REGISTRATION_INVALID_TOKEN, HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboResetPasswordDisabledException.class)
    public void testResetPasswordDisabledError() throws Exception {
        ClientHttpResponse response = prepareResponse(RESET_PASSWORD_DISABLED, HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboUnknownUserException.class)
    public void testUnknownUserErrorEmail() throws Exception {
        ClientHttpResponse response = prepareResponse("Unknown User 'test@email.com'", HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboUnknownUserException.class)
    public void testUnknownUserErrorUsername() throws Exception {
        ClientHttpResponse response = prepareResponse("Unknown User 'test'", HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboUserDisabledException.class)
    public void testUserDisabled() throws Exception {
        ClientHttpResponse response = prepareResponse(USER_DISABLED, HttpStatus.UNAUTHORIZED);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboInvalidPreviousPasswordException.class)
    public void testInvalidPreviousPassword() throws Exception {
        ClientHttpResponse response = prepareResponse(INVALID_PREVIOUS_PASSWORD, HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboObjectNotFoundException.class)
    public void testObjectNotFound() throws Exception {
        ClientHttpResponse response = prepareResponse(OBJECT_NOT_FOUND, HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboUserAlreadyExistsException.class)
    public void testUserAlreadyExist() throws Exception {
        ClientHttpResponse response = prepareResponse("User 'test' already exists.", HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboUserAlreadyExistsException.class)
    public void testUserAlreadyExistEmail() throws Exception {
        ClientHttpResponse response = prepareResponse("User 'test@mnubo.com' already exists.", HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboClientException.class)
    public void testNullMessage() throws Exception {
        ClientHttpResponse response = prepareResponse(null, HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboInvalidResetPasswordTokenException.class)
    public void testInvalidPasswordResetToken() throws Exception {
        ClientHttpResponse response = prepareResponse(PASSWORD_RESET_INVALID_TOKEN, HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboCredentialsExpiredException.class)
    public void testCredentialsExpiredException() throws Exception {
        ClientHttpResponse response = prepareResponse(USER_CREDENTIALS_EXPIRED, HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboInvalidConfirmPasswordException.class)
    public void testInvalidConfirmPasswordException() throws Exception {
        ClientHttpResponse response = prepareResponse(INVALID_CONFIRM_PASSWORD, HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboDuplicateAttributeException.class)
    public void testDuplicateAttributeException() throws Exception {
        ClientHttpResponse response = prepareResponse(DUPLICATE_ATTRIBUTE, HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboObjectAlreadyExistsException.class)
    public void testObjectAlreadyExistsException() throws Exception {
        ClientHttpResponse response = prepareResponse(OBJECT_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboInvalidUUIDException.class)
    public void testInvalidUUIDException() throws Exception {
        ClientHttpResponse response = prepareResponse("Invalid UUID string: invalid", HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboSensorNotFoundException.class)
    public void testSensorNotFoundException() throws Exception {
        ClientHttpResponse response = prepareResponse("sensor name(invalid) not found", HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboInvalidObjectException.class)
    public void testInvalidObjectMissingDeviceIdException() throws Exception {
        ClientHttpResponse response = prepareResponse("Invalid id", HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboInvalidObjectException.class)
    public void testInvalidObjectMissingObjectModelException() throws Exception {
        ClientHttpResponse response = prepareResponse(INVALID_OBJECT_MODEL, HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test(expected = MnuboInvalidObjectException.class)
    public void testInvalidObjectUnknownAttributeException() throws Exception {
        ClientHttpResponse response = prepareResponse("Attribute unknown_attribute undefined in object model", HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(response);
    }

    @Test
    public void testUnknownMessage() throws Exception {
        final String errorMessage = "Unknown error message";
        ClientHttpResponse response = prepareResponse(errorMessage, HttpStatus.BAD_REQUEST);

        boolean catched = false;
        String message = null;
        try {
            responseErrorHandler.handleError(response);
        } catch (MnuboClientException ex) {
            catched = true;
            message = ex.getMessage();
        }
        assertThat(catched, is(equalTo(true)));
        assertThat(message, is(equalTo(errorMessage)));
    }

    private ClientHttpResponse prepareResponse(String errorMessage, HttpStatus code) throws Exception {
        MnuboAPIResponse mnuboAPIResponse = new MnuboAPIResponse(errorMessage, code.value());
        return new MockClientHttpResponse(mapper.writeValueAsBytes(mnuboAPIResponse), code);
    }

    /**
     * { "timestamp": "2015-03-12T19:40:49.344Z", "errorCode": 400, "message": "Unknown User
     * 'dfrancoeur@mnubo.com44'", "path": "/users/dfrancoeur@mnubo.com44", "requestId":
     * "38c75428-cb78-4305-a20e-4259ad3d653b" }
     */
    private class MnuboAPIResponse {
        String timestamp;
        Integer errorCode;
        String message;
        String path;
        String requestId;

        private MnuboAPIResponse(String message, Integer errorCode) {
            this.message = message;
            this.errorCode = errorCode;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public Integer getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(Integer errorCode) {
            this.errorCode = errorCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }
    }
}