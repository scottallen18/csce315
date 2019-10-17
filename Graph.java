import java.util.*;

public class Graph
  {
    private GraphNode list[];

    Graph(int size)
    {
        list = new GraphNode[size];

      for(int i = 0; i < size; i++){
        list[i] = new GraphNode();
      }
    }

    public void addNode(int pos, GraphNode newNode)
    {
      list[pos] = newNode;
    }

    public GraphNode getNode(int pos)
    {
      return list[pos];
    }

    void DFSUtil(int pos, GraphNode v)
    {
      v.visited();
    
      GraphNode nextNode = list[pos].getChild(pos);
      int nextPos = 0;
      while(nextNode.ifVisited())
      {
        DFSUtil(nextPos, nextNode);
        nextPos++;
      }
    }

    void DFS(int pos, GraphNode v)
    {
      DFSUtil(pos, v);
    }
  }