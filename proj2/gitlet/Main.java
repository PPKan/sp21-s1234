package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Commit com = new Commit();
                com.printLogs();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            case "commit":
                String message = args[1];
                Commit newCom = new Commit(message);
                newCom.printLogs();
                break;
        }
    }
}
