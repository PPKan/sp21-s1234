package gitlet;

import java.io.File;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @PeterKan
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */



    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITDIR = Utils.join(CWD, ".gitlet");
    /** The staging directory. */
    public static final File STAGE_DIR = join(GITDIR, "staging");

    public static File stageInfo = join(STAGE_DIR, "stageInfo");

    /* TODO: fill in the rest of this class. */

    /** Set up a staging directory. */
    public static void setupStagingDir() {
        if (!STAGE_DIR.exists()) {
            STAGE_DIR.mkdir();
        }
    }

    /** Use writeObject to save files to the storing area. */
    public static void add(File addFile) {
        setupStagingDir();
        String fileName = addFile.getName();
        File staging = join(STAGE_DIR, fileName);
        if (!staging.equals(addFile)) {
            Utils.writeObject(staging, addFile);
        }
    }

    public void status() {
        
    }







}
