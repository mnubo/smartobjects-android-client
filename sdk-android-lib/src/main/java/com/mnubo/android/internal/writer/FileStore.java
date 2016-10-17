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

package com.mnubo.android.internal.writer;


import android.util.Log;

import com.mnubo.android.utils.ValidationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileStore {
    private final static String TAG = FileStore.class.getName();

    public boolean write(String folderName, File rootDir, int sizeLimit, Serializable object) {
        File file;
        try {
            file = generateFileName(folderName, rootDir, sizeLimit);
        } catch (IOException e) {
            return false;
        }

        ObjectOutputStream os;
        try {
            os = new ObjectOutputStream(new FileOutputStream(file));
            os.writeObject(object);
            os.close();
            Log.d(TAG, String.format("Wrote to the file : %s", file.getAbsolutePath()));
            return true;
        } catch (IOException e) {
            Log.e(TAG, String.format("An error has occurred while writing the object [%s] to the file : %s", object, file.getAbsolutePath()), e);
            return false;
        }
    }

    public Object read(File file) {
        InputStream fis;
        try {
            fis = new FileInputStream(file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            Log.e(TAG, String.format("The file was not found : %s", file.getAbsolutePath()), e);
            return null;
        }
        ObjectInputStream is;
        try {
            is = new ObjectInputStream(fis);
            @SuppressWarnings("unchecked")
            Object values = is.readObject();

            is.close();
            fis.close();
            return values;

        } catch (Exception e) {
            Log.e(TAG, String.format("An error has occurred while reading the file : %s", file.getAbsolutePath()), e);
            return null;
        }
    }

    public File generateFileName(String folderName, File rootDir, int sizeLimit) throws IOException {
        return new File(prepareFolder(folderName, rootDir, sizeLimit), String.valueOf(System.currentTimeMillis() + ".mnubo"));
    }

    public File prepareFolder(String folderName, File rootDir, int sizeLimit) throws IOException {
        ValidationUtils.notNullOrEmpty(folderName, "The queue name should not be null or empty");

        File file = new File(rootDir, folderName);
        if (!file.exists()) {

            if (!file.mkdirs()) {
                throw new IOException(String.format("Could not create queue folder: %s", file.getAbsolutePath()));
            }

        }

        File[] files = file.listFiles();
        if (files != null && files.length >= sizeLimit) {
            removeOldestFileFromFolder(files, 1, sizeLimit);
        }

        return file;
    }

    /**
     * Files name are based on the timestamp. It means that the oldest files will be first in a
     * regular string sort.
     *
     * @param files         list of files
     * @param filesToRemove qty of files to remove to make room
     */
    private void removeOldestFileFromFolder(final File[] files, int filesToRemove, int sizeLimit) {
        List<File> listFiles = Arrays.asList(files);
        Collections.sort(listFiles, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });

        for (int i = 0; i < filesToRemove; i++) {
            final File fileToRemove = listFiles.get(i);
            if (!fileToRemove.delete()) {
                Log.e(TAG, String.format("Unable to remove the file: %s", fileToRemove.getAbsolutePath()));
            } else {
                Log.d(TAG, String.format("File : %s was removed because limit: %s was reached", fileToRemove.getAbsolutePath(), sizeLimit));
            }
        }
    }

}
