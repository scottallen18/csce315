import java.util.ArrayList;

public class node {
    private String data;
    private ArrayList<node> children;
    private node previous;
    private tree tree;
    private tree otherTree;
    private node connection;
    private int height;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private tree getOtherTree() {
        return otherTree;
    }

    public void setOtherTree(tree otherTree) {
        this.otherTree = otherTree;
    }

    // Constructors, the second one is for dynamic creation of nodes,
    // the first is for the first nodes created
    node(String data) {
        this.data = data;
        this.children = new ArrayList<node>();
        this.previous = null;
        this.tree = null;
        this.otherTree = null;
        this.height = 0;
    }

    private node(String data, node previous, tree tree, tree otherTree, int height) {
        this.data = data;
        this.children = new ArrayList<node>();
        this.previous = previous;
        this.tree = tree;
        this.otherTree = otherTree;
        this.height = height;
    }

    ArrayList<node> getChildren() {
        return children;
    }

    // If the child already exists in the list then dont add it,
    // If it doesnt, then add it and check the other tree
    void addChild(String s1) {
        node child = new node(s1, this, this.getTree(), this.getOtherTree(), (this.height + 1));
         if (!alreadyExists(child)) {
             children.add(child);
             if (checkOtherTree(child.otherTree, child)) {
                 printOutConnection(child);
                 child.getTree().setConnectionFound(true);
                 child.getConnection().getTree().setConnectionFound(true);
             }
         }
    }

    private void printOutConnection(node child) {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu/brian.lu32_605",
                    username, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        backward(child);
        forward(child.connection);

    }
    private void backward(node n){

        if(n.previous != null)
            backward(n.previous);
        if (n.getHeight() % 2 ==0)
            System.out.println(n.data + " connects to: ");
        else {
            ArrayList<String>answer = new ArrayList<>();
            try {
                sqlStatement = "SELECT primaryTitle FROM title_basics WHERE tconst = '" + n.getData() + "';";

                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(sqlStatement);
                while (result.next()) {
                    answer.add(result.getString("primaryTitle"));
                }
                System.out.println(answer.get(0) + " connects to: ");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void forward(node n){
        n = n.previous;
        if(n.previous != null) {
            if (n.getHeight() % 2 ==0)
                System.out.println(n.data + " connects to: ");
            else {
                ArrayList<String>answer = new ArrayList<>();
                try {
                    sqlStatement = "SELECT primaryTitle FROM title_basics WHERE tconst = '" + n.getData() + "';";

                    Statement stmt = conn.createStatement();
                    ResultSet result = stmt.executeQuery(sqlStatement);
                    while (result.next()) {
                        answer.add(result.getString("primaryTitle"));
                    }
                    System.out.println(answer.get(0) + " connects to: ");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            forward(n.previous);
        }
        else
            System.out.println(n.data);
    }
    public node getPrevious() {
        return previous;
    }
    public void setPrevious(node previous) {
        this.previous = previous;
    }
    public void setData(String s) {
        this.data = s;
    }
    public String getData() {
        return data;
    }

    public boolean alreadyExists(node n){
        for(node each : n.getTree().getRoot().getChildren()) {
            if (n.getData().equals(each.getData())) {
                return true;
            }
            else
                alreadyExists(each);
        }
        return false;
    }

    public boolean connectionInOtherTree(node n, node child){
        for(node each : n.getChildren()) {
            if (child.getData().equals(each.getData())) {
                child.setConnection(each);
                each.setConnection(child);
                return true;
            }
            else
                connectionInOtherTree(each, child);
        }
        return false;
    }

    public boolean checkOtherTree(tree tree, node child) {
        if (connectionInOtherTree(tree.getRoot(), child))
            return true;
        else
            return false;
    }

    public tree getTree() {
        return tree;
    }

    public void setTree(tree tree) {
        this.tree = tree;
    }

    public node getConnection() {
        return connection;
    }

    public void setConnection(node connection) {
        this.connection = connection;
    }

}
