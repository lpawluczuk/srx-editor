/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srxeditor.api;

import org.openide.filesystems.FileObject;

/**
 *
 * @author lukasz
 */
public abstract class MainFileProvider {

    public abstract FileObject getMainFile();

    public abstract void setMainFile(FileObject file);

    public boolean isMainFile(FileObject file) {
        return file.equals(getMainFile());
    }
}
