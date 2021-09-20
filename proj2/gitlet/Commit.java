package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.util.Calendar;
import java.util.Date; // TODO: You'll likely use this in this class


/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @PeterKan
 */
public class Commit {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private Date date;
    private String message;
    private File commitFile;
    private String sha1;
    private final String breakPoint = "==========";

    private static File logs;
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GITDIR = Utils.join(CWD, ".gitlet");

    /** To check if the .gitlet is persisted */
    private static void setupDirPersistence() {
        if (!GITDIR.exists()) {
            GITDIR.mkdir();
        } else {
            throw new Error("A Gitlet version-control system already exists in the current directory.");
        }
    }

    /** Constructor for the initial commit */
    public Commit() {
        setupDirPersistence();
        logs = Utils.join(GITDIR, "logs.txt");
        date = new Date();
        message = "initial commit";
        sha1 = Utils.sha1(date.toString(), message);
        Utils.writeContents(logs,  breakPoint + "\n", sha1 + "\n", date.toString() + "\n", message);
    }


    /** Constructor for the new commit */
    public Commit(String typedMes) {
        date = new Date();
        message = typedMes;
        sha1 = Utils.sha1(date.toString(), message);
        String old = Utils.readContentsAsString(logs);
        Utils.writeContents(logs, old, "\n" + breakPoint + "\n", sha1 + "\n", date.toString() + "\n", message);
    }

    public void printLogs() {
        System.out.println(logs);
    }

    private void writeFile() {

    }

}
