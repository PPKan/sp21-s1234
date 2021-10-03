package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.join;
import static gitlet.Utils.sha1;


/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @PeterKan
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The basic info of this Commit. */
    private String timestamp;
    private String message;
    private String name;
    /** Parent is a sha1 code of its parent. */
    private String parent;
    /** Store information */
    private HashSet<String> stageSet = new HashSet<>();

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GIT_DIR = join(CWD, ".gitlet");
    /** The staging directory. */
    public static final File BLOBS_DIR = join(GIT_DIR, "blobs");
    /** The staging directory. */
    public static final File STAGE_DIR = join(GIT_DIR, "stage");
    /** The removal directory. */
    public static final File REMOVE_DIR = join(GIT_DIR, "remove");
    /** The commit directory. */
    public static final File COMMIT_DIR = join(GIT_DIR, "commit");
    /** The head directory. */
    public static final File HEAD_DIR = join(GIT_DIR, "head");
    /** The master directory. */
    public static final File MASTER_DIR = join(GIT_DIR, "master");


    /**  Saves a snapshot of tracked files in the current commit and staging area so they can be
     * restored at a later time, creating a new commit. The commit is said to be tracking the saved
     * files. By default, each commit's snapshot of files will be exactly the same as its parent
     * commit's snapshot of files; it will keep versions of files exactly as they are, and not
     * update them. A commit will only update the contents of files it is tracking that have been
     * staged for addition at the time of commit, in which case the commit will now include the
     * version of the file that was staged instead of the version it got from its parent. A commit
     * will save and start tracking any files that were staged for addition but weren't tracked by its
     * parent. Finally, files tracked in the current commit may be untracked in the new commit as a
     * result being staged for removal by the rm command (below). */
    public Commit(String message) {

        File masterCommit = null;
        File headCommit = null;
        File newCommit = null;
        File masterFile = null;
        this.message = message;

        /* to distinguish from initial commit (parent is null), initial commit
        commits no file */
        if (!this.message.equals("initial commit")) {

            /* current time */
            this.timestamp = new Date().toString();

            /* get the file from head as parent  */
            File head = HEAD_DIR.listFiles()[0];
            this.parent = head.getName();

            /* get set from parent */
            this.stageSet = Utils.readObject(head, Commit.class).getSet();
            head.delete();

            /* Commit staged files */
            File[] stageList = STAGE_DIR.listFiles();
            if (stageList.length == 0) {
                /* the error message needed to be revised */
                throw new Error("Nothing was in the stage list");
            }
            for (File f : stageList) {
                Repository repF = Utils.readObject(f, Repository.class);
                stageSet.add(repF.sha1);
                f.delete();
            }

            /* construct new commit */
            this.name = Utils.sha1(timestamp, message);
            newCommit = Utils.join(COMMIT_DIR, name);
        } else {

            /* current time */
            this.timestamp = new Date(0).toString();
            /* no parent on first commit */
            this.parent = null;
            /* construct initial commit */
            this.name = Utils.sha1(timestamp, message);
            masterCommit = Utils.join(MASTER_DIR, name);
            Utils.writeObject(masterCommit, this);
            newCommit = Utils.join(COMMIT_DIR, name);


        }

        /* add the commit to head file */
        headCommit = Utils.join(HEAD_DIR, name);
        Utils.writeObject(headCommit, this);

        /* create new commit -> new commit on COMMIT_DIR, init in MASTER_DIR */
        Utils.writeObject(newCommit, this);
    }

//    private HashSet<String> getStringSet() {
//        return new HashSet<String>();
//    }

    public HashSet<String> getSet() {
        return stageSet;
    }




}
