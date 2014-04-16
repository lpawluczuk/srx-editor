/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srxeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Build",
        id = "com.srxeditor.RunSRXActionListener"
)
@ActionRegistration(
        iconBase = "com/srxeditor/play.png",
        displayName = "#CTL_RunSRXActionListener"
)
@ActionReference(path = "Toolbars/Build", position = 250)
@Messages("CTL_RunSRXActionListener=Run SRX")
public final class RunSRXActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("helo");
        
        // get the documents folder and run it against srx-rules file
        
    }
}
