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
public interface ViewService {

    boolean isRendered(FileObject file);

    boolean isUpToDate(FileObject file);

    void view(FileObject file);

}
