package com.zlibrary.base.entity;

import java.io.File;


public class ZReqFile {

    private String name;
    private File file;
    private ZReqFileType type;

    public ZReqFile(String name, File file, ZReqFileType type) {
        this.name = name;
        this.file = file;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ZReqFileType getType() {
        return type;
    }

    public void setType(ZReqFileType type) {
        this.type = type;
    }

    public long getLength() {
        if (file.isFile() && file.exists()) {
            return file.length();
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("文件名：").append(name).append("<--->");
        sb.append("文件地址：").append(file.getPath()).append("<--->");
        sb.append("文件类型：").append(type.getType());
        return sb.toString();
    }
}
