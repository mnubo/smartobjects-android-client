package com.mnubo.mnubo.platform.android.demo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class HttpRequestTask extends AsyncTask<Void, Void, Greeting> {

    private Activity activity;

    public HttpRequestTask(final Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Greeting doInBackground(Void... params) {
        try {
            final String url = "http://rest-service.guides.spring.io/greeting";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Greeting greeting = restTemplate.getForObject(url, Greeting.class);
            return greeting;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Greeting greeting) {
        TextView greetingIdText = (TextView) activity.findViewById(R.id.id_value);
        TextView greetingContentText = (TextView) activity.findViewById(R.id.content_value);
        greetingIdText.setText(greeting.getId());
        greetingContentText.setText(greeting.getContent());
    }

}