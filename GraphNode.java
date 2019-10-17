import java.util.*;

public class GraphNode
{
    private String data;
    Vector<GraphNode> children;

    boolean visited;

    GraphNode()
    {
        this.data = "";
        this.visited = false;
    }
    GraphNode(String data)
    {
        this.data = data;
        this.visited = false;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String s)
    {
        this.data = s;
    }

    public void visited()
    {
        this.visited = true;
    }

    public boolean ifVisited()
    {
        return visited;
    }

    public void addChildren(GraphNode newNode)
    {
        children.add(newNode);
    }

    public GraphNode getChild(int pos)
    {
        return children.get(pos);
    }
}