package com.hrznstudio.sandbox.api.addon;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.util.List;

public class AddonInfo {
    @Expose
    private String title;
    @Expose
    private String type;
    @Expose
    private List<String> tags;
    @Expose
    private List<String> ignore;

    private File file;

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getIgnore() {
        return ignore;
    }

    public File getFile() {
        return file;
    }

    public AddonInfo setFile(File file) {
        this.file = file;
        return this;
    }
}