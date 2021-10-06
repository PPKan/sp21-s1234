package gitlet;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @PeterKan
 */
public class Repository implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    String name;
    String sha1;
    byte[] byteArray;

    /** The current working directory. */
    public static final File WD = new File(System.getProperty("user.dir"));
    /** folder to put some testing file */
    public static final File CWD = join(WD, "files");
    /** The .gitlet directory. */
    public static final File GIT_DIR = join(WD, ".gitlet");
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

    /** Set up a staging directory. */
    public static void setupDir() {
        if (!GIT_DIR.exists()) {
            GIT_DIR.mkdir();
        } else {
            throw new Error("A Gitlet version-control system already exists in the current directory.");
        }
        CWD.mkdir();
        BLOBS_DIR.mkdir();
        COMMIT_DIR.mkdir();
        MASTER_DIR.mkdir();
        STAGE_DIR.mkdir();
        REMOVE_DIR.mkdir();
        HEAD_DIR.mkdir();
    }

    public Repository(String sha1, String name, byte[] byteArray) {
        this.sha1 = sha1;
        this.name = name;
        this.byteArray = byteArray;
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
        Commit.getCommit("initial commit");
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
     * 1. put the file in blob with the name of its sha1
     * return if there are already a same file in blob
     * 2. place the class file in staging area with the name of its file name
     * if the class file name are same, replace the file with the new content.
      */
    public static void add(String addFile) {

        File file = getFileFromDir(CWD, addFile);
        byte[] byteArray = getByte(file);
        Repository res = new Repository(contentSha1(file), addFile, byteArray);
//        System.out.println(res.sha1);
//        System.out.println(sha1(addFile, byteArray));

        /* look for file in blob, if there's a same file, return */
        File[] blobList = BLOBS_DIR.listFiles();
        if (blobList.length > 0) {
            for (File f : blobList) {
                if (f.getName().equals(res.sha1)) {
                    throw new Error("the file has been committed");
                }
            }
        }

        /* look for the file in stage, if there's a same name file with
        different content, update it, else do nothing.
        1. compare its name
        if same - compare its content
        if different/nothing - do nothing
        use file's name as a staged file. convert it while committing.
        */
        File[] stageList = STAGE_DIR.listFiles();
        if (stageList.length > 0) {
            for (File f : stageList) {
                Repository resF = Utils.readObject(f, Repository.class);
                /* update content if name are same but with different content */
                if (resF.name.equals(addFile) && !resF.sha1.equals(res.sha1)) {
                    f.delete();
                } else if (contentSha1(f).equals(res.sha1)) {
                    throw new Error("don't add same thing twice");
                }
            }
        }


        /* add file to blob and stage */
        File stageFile = Utils.join(STAGE_DIR, res.name);
        File blobFile = Utils.join(BLOBS_DIR, res.sha1);
        Utils.writeObject(stageFile, res);
        Utils.writeContents(blobFile, res.byteArray);

    }


    /** a method for test file sameness */
    public static boolean same(String file1, String file2) {
        File fileone = null;
        File filetwo = null;

        File[] fileList = CWD.listFiles();
        for (File f : fileList) {
            if (f.getName().equals(file1)) {
                fileone = f;
            }
        }

        File[] fileList2 = BLOBS_DIR.listFiles();
        for (File f : fileList2) {
            if (f.getName().equals(file2)) {
                filetwo = f;
            }
        }

        byte[] fileContent1 = new byte[0];
        try {
            fileContent1 = Files.readAllBytes(fileone.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] fileContent2 = new byte[0];
        try {
            fileContent2 = Files.readAllBytes(filetwo.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String one = sha1(fileContent1);
        String two = sha1(fileContent2);

        System.out.println(one);
        System.out.println(two);

        if (one.equals(two)) {
            return true;
        } else {
            return false;
        }

    }

    public static String contentSha1(File file) {
        byte[] addFileByte = getByte(file);
        String fileSha1 = sha1(file.getName(), addFileByte);
        return fileSha1;
    }

    public static File getFileFromDir(File dir, String file) {
        File[] fileList = dir.listFiles();
        File tempFile = null;

        for (File f : fileList) {
            if (f.getName().equals(file)) {
                tempFile = f;
                return tempFile;
            }
        }
        return null;
    }

    public static boolean removeFileFromDir(File dir, String file) {
        File[] fileList = dir.listFiles();
        if (fileList.length > 0) {
            for (File f : fileList) {
                if (f.getName().equals(file)) {
                    f.delete();
                    return true;
                }
            }
        }
        return false;
    }

    public static byte[] getByte(File file) {
        byte[] addFileByte = new byte[0];
        try {
            addFileByte = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new Error("fail on converting to byte");
        }
        return addFileByte;
    }

    /** Unstage the file if it is currently staged for addition.
     * If the file is tracked in the current commit, stage it for
     * removal and remove the file from the working directory if
     * the user has not already done so (do not remove it unless
     * it is tracked in the current commit).   */
     public static void remove(String fileName) {

         File cwdFile = getFileFromDir(CWD, fileName);
         String cwdFileSha1 = contentSha1(cwdFile);
         System.out.println(cwdFileSha1);

         /* remove the file from staging and blob area */
         String rmSha1 = contentSha1(getFileFromDir(CWD, fileName));
         if (removeFileFromDir(STAGE_DIR, fileName)) {
             removeFileFromDir(BLOBS_DIR, rmSha1);
         }
//         System.out.println(rmSha1);

         /* remove the file from CWD if it is tracked by head */
        Commit headCommit = Utils.readObject(HEAD_DIR.listFiles()[0], Commit.class);
        List<Commit.Node> list = new ArrayList<Commit.Node>(headCommit.getMap().values());
        for (Commit.Node n : list) {
            if (n.getSha1().equals(cwdFileSha1)) {
                cwdFile.delete();
            } else {
                System.out.println("123");
            }
        }
    }

    public static void log() {
        String output = readContentsAsString(LOG);
        System.out.println(output);
    }

    public static void globalLog() {
         File[] commitList = COMMIT_DIR.listFiles();
         for (File f : commitList) {
             Commit c = Utils.readObject(f, Commit.class);
             System.out.println(c.logMessage(true));
         }
    }

    public static void find(String message) {
        File[] commitList = COMMIT_DIR.listFiles();
        for (File f : commitList) {
            Commit c = Utils.readObject(f, Commit.class);
            if (c.getMessage().equals(message)) {
                System.out.println("===");
                System.out.println(message);
                System.out.println(c.getName());
            }
        }
    }


    public static void status() {

        File[] masterList = HEAD_DIR.listFiles();
        if (masterList.length < 1) {
            throw new Error("please init the gitlet first");
        }

        String stageFile = statusString(STAGE_DIR);
        String removeFile = statusString(REMOVE_DIR);

         System.out.println(
                 "=== Branches ===" + "\n"
                 + "*master" + "\n"
                 + "\n"
                 + "=== Staged Files ===" + "\n"
                 + stageFile + "\n"
                 + "\n"
                 + "=== Removed Files ===" + "\n"
                 + removeFile
         );
    }

    private static String statusString(File dir) {
        String file = "";
        File[] list = dir.listFiles();
        if (list.length > 0) {
            int counter = 0;
            for (File f : list) {
                if (counter == list.length) {
                    file = file + f.getName();
                } else {
                    file = file + f.getName() + "\n";
                }
                counter += 1;
            }
        }
        return file;
    }


    public static void checkout (String[] args) {
        if (args[0].length() == 40) {
            if (args.length == 2) {
                checkIdFile(args[0], args[1]);
            } else {
                checkSha1(args[0]);
            }
        } else {
            checkName(args[0]);
        }
    }

    /** 1. Takes the version of the file as it exists in the head commit and
     * puts it in the working directory, overwriting the version of the
     * file that's already there if there is one. The new version of the
     * file is not staged. */
    private static void checkName(String name) {
        Commit headCommit = readObject(HEAD_DIR.listFiles()[0], Commit.class);
        HashMap<String, Commit.Node> headMap = headCommit.getMap();
        File checkFile = Utils.join(CWD, name);
        if (headMap.containsKey(name)) {
            Utils.writeContents(checkFile, headMap.get(name).getContent());
        } else {
            throw new Error("this file does not exist in head");
        }
    }

    /**Takes all files in the commit at the head of the given branch, and
     * puts them in the working directory, overwriting the versions of the
     * files that are already there if they exist. Also, at the end of this
     * command, the given branch will now be considered the current branch
     * (HEAD). Any files that are tracked in the current branch but are not
     * present in the checked-out branch are deleted. The staging area is
     * cleared, unless the checked-out branch is the current branch (see
     * Failure cases below). */
    private static void checkSha1(String Sha1) {

        /* check the file in cwd to make sure they are all committed
        * (need to traverse through all files?) */

        /* get head map */

        /* delete all file in cwd */

        /* put all file from the sha1 into cwd */

        /* the sha1 become head */


    }

    /** 2. Takes the version of the file as it exists in the commit with the
     * given id, and puts it in the working directory, overwriting the version
     * of the file that's already there if there is one. The new version of
     * the file is not staged. */
    private static void checkIdFile (String id, String name) {

        File deleteFile = null;

        /* get the certain commit */
        File[] commitList = COMMIT_DIR.listFiles();
        Commit checkCommit = null;
        for (File f : commitList) {
            if (f.getName().equals(id)) {
                checkCommit = readObject(f, Commit.class);
            }
        }
        if (checkCommit == null) {
            throw new Error("wrong commit id");
        }

        /* remove file in CWD */
        File[] cwdList = CWD.listFiles();
        for (File g : cwdList) {
            if (g.getName().equals(name)) {
                deleteFile = g;
            }
        }

        HashMap<String, Commit.Node> checkMap = checkCommit.getMap();
        /* put file in CWD */
        File checkFile = Utils.join(CWD, name);
        if (checkMap.containsKey(name)) {
            if (deleteFile != null) {
                deleteFile.delete();
            }
//            checkFile = Utils.join(CWD, name);
            Utils.writeContents(checkFile, checkMap.get(name).getContent());
        } else {
            throw new Error("this file does not exist in commit");
        }

    }







}
