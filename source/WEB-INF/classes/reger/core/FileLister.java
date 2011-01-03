package reger.core;

import reger.filesync.server.*;
import reger.systemproperties.AllSystemProperties;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

/**
 * User: Joe Reger Jr
 * Date: Apr 3, 2007
 * Time: 9:53:27 AM
 */
public class FileLister {

    private ArrayList listOfFilesAndPaths = new ArrayList();

    public FileLister(){
        File rootPath = new File(AllSystemProperties.getProp("PATHUPLOADMEDIA")+"files"+java.io.File.separator);
        processDirectories(rootPath);
    }

    public FileLister(File rootPath){
        processDirectories(rootPath);
    }

    private void processDirectories(File in) {
        if (in.getAbsolutePath().length()>225){
            listOfFilesAndPaths.add(in.getAbsolutePath());
            Debug.debug(3, "FileLister", in.getAbsolutePath());
        } else {
            if (in.isDirectory()) {
                String[] children = in.list();
                for (int i=0; i<children.length; i++) {
                    processDirectories(new File(in, children[i]));
                }
            }
        }
    }


    public ArrayList getListOfFilesAndPaths() {
        return listOfFilesAndPaths;
    }
}
