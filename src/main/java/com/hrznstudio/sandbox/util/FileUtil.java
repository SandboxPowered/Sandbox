package com.hrznstudio.sandbox.util;

import com.hrznstudio.sandbox.api.IDownloadIndicator;
import org.apache.commons.io.output.CountingOutputStream;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class FileUtil {
    public static Collection<File> getFiles(File folder, FilenameFilter fileFilter, boolean recursive) {
        File[] files = folder.listFiles();
        if (files == null)
            return Collections.emptySet();
        Set<File> allFiles = new HashSet<>();
        for (File f : files) {
            if (recursive && f.isDirectory()) {
                allFiles.addAll(getFiles(f, fileFilter, true));
                continue;
            }
            if (fileFilter.accept(folder, f.getName())) {
                allFiles.add(f);
            }
        }
        return allFiles;
    }

    public static File[] listFiles(File dir) {
        File[] files = dir.listFiles();
        if (files != null)
            return files;
        return new File[0];
    }

    public static IDownloadIndicator downloadFile(URL url, Path out) {
        DownloadTracker tracker = new DownloadTracker();
        new Thread(() -> {
            tracker.init(getFileSize(url));
            try (InputStream input = url.openStream()) {
                Path downloads = Paths.get("downloads");
                Files.createDirectories(downloads);

                Path temp = downloads.resolve(UUID.randomUUID().toString() + ".sbxdl");
                try (CountingOutputStream output = new CountingOutputStream(Files.newOutputStream(temp))) {
                    int b;
                    while ((b = input.read()) != -1) {
                        output.write(b);
                        tracker.set(output.getByteCount());
                    }
                }
                if (Files.exists(out))
                    Files.delete(out);
                Files.move(temp, out);
                tracker.complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        return tracker;
    }

    static class DownloadTracker implements IDownloadIndicator {
        AtomicLong totalSize = new AtomicLong();
        AtomicLong currentSize = new AtomicLong();
        AtomicBoolean complete = new AtomicBoolean();
        AtomicBoolean hasStarted = new AtomicBoolean();

        void init(long totalSize) {
            this.totalSize.set(totalSize);
            this.hasStarted.set(true);
        }

        public void set(long byteCount) {
            this.currentSize.set(byteCount);
        }

        void complete() {
            this.complete.set(true);
        }

        @Override
        public long getCurrentSize() {
            return currentSize.get();
        }

        @Override
        public long getTotalSize() {
            return totalSize.get();
        }

        @Override
        public boolean isComplete() {
            return complete.get();
        }

        @Override
        public boolean hasStarted() {
            return hasStarted.get();
        }
    }

    public static long getFileSize(URL url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            return conn.getContentLengthLong();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}