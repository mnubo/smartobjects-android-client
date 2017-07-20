/*
 * Copyright (c) 2016 Mnubo. Released under MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.mnubo.android_sdk_v3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mnubo.android.Mnubo;
import com.mnubo.android.api.AuthenticationProblemCallback;
import com.mnubo.android.config.MnuboSDKConfig;

import java.io.File;
import java.util.Properties;

/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends Activity {

    private final static Properties props = new Properties();

    static {
        props.put(MnuboSDKConfig.CONFIG_KEY, "TTtFCVUySo1cpGMm1WhiyugtbF8pVb8BhHyIy6UaOct1O2vpBu");
        props.put(MnuboSDKConfig.CONFIG_URL, "https://rest.sandbox.mnubo.com");
    }

    private final static MnuboSDKConfig config = MnuboSDKConfig.withProperties(props);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Mnubo.init(this, config, new AuthenticationProblemCallback() {
            @Override
            public void onError() {
                startActivity(new Intent(getApplicationContext(), ChooseLoginActivity.class));
            }
        });

        startActivity(new Intent(getApplicationContext(), ChooseLoginActivity.class));

    }

}

