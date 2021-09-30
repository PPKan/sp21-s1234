package gitlet;

import edu.princeton.cs.algs4.ST;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
        STAGE_DIR.mkdir();
        REMOVE_DIR.mkdir();
        HEAD_DIR.mkdir();
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
     * if it was at the time of the command.
     * 1. check the file in blobs
     * if the name are the same, return if the content is identical
     * else add files into the blob directory and
     * 2. check files in staging directory
     * if the name are the same, do nothing if the content is identical
     * else replace the file in the staging directory. */
    public static void add(String addFile) {


        Commit master = Utils.readObject(MASTER_DIR.listFiles()[0], Commit.class);

        Boolean blobFileBool = false;
        Boolean blobSameBool = false;
        Boolean stageFileBool = false;
        Boolean stageSameBool = false;
        String blobSha1 = null;
        String stageSha1 = null;

        File file = getFileFromDir(CWD, addFile);
        String fileSha1 = contentSha1(file);

        /* look for the file in stage, if there's a same name file with
        different content, update it, else do nothing.
        1. compare its name
        if same - compare its content
        if different/nothing - do nothing
        use file's name as a staged file. convert it while commiting.
        */
        File[] stageList = STAGE_DIR.listFiles();
        if (stageList.length > 0) {
            for (File f : stageList) {
                /* update content if name are same but with different content */
                if (f.getName().equals(addFile) && !contentSha1(f).equals(fileSha1)) {
                    f.delete();
//                    File newFile = Utils.join(STAGE_DIR, addFile);
//                    Utils.writeContents(newFile, file);
                } else if (contentSha1(f).equals(fileSha1)) {
                    throw new Error("don't add same thing twice");
                }
            }
        }

        /* look for file in blob, if there's a same file, return */
        File[] blobList = BLOBS_DIR.listFiles();
        if (blobList.length > 0) {
            for (File f : blobList) {
                if (contentSha1(f).equals(fileSha1)) {
                    throw new Error("the file has been committed");
                }
            }
        }

        /* add file to blob and stage */
        File stageFile = Utils.join(STAGE_DIR, addFile);
        File blobFile = Utils.join(BLOBS_DIR, fileSha1);
        Utils.writeContents(STAGE_DIR, stageFile);
        Utils.writeContents(BLOBS_DIR, blobFile);

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
        byte[] addFileByte = new byte[0];
        try {
            addFileByte = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new Error("fail on converting to byte");
        }
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

    public static byte[] getByte(File file) {
        byte[] addFileByte = new byte[0];
        try {
            addFileByte = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new Error("fail on converting to byte");
        }
        return addFileByte;
    }

    public void status() {

    }







}
