import com.sun.deploy.security.SelectableSecurityManager;

import java.util.ArrayList;

public class node {
    private String data;
    private ArrayList<node> children;
    private node previous;
    private tree tree;
    private tree otherTree;
    private node connection; //

    public tree getOtherTree() {
        return otherTree;
    }

    public void setOtherTree(tree otherTree) {
        this.otherTree = otherTree;
    }

    // Constructors, the second one is for dynamic creation of nodes,
    // the first is for the first nodes created
    public node(String data) {
        this.data = data;
    }

    public node(String data, node previous, tree tree, tree otherTree) {
        this.data = data;
        this.previous = previous;
        this.tree = tree;
        this.otherTree = otherTree;
    }

    public ArrayList<node> getChildren() {
        return children;
    }

    // If the child already exists in the list then dont add it,
    // If it doesnt, then add it and check the other tree
    public void addChild(node child) {
         if (!alreadyExists(child)) {
             children.add(child);
             if (checkOtherTree(child.otherTree)) {
                 // THIS IS OUR GOAL, NEED TO RETURN THE CONNECTIONS
                 System.out.println("Connection Found!");
             }
         }
         else
             System.out.println("Tried to add child that already existed.");
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

    // This checks the whole tree to see if the node already exists. Logic needs work I think.
    public boolean alreadyExists(node n){
        for(node each : n.getChildren()) {
            if (n.getData() == each.getData())
                return true;
            else
                alreadyExists(each);
        }
        return false;
    }

    public boolean connectionInOtherTree(node n){
        for(node each : n.getChildren()) {
            if (n.getData() == each.getData()) {

                return true;
            }
            else
                connectionInOtherTree(each);
        }
        return false;
    }

    public boolean checkOtherTree(tree tree) {
        if (connectionInOtherTree(tree.getRoot()))
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
