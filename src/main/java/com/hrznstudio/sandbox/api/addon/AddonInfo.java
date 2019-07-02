package com.hrznstudio.sandbox.api.addon;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.util.List;

public class AddonInfo {
    @Expose
    private String title;
    @Expose
    private String id;
    @Expose
    private List<String> ignore;

    private FolderStructure file;

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public List<String> getIgnore() {
        return ignore;
    }

    public FolderStructure getFolder() {
        return file;
    }

    public AddonInfo setFile(FolderStructure file) {
        this.file = file;
        return this;
    }

    public static class FolderStructure {
        private final File folder;

        public FolderStructure(File folder) {
            this.folder = folder;
        }

        public File asFile() {
            return folder;
        }

        public File getSubFile(String path) {
            return new File(folder, path);
        }

        public FolderStructure getSubFolder(String path) {
            return new FolderStructure(getSubFile(path));
        }

        public String getDirName() {
            return folder.getName();
        }

    }
}