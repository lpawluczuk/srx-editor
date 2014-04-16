/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srxeditor.rulefile;

import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;

/**
 *
 * @author lukasz
 */
public class DocumentDataNode extends DataNode {

    public DocumentDataNode(DataObject obj) {
        super(obj, Children.LEAF);
    }
}
