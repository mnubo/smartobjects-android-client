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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mnubo.android.Mnubo;
import com.mnubo.android.api.CompletionCallback;
import com.mnubo.android.exceptions.MnuboException;
import com.mnubo.android.models.Event;
import com.mnubo.android.models.SmartObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.mnubo.android.Mnubo.getApi;

public class TennisActivity extends AbstractLoggedInActivity {

    private EditText mColor;
    private EditText mMake;
    private EditText mCountry;
    private EditText mYear;

    private Button mUpdateTennisButton;
    private Button mSendEvents;

    private View mProgressView;
    private View mUpdateTennisView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tennis);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mUpdateTennisView = findViewById(R.id.update_tennis_form);
        mProgressView = findViewById(R.id.update_tennis_progress);

        mUpdateTennisButton = (Button) findViewById(R.id.update_tennis_button);
        mSendEvents = (Button) findViewById(R.id.send_events_tennis_button);


        mColor = (EditText) findViewById(R.id.color);
        mMake = (EditText) findViewById(R.id.make);
        mCountry = (EditText) findViewById(R.id.country);
        mYear = (EditText) findViewById(R.id.year);

        mUpdateTennisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTennis();
            }
        });

        mSendEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEvents();
            }
        });

    }

    private void sendEvents() {
        String username = "PgMKqC";
        String deviceId = username + "-tennis";

        showProgress(true, mProgressView, mUpdateTennisView);
        getApi().getEventOperations().sendEventsAsync(deviceId, generateEvents("tennis"), new CompletionCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                showProgress(false, mProgressView, mUpdateTennisView);
                Toast.makeText(getApplicationContext(), "It works, marvelous", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(MnuboException exception) {
                showProgress(false, mProgressView, mUpdateTennisView);
                Toast.makeText(getApplicationContext(), "It didn't work, too sad.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTennis() {
        String color = mColor.getText().toString();
        String make = mMake.getText().toString();
        String country = mCountry.getText().toString();
        String year = mYear.getText().toString();

        String username = "PgMKqC";
        String deviceId = username + "-tennis";
        SmartObject object =
                SmartObject.builder()
                        .attribute("color", color)
                        .attribute("make", make)
                        .attribute("year", year)
                        .attribute("country", country)
                        .build();

        showProgress(true, mProgressView, mUpdateTennisView);
        getApi().getSmartObjectOperations().updateAsync(deviceId, object,
                new CompletionCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        showProgress(false, mProgressView, mUpdateTennisView);
                        Toast.makeText(getApplicationContext(), "It works, marvelous", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(MnuboException exception) {
                        showProgress(false, mProgressView, mUpdateTennisView);
                        Toast.makeText(getApplicationContext(), "It didn't work, too sad.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
