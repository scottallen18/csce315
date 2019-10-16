public class tree {
    private node root;

    public boolean isConnectionFound() {
        return connectionFound;
    }

    public void setConnectionFound(boolean connectionFound) {
        this.connectionFound = connectionFound;
    }

    private boolean connectionFound = false;

    public tree(node root) {
        this.root = root;
    }

    public node getRoot() {
        return root;
    }
}
