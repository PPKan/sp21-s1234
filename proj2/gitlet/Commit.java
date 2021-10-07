package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serial;
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
    private byte[] byteArray;
    /** Parent is a sha1 code of its parent. */
    private String parent;
    private String log;
    /** Store information */
    private HashMap<String, Node> stageMap = new HashMap<>();

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

    /** log txt file */
    public static final File LOG = join(GIT_DIR, "log.txt");

    /* a weird stuff that I don't know */
    @Serial
    private static final long serialVersionUID = 6892974342084411122L;


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
    public Commit() {

    }

    public static void getCommit(String message) {

        Commit fileCommit = new Commit();

        File masterCommit = null;
        File headCommit = null;
        File newCommit = null;
        fileCommit.message = message;
        File head = null;

        /* to distinguish from initial commit (parent is null), initial commit
        commits no file */
        if (!fileCommit.message.equals("initial commit")) {

            /* current time */
            fileCommit.timestamp = new Date().toString();

            /* get the file from head as parent  */
            head = HEAD_DIR.listFiles()[0];
            fileCommit.parent = head.getName();

            /* get set from parent */
            fileCommit.stageMap = Utils.readObject(head, Commit.class).getMap();

            File[] stageList = STAGE_DIR.listFiles();
            File[] removeList = REMOVE_DIR.listFiles();
            File[] cwdList = CWD.listFiles();

            if (stageList.length == 0 && removeList.length == 0 ) {
                /* the error message needed to be revised */
                throw new Error("Nothing was in the stage list or remove list");
            }

            /* Commit staged files */
            for (File f : stageList) {
                Repository repF = Utils.readObject(f, Repository.class);
                Node rep = new Node(repF.name, repF.sha1, repF.byteArray);
                fileCommit.stageMap.put(repF.name, rep);
                f.delete();
            }

            /* remove from map if there's staged for deletion */
            if (removeList.length != 0) {
                for (File r : removeList) {
                    Repository rev = Utils.readObject(r, Repository.class);
                    if (fileCommit.stageMap.containsKey(rev.name)) {
                        fileCommit.stageMap.remove(rev.name);
                    } else {
                        throw new Error("the file isn't in the head file");
                    }
                    r.delete();
                    for (File c : cwdList) {
                        if (c.getName().equals(r.getName())) {
                            c.delete();
                        }
                    }
                }
            }

            /* construct new commit */
            fileCommit.name = Utils.sha1(fileCommit.timestamp, message);
            newCommit = Utils.join(COMMIT_DIR, fileCommit.name);

            /* construct log file */
            String logString = Utils.readContentsAsString(LOG);
            Utils.writeContents(LOG, fileCommit.logMessage(false), logString);

        } else {

            /* current time */
            fileCommit.timestamp = new Date(0).toString();
            /* no parent on first commit */
            fileCommit.parent = null;
            /* construct initial commit */
            fileCommit.name = Utils.sha1(fileCommit.timestamp, message);
            masterCommit = Utils.join(MASTER_DIR, fileCommit.name);
            Utils.writeObject(masterCommit, fileCommit);
            newCommit = Utils.join(COMMIT_DIR, fileCommit.name);
            /* construct log file */
            Utils.writeContents(LOG, fileCommit.logMessage(true));

        }

        /* add the commit to head file */
        if (head != null ) {
            head.delete();
        }
        headCommit = Utils.join(HEAD_DIR, fileCommit.name);
        Utils.writeObject(headCommit, fileCommit);

        /* create new commit -> new commit on COMMIT_DIR, init in MASTER_DIR */
        Utils.writeObject(newCommit, fileCommit);
    }


    public static class Node implements Serializable{

        private String name;
        private String sha1;
        private byte[] content;

        public Node(String name, String sha1, byte[] content) {
            this.name = name;
            this.sha1 = sha1;
            this.content = content;
        }

        public byte[] getContent() {
            return content;
        }

        public String getName() {
            return name;
        }

        public String getSha1() {
            return sha1;
        }

    }


//    private HashSet<String> getStringSet() {
//        return new HashSet<String>();
//    }

    /** get the log block */
    public String logMessage(boolean init) {
        String logContent =  "===" + "\n"
                + "commit " + this.name + "\n"
                + "Date: " + this.timestamp + "\n"
                + this.message;

        /* get log file to string */
        if (!init) {
            logContent = logContent + "\n";
        }

        return logContent;

    }

    public String getLog() {
        return this.log;
    }

    public String getMessage() {
        return this.message;
    }

    public String getName() {
        return this.name;
    }

    public HashMap<String, Node> getMap() {
        return stageMap;
    }




}
