/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srxeditor.components;

import com.srxeditor.projecttype.RulesProject;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JCheckBox;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.srxeditor.components//Documents//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "DocumentsTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "properties", openAtStartup = true)
@ActionID(category = "Window", id = "com.srxeditor.components.DocumentsTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_DocumentsAction",
        preferredID = "DocumentsTopComponent"
)
@Messages({
    "CTL_DocumentsAction=Documents",
    "CTL_DocumentsTopComponent=Documents Window",
    "HINT_DocumentsTopComponent=This is a Documents window"
})
public final class DocumentsTopComponent extends TopComponent {

    private Map<FileObject, Boolean> filesChecks = new HashMap<FileObject, Boolean>();
    private final Project project;

    public DocumentsTopComponent() {
        project = OpenProjects.getDefault().getOpenProjects()[0];  //.getMainProject(); TODO fix
//        if (project == null || !project.getClass().equals(RulesProject.class)) {
//            System.out.println("No main project or wrong main project! Exiting DocumentsComponent initialization.");
//            return;
//        }

//        initComponents();
        init();
        setName(Bundle.CTL_DocumentsTopComponent());
        setToolTipText(Bundle.HINT_DocumentsTopComponent());

    }

    private void init() {
        removeAll();
        Map<FileObject, Boolean> tempFileChecks = null;
        if (!filesChecks.isEmpty()) {
            tempFileChecks = new HashMap<FileObject, Boolean>(filesChecks);
        }

        filesChecks = new HashMap<FileObject, Boolean>();

        setLayout(new java.awt.GridLayout(10, 1));

        FileObject documentFolder = project.getLookup().lookup(RulesProject.class).getDocumentsFolder(false);
        documentFolder.addFileChangeListener(new FileChangeListener() {

            @Override
            public void fileFolderCreated(FileEvent fe) {
            }

            @Override
            public void fileDataCreated(FileEvent fe) {
                init();
            }

            @Override
            public void fileChanged(FileEvent fe) {
            }

            @Override
            public void fileDeleted(FileEvent fe) {
                init();
            }

            @Override
            public void fileRenamed(FileRenameEvent fe) {
                init();
            }

            @Override
            public void fileAttributeChanged(FileAttributeEvent fe) {
            }
        });

        FileObject[] documents = documentFolder.getChildren();

        for (FileObject document : documents) {

            JCheckBox jCheckBox = new JCheckBox(document.getName());
            jCheckBox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    JCheckBox checkBox = (JCheckBox) e.getItem();
                    for (FileObject file : filesChecks.keySet()) {
                        if (file.getName().equals(checkBox.getText())) {
                            filesChecks.put(file, checkBox.isSelected());
                            break;
                        }
                    }
                    project.getLookup().lookup(RulesProject.class).setActiveDocuments(getActiveDocuments());
                }
            });

            if (tempFileChecks != null && tempFileChecks.containsKey(document)) {
                jCheckBox.setSelected(tempFileChecks.get(document));
                filesChecks.put(document, tempFileChecks.get(document));
            } else {
                jCheckBox.setSelected(true);
                filesChecks.put(document, Boolean.TRUE);
            }
            add(jCheckBox);
        }
        project.getLookup().lookup(RulesProject.class).setActiveDocuments(getActiveDocuments());
    }

    private FileObject[] getActiveDocuments() {
        List<FileObject> activeDocumnets = new LinkedList<FileObject>();

        for (FileObject fileObject : filesChecks.keySet()) {
            if (filesChecks.get(fileObject)) {
                activeDocumnets.add(fileObject);
            }
        }
        return activeDocumnets.toArray(new FileObject[0]);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

}
