package com.mnubo.platform.android.sdk.internal.connect;

import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboBadCredentialsException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboExpiredAccessException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidRegistrationTokenException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboResetPasswordDisabledException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboUnknownUserException;
import com.mnubo.platform.android.sdk.exceptions.client.MnuboUserDisabledException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import static com.mnubo.platform.android.sdk.exceptions.client.MnuboBadCredentialsException.BAD_CREDENTIALS;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboExpiredAccessException.EXPIRED_REFRESH_TOKEN;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboInvalidRegistrationTokenException.REGISTRATION_INVALID_TOKEN;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboResetPasswordDisabledException.RESET_PASSWORD_DISABLED;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboUnknownUserException.UNKNOWN_USER;
import static com.mnubo.platform.android.sdk.exceptions.client.MnuboUserDisabledException.USER_DISABLED;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class MnuboAPIErrorHandlerTest {

    private final ResponseErrorHandler responseErrorHandler = new MnuboAPIErrorHandler();
    private final ObjectMapper mapper = new ObjectMapper();

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
        MnuboAPIResponse mnuboAPIResponse = new MnuboAPIResponse(BAD_CREDENTIALS, HttpStatus.BAD_REQUEST.value());
        MockClientHttpResponse mockClientHttpResponse = new MockClientHttpResponse(mapper.writeValueAsBytes(mnuboAPIResponse), HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(mockClientHttpResponse);
    }

    @Test(expected = MnuboExpiredAccessException.class)
    public void testExpiredAccessException() throws Exception {
        MnuboAPIResponse mnuboAPIResponse = new MnuboAPIResponse(EXPIRED_REFRESH_TOKEN, HttpStatus.BAD_REQUEST.value());
        MockClientHttpResponse mockClientHttpResponse = new MockClientHttpResponse(mapper.writeValueAsBytes(mnuboAPIResponse), HttpStatus.UNAUTHORIZED);

        responseErrorHandler.handleError(mockClientHttpResponse);
    }

    @Test(expected = MnuboInvalidRegistrationTokenException.class)
    public void testInvalidRegistrationTokenError() throws Exception {
        MnuboAPIResponse mnuboAPIResponse = new MnuboAPIResponse(REGISTRATION_INVALID_TOKEN, HttpStatus.BAD_REQUEST.value());
        MockClientHttpResponse mockClientHttpResponse = new MockClientHttpResponse(mapper.writeValueAsBytes(mnuboAPIResponse), HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(mockClientHttpResponse);
    }

    @Test(expected = MnuboResetPasswordDisabledException.class)
    public void testResetPasswordDisabledError() throws Exception {
        MnuboAPIResponse mnuboAPIResponse = new MnuboAPIResponse(RESET_PASSWORD_DISABLED, HttpStatus.BAD_REQUEST.value());
        MockClientHttpResponse mockClientHttpResponse = new MockClientHttpResponse(mapper.writeValueAsBytes(mnuboAPIResponse), HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(mockClientHttpResponse);
    }

    @Test(expected = MnuboUnknownUserException.class)
    public void testUnknownUserError() throws Exception {
        MnuboAPIResponse mnuboAPIResponse = new MnuboAPIResponse(UNKNOWN_USER, HttpStatus.BAD_REQUEST.value());
        MockClientHttpResponse mockClientHttpResponse = new MockClientHttpResponse(mapper.writeValueAsBytes(mnuboAPIResponse), HttpStatus.BAD_REQUEST);

        responseErrorHandler.handleError(mockClientHttpResponse);
    }

    @Test(expected = MnuboUserDisabledException.class)
    public void testUserDisabled() throws Exception {
        MnuboAPIResponse mnuboAPIResponse = new MnuboAPIResponse(USER_DISABLED, HttpStatus.BAD_REQUEST.value());
        MockClientHttpResponse mockClientHttpResponse = new MockClientHttpResponse(mapper.writeValueAsBytes(mnuboAPIResponse), HttpStatus.UNAUTHORIZED);

        responseErrorHandler.handleError(mockClientHttpResponse);
    }

    /**
     * {
     * "timestamp": "2015-03-12T19:40:49.344Z",
     * "errorCode": 400,
     * "message": "Unknown User 'dfrancoeur@mnubo.com44'",
     * "path": "/users/dfrancoeur@mnubo.com44",
     * "requestId": "38c75428-cb78-4305-a20e-4259ad3d653b"
     * }
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