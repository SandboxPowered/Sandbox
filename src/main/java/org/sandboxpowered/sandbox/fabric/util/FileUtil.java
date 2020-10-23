package org.sandboxpowered.sandbox.fabric.util;

import org.apache.commons.io.output.CountingOutputStream;
import org.sandboxpowered.sandbox.fabric.internal.IDownloadIndicator;

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

public class FileUtil {
    private FileUtil() {
    }

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
            try {
                tracker.init(getFileSize(url));
            } catch (IOException e) {
                return;
            }
            try (InputStream input = url.openStream()) {
                Path downloads = Paths.get("downloads");
                Files.createDirectories(downloads);

                Path temp = downloads.resolve(UUID.randomUUID().toString() + ".sbxdl");
                int totalDataRead = 0;
                try (CountingOutputStream output = new CountingOutputStream(Files.newOutputStream(temp))) {
                    byte[] data = new byte[1024];
                    int b;
                    while ((b = input.read(data, 0, 1024)) >= 0) {
                        output.write(data);
                        totalDataRead += b;
                        tracker.set(totalDataRead);
                    }
                }
                if (Files.exists(out))
                    Files.delete(out);
                if (Files.notExists(out.getParent()))
                    Files.createDirectories(out.getParent());
                Files.move(temp, out);
                tracker.finish();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                tracker.finish();
            }
        }).start();
        return tracker;
    }

    public static long getFileSize(URL url) throws IOException {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            return conn.getContentLengthLong();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    static class DownloadTracker implements IDownloadIndicator {
        long totalSize;
        long currentSize;
        boolean complete;
        boolean hasStarted;

        void init(long totalSize) {
            this.totalSize = totalSize;
            this.hasStarted = true;
        }

        public void set(long byteCount) {
            this.currentSize = byteCount;
        }

        void finish() {
            this.complete = true;
        }

        @Override
        public long getCurrentSize() {
            return currentSize;
        }

        @Override
        public long getTotalSize() {
            return totalSize;
        }

        @Override
        public boolean isComplete() {
            return complete;
        }

        @Override
        public boolean hasStarted() {
            return hasStarted;
        }
    }

}