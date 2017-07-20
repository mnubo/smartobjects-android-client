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

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mnubo.android.Mnubo;
import com.mnubo.android.api.CompletionCallback;
import com.mnubo.android.exceptions.MnuboException;
import com.mnubo.android.models.Owner;

public class OwnerActivity extends AbstractLoggedInActivity {


    private EditText mAttribute1;
    private Button mUpdateOwnerButton;
    private View mProgressView;
    private View mUpdateOwnerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mAttribute1 = (EditText) findViewById(R.id.attribute1);
        mUpdateOwnerButton = (Button) findViewById(R.id.update_owner_button);

        mUpdateOwnerView = findViewById(R.id.update_owner_form);
        mProgressView = findViewById(R.id.update_owner_progress);

        mUpdateOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOwner();
            }
        });
    }

    private void updateOwner() {
        String attribute1 = mAttribute1.getText().toString();

        Owner owner =
                Owner.builder()
                        .attribute("attribute1", attribute1)
                        .build();

        showProgress(true, mProgressView, mUpdateOwnerView);
        Mnubo.getApi().getOwnerOperations().updateAsync(owner,
                new CompletionCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        showProgress(false, mProgressView, mUpdateOwnerView);
                        Toast.makeText(getApplicationContext(), "It works, marvelous", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(MnuboException exception) {
                        showProgress(false, mProgressView, mUpdateOwnerView);
                        Toast.makeText(getApplicationContext(), "It didn't work, too sad.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
