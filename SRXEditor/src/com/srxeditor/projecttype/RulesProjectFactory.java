package com.srxeditor.projecttype;

import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ProjectFactory.class)
public class RulesProjectFactory implements ProjectFactory {

    public static final String PROJECT_FILE = "srx-rules.xml";

    //Specifies when a project is a project, i.e.,
    //if "customer.txt" is present in a folder:
    @Override
    public boolean isProject(FileObject projectDirectory) {
        return projectDirectory.getFileObject(PROJECT_FILE) != null;
    }

    //Specifies when the project will be opened, i.e., if the project exists:
    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new RulesProject(dir, state) : null;
    }

    @Override
    public void saveProject(final Project project) throws IOException, ClassCastException {
        FileObject projectRoot = project.getProjectDirectory();
        if (projectRoot.getFileObject(PROJECT_FILE) == null) {
            throw new IOException("Project dir " + projectRoot.getPath() + " deleted,"
                    + " cannot save project");
        }

        //Force creation of the documents/ dir if it was deleted
        project.getLookup().lookup(RulesProject.class).getDocumentsFolder(true);
    }

}
