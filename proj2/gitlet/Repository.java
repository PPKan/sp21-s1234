package gitlet;

import java.io.File;
import java.util.Date;

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
    public static final File GIT_DIR = join(CWD, ".gitlet");
    /** The staging directory. */
    public static final File BLOBS_DIR = join(GIT_DIR, "blobs");
    /** The commit directory. */
    public static final File COMMIT_DIR = join(GIT_DIR, "commit");
    /** The master directory. */
    public static final File MASTER_DIR = join(GIT_DIR, "master");


    /** Set up a staging directory. */
    public static void setupDir() {
        if (!GIT_DIR.exists()) {
            GIT_DIR.mkdir();
        } else {
            throw new Error("A Gitlet version-control system already exists in the current directory.");
        }
        BLOBS_DIR.mkdir();
        COMMIT_DIR.mkdir();
        MASTER_DIR.mkdir();
    }

    /**
     * Creates a new Gitlet version-control system in the current directory.
     * This system will automatically start with one commit: a commit that
     * contains no files and has the commit message initial commit (just like
     * that, with no punctuation). It will have a single branch: master, which
     * initially points to this initial commit, and master will be the current
     * branch. The timestamp for this initial commit will be 00:00:00 UTC,
     * Thursday, 1 January 1970 in whatever format you choose for dates (this
     * is called "The (Unix) Epoch", represented internally by the time 0.)
     * Since the initial commit in all repositories created by Gitlet will
     * have exactly the same content, it follows that all repositories will
     * automatically share this commit (they will all have the same UID) and all
     * commits in all repositories will trace back to it. */
    public static void init() {
        setupDir();
        new Commit("initial commit");
    }

    /** Adds a copy of the file as it currently exists to the staging area (see the
     * description of the commit command). For this reason, adding a file is also
     * called staging the file for addition. Staging an already-staged file overwrites
     * the previous entry in the staging area with the new contents. The staging
     * area should be somewhere in .gitlet. If the current working version of the
     * file is identical to the version in the current commit, do not stage it to
     * be added, and remove it from the staging area if it is already there (as can
     * happen when a file is changed, added, and then changed back to it's original
     * version). The file will no longer be staged for removal (see gitlet rm),
     * if it was at the time of the command. */
    public static void add(String addFile) {

        /* If the current working version of the file is identical to the version
        in the current commit, do not stage it to be added.
        But weird! How do I compare a file in commit to the addFile?
        By sha1? hashcode?
        */
        Commit master = Utils.readObject(MASTER_DIR.listFiles()[0], Commit.class);
        if (master.getMap().get(addFile).equals(addFile)) {

        }

        File[] fileList = CWD.listFiles();
        File file = null;
        for (File f : fileList) {
            if (f.getName().equals(addFile)) {
                file = f;
            }
        }
//        String fileName = Utils.sha1(file.getName());
        File add = Utils.join(BLOBS_DIR, file.getName());
        Utils.writeObject(add, file);
    }

    public void status() {

    }







}
