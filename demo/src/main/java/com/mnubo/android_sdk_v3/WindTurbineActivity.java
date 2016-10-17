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
import com.mnubo.android.exceptions.MnuboNetworkException;
import com.mnubo.android.models.Event;
import com.mnubo.android.models.SmartObject;

import java.util.Collections;
import java.util.List;

public class WindTurbineActivity extends AbstractLoggedInActivity {

    private EditText mModel;
    private EditText mMake;
    private EditText mHigh;
    private EditText mRotorDiameter;
    private EditText mSweptArea;
    private EditText mMaxOutput;

    private Button mUpdateWindTurbineButton;
    private Button mSendEventsButton;

    private View mProgressView;
    private View mUpdateWindTurbineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wind_turbine);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mUpdateWindTurbineView = findViewById(R.id.update_windturbine_form);
        mProgressView = findViewById(R.id.update_windturbine_progress);

        mUpdateWindTurbineButton = (Button) findViewById(R.id.update_windturbine_button);
        mSendEventsButton = (Button) findViewById(R.id.send_events_windturbine_button);

        mModel = (EditText) findViewById(R.id.model);
        mMake = (EditText) findViewById(R.id.make);
        mHigh = (EditText) findViewById(R.id.high);
        mRotorDiameter = (EditText) findViewById(R.id.rotor_diameter);
        mSweptArea = (EditText) findViewById(R.id.swept_area);
        mMaxOutput = (EditText) findViewById(R.id.max_output);

        mUpdateWindTurbineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWindTurbine();
            }
        });

        mSendEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEvents();
            }
        });


    }

    private void sendEvents() {
        String username = "PgMKqC";
        final String deviceId = username + "-windturbine";

        showProgress(true, mProgressView, mUpdateWindTurbineView);
        final List<Event> events = generateEvents("windturbine");
        Mnubo.getApi().getEventOperations().sendEventsAsync(deviceId, events, new CompletionCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                showProgress(false, mProgressView, mUpdateWindTurbineView);
                Toast.makeText(getApplicationContext(), "It works, marvelous", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(MnuboException exception) {
                showProgress(false, mProgressView, mUpdateWindTurbineView);
                Toast.makeText(getApplicationContext(), "It didn't work, too sad.", Toast.LENGTH_SHORT).show();
                if (exception instanceof MnuboNetworkException) {
                    Mnubo.getStore().writeEvents(deviceId, events);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void updateWindTurbine() {
        String model = mModel.getText().toString();
        String make = mMake.getText().toString();
        String high = mHigh.getText().toString();

        Float rotorDiameter = Float.parseFloat(mRotorDiameter.getText().toString());
        Float sweptArea = Float.parseFloat(mSweptArea.getText().toString());
        Float maxOutput = Float.parseFloat(mMaxOutput.getText().toString());

        String username = "PgMKqC";
        String deviceId = username + "-windturbine";
        SmartObject object =
                SmartObject.builder()
                        .attribute("model", model)
                        .attribute("make", make)
                        .attribute("rotor_diameter", rotorDiameter)
                        .attribute("swept_area", sweptArea)
                        .attribute("max_power_output", maxOutput)
                        .attribute("high", high)
                        .build();

        showProgress(true, mProgressView, mUpdateWindTurbineView);
        Mnubo.getApi().getSmartObjectOperations().updateAsync(deviceId, object,
                new CompletionCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        showProgress(false, mProgressView, mUpdateWindTurbineView);
                        Toast.makeText(getApplicationContext(), "It works, marvelous", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(MnuboException exception) {
                        showProgress(false, mProgressView, mUpdateWindTurbineView);
                        Toast.makeText(getApplicationContext(), "It didn't work, too sad.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
