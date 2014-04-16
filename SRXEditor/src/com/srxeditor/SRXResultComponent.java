/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srxeditor;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@TopComponent.Description(
        preferredID = "SRXResultComponent",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(
        mode = "explorer",
        openAtStartup = true)
@ActionID(
        category = "Window",
        id = "com.srxeditor.SRXResultComponent")
@ActionReferences({
    @ActionReference(
            path = "Menu/Window",
            position = 0)
})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_FeedAction")
@Messages({
    "CTL_FeedAction=Open Feed Window"})
public class SRXResultComponent extends TopComponent {

    @Messages({
        "CTL_SRXResultComponent=SRX Results Window",
        "HINT_SRXResultComponent=This is a SRX Results Window"})
    private SRXResultComponent() {
        setName(Bundle.CTL_SRXResultComponent());
        setToolTipText(Bundle.HINT_SRXResultComponent());
    }
}
