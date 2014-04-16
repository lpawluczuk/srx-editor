package com.srxeditor.projecttype;

import com.srxeditor.SRXRunner;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

public class RulesProject implements Project {

    public static final String DOCUMENTS_DIR = "documents";

    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;

    public RulesProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.state = state;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    FileObject getDocumentsFolder(boolean create) {
        FileObject result
                = projectDir.getFileObject(DOCUMENTS_DIR);

        if (result == null && create) {
            try {
                result = projectDir.createFolder(DOCUMENTS_DIR);
            } catch (IOException ioe) {
                Exceptions.printStackTrace(ioe);
            }
        }
        return result;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
                this,
                state,
                new ActionProviderImpl(),
                new Info(),
                new RulesProjectLogicalView(this),});
        }
        return lkp;
    }

    private final class ActionProviderImpl implements ActionProvider {

        @Override
        public String[] getSupportedActions() {
            return new String[]{
                ActionProvider.COMMAND_RUN
            };
        }

        @Override
        public void invokeAction(String string, Lookup lookup) throws IllegalArgumentException {
            int idx = Arrays.asList(getSupportedActions()).indexOf(string);
            switch (idx) {
                case 0: //run
                    // TODO thread?

                    FileObject rules = null;

                    System.out.println("Rules");
                    for (FileObject fileObject : getProjectDirectory().getChildren()) {
                        if (fileObject.getExt().equals("xml")) {
                            rules = fileObject;
                            break;
                        }
                    }

                    SRXRunner srxr = new SRXRunner();
                    srxr.run(getLookup().lookup(RulesProject.class).getDocumentsFolder(false).getChildren(), rules);
//                    final RendererService ren = (RendererService) getLookup().lookup(RendererService.class);
//                    RequestProcessor.getDefault().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            FileObject image = ren.render();
//                            //If we succeeded, try to open the image
//                            if (image != null) {
//                                DataObject dob;
//                                try {
//                                    dob = DataObject.find(image);
//                                    OpenCookie open = (OpenCookie) dob.getNodeDelegate().getLookup().lookup(OpenCookie.class);
//                                    if (open != null) {
//                                        open.open();
//                                    }
//                                } catch (DataObjectNotFoundException ex) {
//                                    Exceptions.printStackTrace(ex);
//                                }
//                            }
//                        }
//                    });
                    break;
                default:
                    throw new IllegalArgumentException(string);
            }
        }

        @Override
        public boolean isActionEnabled(String string, Lookup lookup) throws IllegalArgumentException {
            // TODO check
            return true;
        }

    }

    private final class Info implements ProjectInformation {

        @StaticResource()
        public static final String CUSTOMER_ICON = "com/srxeditor/projecttype/sub-icon.png";

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(CUSTOMER_ICON));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public Project getProject() {
            return RulesProject.this;
        }

    }

    class RulesProjectLogicalView implements LogicalViewProvider {

        @StaticResource()
        public static final String CUSTOMER_ICON = "com/srxeditor/projecttype/sub-icon.png";

        private final RulesProject project;

        public RulesProjectLogicalView(RulesProject project) {
            this.project = project;
        }

        @Override
        public Node createLogicalView() {
            try {
//                FileObject documents = project.getDocumentsFolder(true);
//                DataFolder documentsDataFolder = DataFolder.findFolder(documents);
//                Node realDocumentsDataNode = documentsDataFolder.getNodeDelegate();
//
//                return new DocumentsNode(realDocumentsDataNode, project);

                //Obtain the project directory's node:
                FileObject projectDirectory = project.getProjectDirectory();
                DataFolder projectFolder = DataFolder.findFolder(projectDirectory);
                Node nodeOfProjectFolder = projectFolder.getNodeDelegate();
                //Decorate the project directory's node:
                return new ProjectNode(nodeOfProjectFolder, project);
            } catch (DataObjectNotFoundException donfe) {
                Exceptions.printStackTrace(donfe);
                //Fallback-the directory couldn't be created -
                //read-only filesystem or something evil happened
                return new AbstractNode(Children.LEAF);
            }
        }

        /**
         * This is the node you actually see in the Projects window for the
         * project
         */
        private final class DocumentsNode extends FilterNode {

            final RulesProject project;

            public DocumentsNode(Node node, RulesProject project) throws DataObjectNotFoundException {
                super(node, new FilterNode.Children(node),
                        //The projects system wants the project in the Node's lookup.
                        //NewAction and friends want the original Node's lookup.
                        //Make a merge of both:
                        new ProxyLookup(
                                Lookups.singleton(project),
                                node.getLookup())
                );
                this.project = project;
            }

            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(CUSTOMER_ICON);
            }

            @Override
            public Image getOpenedIcon(int type) {
                return getIcon(type);
            }

            @Override
            public String getDisplayName() {
                return project.getProjectDirectory().getName();
            }

        }

        private final class ProjectNode extends FilterNode {

            final RulesProject project;

            public ProjectNode(Node node, RulesProject project)
                    throws DataObjectNotFoundException {
                super(node,
                        new FilterNode.Children(node),
                        new ProxyLookup(
                                new Lookup[]{
                                    Lookups.singleton(project),
                                    node.getLookup()
                                }));
                this.project = project;
            }

            @Override
            public Action[] getActions(boolean arg0) {
                return new Action[]{
                    CommonProjectActions.newFileAction(),
                    CommonProjectActions.copyProjectAction(),
                    CommonProjectActions.deleteProjectAction(),
                    CommonProjectActions.closeProjectAction()
                };
            }

            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(CUSTOMER_ICON);
            }

            @Override
            public Image getOpenedIcon(int type) {
                return getIcon(type);
            }

            @Override
            public String getDisplayName() {
                return project.getProjectDirectory().getName();
            }

        }

        @Override
        public Node findPath(Node root, Object target) {
            //leave unimplemented for now
            return null;
        }

    }
}
