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

package com.mnubo.platform.android.sdk.api.store.impl;

import android.text.TextUtils;
import android.util.Log;

import com.mnubo.platform.android.sdk.Strings;
import com.mnubo.platform.android.sdk.api.store.MnuboDataStore;
import com.mnubo.platform.android.sdk.api.store.MnuboEntity;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static com.mnubo.platform.android.sdk.api.store.impl.MnuboFileEntity.EXTENSION;

/**
 * Implements a MnuboStore that saves data in folders (queue) on the disk
 * <p/>
 * The store has a size limit of 100 entities by queue (folder). You can change it see the
 * #setQueueMaximumSize.
 */
public class MnuboFileDataStore implements MnuboDataStore {

    private final static String TAG = MnuboFileDataStore.class.getName();

    private int maximumSize = 100;

    private final File rootDir;

    /**
     * The store will create it's folder into this directory. The recommended root directory is the
     * application cache directory that can be found in {link android.context.Context}
     *
     * @param rootDir root directory where the store will operate
     */
    public MnuboFileDataStore(File rootDir) {
        this.rootDir = rootDir;
    }

    /**
     * {@inheritDoc} <p/> The entity will be written in <code>rootDir/queueName/{id}-timestamp</code>
     */
    @Override
    public boolean put(String queueName, Object entity) {
        return write(queueName, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object entity) {
        boolean success = false;
        if (entity instanceof MnuboFileEntity) {
            MnuboFileEntity fileEntity = (MnuboFileEntity) entity;
            File file = new File(fileEntity.getFile());
            success = file.delete();

            if (!success) {
                Log.e(TAG, String.format("Deleting : %s has failed", fileEntity.getFile()));
            }
        } else {
            Log.e(TAG, "MnuboFileStore can only remove MnuboFileEntity");
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MnuboEntity> getEntities(String queueName) {
        try {
            return readQueue(queueName);
        } catch (IOException e) {
            Log.e(TAG, String.format("An error has occurred while reading the queue : %s", queueName), e);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setQueueMaximumSize(int size) {
        this.maximumSize = size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getQueueMaximumSize() {
        return maximumSize;
    }

    private boolean write(String queueName, Object entity) {

        OutputStream fos;
        File file;
        try {
            final File queueFolder = prepareQueueFolder(getQueueFolder(queueName));
            file = getFilename(queueFolder, entity.hashCode());
            fos = new FileOutputStream(file);

        } catch (IOException ie) {
            Log.e(TAG, String.format("An error has occurred while opening/creating the file in queue : %s", queueName), ie);
            return false;
        }

        ObjectOutputStream os;
        try {
            MnuboFileEntity fileEntity = new MnuboFileEntity(entity, file.getAbsolutePath());
            os = new ObjectOutputStream(fos);
            os.writeObject(fileEntity);
            os.close();
            Log.d(TAG, String.format("Wrote to the file : %s", file.getAbsolutePath()));
            return true;
        } catch (IOException e) {
            Log.e(TAG, String.format("An error has occurred while writing the object [%s] to the file : %s", entity, file.getAbsolutePath()), e);
            return false;
        }
    }

    private List<MnuboEntity> readQueue(String queueFolder) throws IOException {
        final File queueDirectory = getQueueFolder(queueFolder);

        File[] files = queueDirectory.listFiles();
        List<MnuboEntity> queue = new LinkedList<>();
        for (File file : files) {
            final MnuboFileEntity entity = readFile(file);
            if (entity != null) {
                queue.add(entity);
            }
        }
        return queue;


    }

    private MnuboFileEntity readFile(File file) {
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
            MnuboFileEntity object = (MnuboFileEntity) is.readObject();

            is.close();
            fis.close();

            return object;
        } catch (Exception e) {
            Log.e(TAG, String.format("An error has occured while reading the file : %s", file.getAbsolutePath()), e);
            return null;
        }
    }

    private File getFilename(File queueFolder, int hashCode) throws IOException {
        long timestamp = Calendar.getInstance().getTimeInMillis();
        File file = new File(queueFolder, String.format("%s-%s.%s", timestamp, hashCode, EXTENSION));
        boolean exists = file.exists();
        if (!exists) {
            exists = file.createNewFile();
        }

        if (!exists) {
            throw new IOException(String.format("File cannot be created and didn't exists : %s", file.getAbsolutePath()));
        }
        return file;
    }

    private File getQueueFolder(String queueName) throws IOException {
        if (TextUtils.isEmpty(queueName)) {
            throw new IllegalArgumentException("The queue name should not be null or empty");
        }

        File file = new File(this.rootDir, queueName);
        if (!file.exists()) {

            if (!file.mkdirs()) {
                throw new IOException("Could not create queue folder '" +
                        file.getAbsolutePath() + "'");
            }

        }
        return file;
    }

    private File prepareQueueFolder(File file) {
        if (getQueueMaximumSize() == 0) {
            return file;
        }
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File fileToFilter) {
                return fileToFilter.isFile();
            }
        });
        if (files != null && files.length >= getQueueMaximumSize()) {
            int filesToRemove = files.length - getQueueMaximumSize() + 1;
            removeOldestFileFromFolder(files, filesToRemove);
        }
        return file;
    }

    /**
     * Files name are based on the timestamp. It means that the oldest files will be first in a
     * regular string sort.
     *
     * @param files list of files
     * @param filesToRemove qty of files to remove to make room
     */
    private void removeOldestFileFromFolder(final File[] files, int filesToRemove) {
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
                Log.e(TAG, String.format(Strings.SDK_DATA_STORE_UNABLE_TO_REMOVE_OLD_DATA, fileToRemove.getAbsolutePath()));
            } else {
                Log.d(TAG, String.format(Strings.SDK_DATA_STORE_REMOVED_OLD_DATA, fileToRemove.getAbsolutePath()));
            }
        }
    }
}
