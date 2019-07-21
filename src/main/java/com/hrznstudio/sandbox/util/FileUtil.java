package com.hrznstudio.sandbox.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
}