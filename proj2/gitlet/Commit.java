package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.LinkedHashMap;


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
    private Date date;
    private String message;
    private String name;
    /** Parent is a sha1 code of its parent. */
    private String parent;
    /** Store information */
    private LinkedHashMap library;

    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GIT_DIR = Utils.join(CWD, ".gitlet");
    public static final File COMMIT_DIR = Utils.join(GIT_DIR, "commit");

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
    public Commit(String message, String parent, Date date) {
        this.date = date;
        this.message = message;
        this.parent = parent;
        this.name = Utils.sha1(date.toString(), message);



        // check parents' files
        // update staged files

        File newCommit = Utils.join(COMMIT_DIR, name);
        Utils.writeObject(newCommit, this);
    }


}
