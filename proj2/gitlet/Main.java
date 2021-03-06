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
                Repository.init();
                break;
            case "add":
                Repository.add(args[1]);
                break;
            case "commit":
                Commit.getCommit(args[1]);
                break;
            case "rm":
                Repository.remove(args[1]);
                break;
            case "same":
                System.out.println(Repository.same(args[1], args[2]));
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.globalLog();
                break;
            case "find":
                Repository.find(args[1]);
                break;
            case "status":
                Repository.status();
                break;
            case "checkout":
                String[] check = new String[args.length - 1];
                int count = 0;
                for (int i=1; i<args.length; i+=1) {
                    check[count] = args[i];
                    count += 1;
                }
                Repository.checkout(check);
                break;

        }
    }
}
