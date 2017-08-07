/*
 * Copyright (c) 2017 Mnubo. Released under MIT License.
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

package com.mnubo.android.api;

import android.util.Log;

import com.mnubo.android.internal.writer.FileStore;
import com.mnubo.android.models.Event;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;

@AllArgsConstructor
@Data
public class MnuboStore {
    private final static String TAG = MnuboStore.class.getName();
    private final static int DEFAULT_FILE_LIMIT = 200;

    private File rootDir;
    private int sizeLimit;
    private final FileStore fileStore;

    public MnuboStore(File rootDir) {
        this.rootDir = rootDir;
        this.sizeLimit = DEFAULT_FILE_LIMIT;
        fileStore = new FileStore();
    }

    public boolean writeEvents(@NonNull String deviceId, @NonNull List<Event> eventList) {
        return eventList.isEmpty() ||
                fileStore.write("events", rootDir, sizeLimit, new StoredEvents(deviceId, new ArrayList<>(eventList)));
    }

    public void readEvents(ReadEventsCallback callback) {
        File folder;
        try {
            folder = fileStore.prepareFolder("events", rootDir, sizeLimit);
        } catch (IOException e) {
            Log.e(TAG, String.format("Unable to find the folder: %s in %s", "events", rootDir.getAbsolutePath()), e);
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length <= 0) {
            return;
        }

        for (File file : files) {
            Object read = fileStore.read(file);
            if (read != null) {
                if (read instanceof StoredEvents) {
                    StoredEvents events = (StoredEvents) read;
                    callback.process(events.getDeviceId(), events.getEvents());
                } else {
                    callback.error(file);
                    Log.d(TAG, String.format("Expected %s but read %s from %s", StoredEvents.class.getName(), read.getClass().getName(), file.getAbsolutePath()));
                }
            } else {
                callback.error(file);
            }
        }
    }

    public interface ReadEventsCallback {
        void process(String deviceId, List<Event> readEvents);
        void error(File fileInError);
    }

    @Value
    static class StoredEvents implements Serializable {
        private final String deviceId;
        //ArrayList is serializable
        private final ArrayList<Event> events;
    }

}
