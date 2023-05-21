package com.tree.filesmanagerui;

public class OneFile {
    String name;
    String path;
    String type;
    String suffix;
    String size_B;
    String duration_s;

    OneFile (String name, String path, String type, String suffix) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.suffix = suffix;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
