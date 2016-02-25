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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mnubo.android.Mnubo;
import com.mnubo.android.api.CompletionCallback;
import com.mnubo.android.api.MnuboStore;
import com.mnubo.android.exceptions.MnuboException;
import com.mnubo.android.models.Event;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by davidfrancoeur on 2016-02-23.
 */
public abstract class AbstractLoggedInActivity extends AppCompatActivity {

    List<String> windsDirection = Arrays.asList("NORTH", "EAST", "WEST", "SOUTH");

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_menu_objects:
                startActivity(new Intent(getApplicationContext(), ObjectActivity.class));
                return true;
            case R.id.action_menu_owner:
                startActivity(new Intent(getApplicationContext(), OwnerActivity.class));
                return true;
            case R.id.action_menu_windturbine:
                startActivity(new Intent(getApplicationContext(), WindTurbineActivity.class));
                return true;
            case R.id.action_menu_tennis:
                startActivity(new Intent(getApplicationContext(), TennisActivity.class));
                return true;
            case R.id.action_menu_logout:
                Mnubo.logOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            case R.id.action_menu_resend_events:
                resendEvents();
                return true;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress(final boolean show, final View progressView, final View formView) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            formView.setVisibility(show ? View.GONE : View.VISIBLE);
            formView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    formView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            formView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    protected List<Event> generateEvents(String eventType) {
        Random randomGenerator = new Random();
        int random = randomGenerator.nextInt(25);

        if (random == 0) {
            random = 5;
        }

        Log.d("EVENT_GENERATOR", String.format("Generating %s events for %s.", random, eventType));

        List<Event> events = new ArrayList<>();
        for (int i = 0; i < random; i++) {
            Event.EventBuilder eventBuilder = Event.builder().eventType(eventType);
            if (eventType.equals("windturbine")) {
                events.add(
                        eventBuilder
                                .timeserie("rotation_per_minute", randomGenerator.nextInt())
                                .timeserie("instant_output_power", randomGenerator.nextFloat())
                                .timeserie("wind_speed", randomGenerator.nextFloat())
                                .timeserie("power_coefficient", randomGenerator.nextFloat())
                                .timeserie("wind_direction", windsDirection.get(randomGenerator.nextInt(3)))
                                .build()
                );
            } else {
                events.add(
                        eventBuilder
                                .timeserie("speed", randomGenerator.nextInt())
                                .timeserie("velocity", randomGenerator.nextFloat())
                                .timeserie("pitch_control", randomGenerator.nextBoolean())
                                .timeserie("angle", randomGenerator.nextFloat())
                                .build()
                );
            }
        }
        return events;
    }

    private void resendEvents() {
        Mnubo.getStore().readEvents(new MnuboStore.ReadEventsCallback() {
            @Override
            public void process(String deviceId, List<Event> readEvents) {
                Mnubo.getApi().getEventOperations().sendEventsAsync(deviceId, readEvents, new CompletionCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(getApplicationContext(), "Successfully, ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(MnuboException exception) {
                        Toast.makeText(getApplicationContext(), "It didn't work, too sad.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void error(File fileInError) {
                //could not read the file or type is not what expected
            }
        });
    }
}
