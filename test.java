import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import java.sql.DriverManager;
import java.util.*;

public class test {
    public static class Node
  {
    private String data;
    Vector<Node> children;

    boolean visited;

    Node()
    {
      this.data = "";
      this.visited = false;
    }
    Node(String data)
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

    public void addChildren(Node newNode)
    {
      children.add(newNode);
    }

    public Node getChild(int pos)
    {
      return children.get(pos);
    }
  }

  public static class Graph
  {
    private Node list[];

    Graph(int size)
    {
      for(int i = 0; i < size; i++){
        list[i] = new Node();
      }
    }

    public void addNode(int pos, Node newNode)
    {
      list[pos] = newNode;
    }

    public Node getNode(int pos)
    {
      return list[pos];
    }

    void DFSUtil(int pos, Node v)
    {
      v.visited();
    
      Node nextNode = list[pos].getChild(pos);
      int nextPos = 0;
      while(nextNode.ifVisited())
      {
        DFSUtil(nextPos, nextNode);
        nextPos++;
      }
    }

    void DFS(int pos, Node v)
    {
      DFSUtil(pos, v);
    }
  }

  private static String sqlStatement = null;
	private static final JFrame frame = new JFrame("Database Queries");

	private static void printAnswer1(String first_name, String second_name) {
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
		GridBagLayout display = new GridBagLayout();
		frame.setLayout(display);
		System.out.println(first_name + " " + second_name);

	}
	private static void printAnswer2(int first_year, int second_year) {
		frame.getContentPane().removeAll();
		frame.getContentPane().repaint();
		GridBagLayout display = new GridBagLayout();
		frame.setLayout(display);
		System.out.println(first_year + " " + second_year);

	}

	private static final ActionListener choice1 = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.getContentPane().removeAll();
			frame.getContentPane().repaint();
			GridLayout grid = new GridLayout(1, 2);
			frame.setLayout(grid);
			JTextField answer= new JTextField();
			JLabel name1= new JLabel();
			name1.setText("First Cast Members Name: ");

			frame.add(name1);
			frame.add(answer);
			frame.setVisible(true);

			ActionListener name1_input = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//Here we get the name of the first person who we need to connect
					String first_name = answer.getText();
					frame.getContentPane().removeAll();
					frame.getContentPane().repaint();
					GridLayout grid = new GridLayout(1, 2);
					frame.setLayout(grid);
					JTextField answer2= new JTextField();
					JLabel name2= new JLabel();
					name2.setText("Find the connection between " + first_name + " and : ");
					frame.add(name2);
					frame.add(answer2);
					frame.setVisible(true);
					ActionListener name2_input = new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							//Here we get the name of the first person who we need to connect
							String second_name = answer2.getText();
							printAnswer1(first_name, second_name);
						}
					};
					answer2.addActionListener(name2_input);
				}
			};
			answer.addActionListener(name1_input);
		}
	};
	private static final ActionListener choice2 = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.getContentPane().removeAll();
			frame.getContentPane().repaint();
			GridLayout grid = new GridLayout(1, 2);
			frame.setLayout(grid);
			JTextField answer= new JTextField();
			JLabel name1= new JLabel();
			name1.setText("First Year: ");

			frame.add(name1);
			frame.add(answer);
			frame.setVisible(true);

			ActionListener name1_input = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//Here we get the name of the first person who we need to connect
					int first_year = Integer.parseInt(answer.getText());
					frame.getContentPane().removeAll();
					frame.getContentPane().repaint();
					GridLayout grid = new GridLayout(1, 2);
					frame.setLayout(grid);
					JTextField answer2= new JTextField();
					JLabel name2= new JLabel();
					name2.setText("Find the connection between " + first_year + " and : ");
					frame.add(name2);
					frame.add(answer2);
					frame.setVisible(true);
					ActionListener name2_input = new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							//Here we get the name of the first person who we need to connect
                            int second_year = Integer.parseInt(answer2.getText());
                            sqlStatement = "SELECT * FROM title_basics WHERE startyear BETWEEN " + first_year + " AND " + second_year;
							printAnswer2(first_year, second_year);
						}
					};
					answer2.addActionListener(name2_input);
				}
			};
			answer.addActionListener(name1_input);
		}
    };
    
    public static void Question2(Connection connect)
    {
        Graph g;
        int NodeCounter = 0;
        Vector<Node> initialNodes = new Vector<Node>();

        String startyear = "";
        Vector listOfTitleConst = new Vector();
        try {
            Statement stmt = connect.createStatement();

            System.out.println("Got your Query, collecting data!");
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) { // This will retrieve the titles within the years specified
                startyear = result.getString("tconst");
                listOfTitleConst.add(startyear);

                NodeCounter++;
                Node addNode = new Node(startyear);
                initialNodes.add(addNode);
            }     
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        g = new Graph(NodeCounter); // Initializes graph

        for(int i = 0; i < initialNodes.size(); i++)
        {
          g.addNode(i, initialNodes.get(i)); // Adds all the titles that are within the year range
        }


        // Now looking for the actors related to those titles previously searched
        Vector listOfActors = new Vector();

        System.out.println("Getting List of Actors");
        try
        {
          Statement stmt = connect.createStatement();

          boolean running = true;
          int index = 0;
          while(running)
          {
            sqlStatement = "SELECT * FROM name_basics WHERE titles LIKE '%" + listOfTitleConst.get(index) + "%'";

            ResultSet resultActors = stmt.executeQuery(sqlStatement);

            while(resultActors.next())
            {
              String Actors = resultActors.getString("primaryname");

              if(!(listOfActors.contains(Actors))){ // Checks for duplicates
                listOfActors.add(Actors);

                Node actorNode = new Node(Actors);
                g.getNode(index).addChildren(actorNode); // Adds the actors as children of the title we searched for
              }
            }

            if(index == listOfTitleConst.size() - 1)
              break;
            else
              index++;

          }
        }
        catch (Exception e)
        {
          JOptionPane.showMessageDialog(null, e);
        }


        // Now looking for the titles related to the actors previously searched
        Vector<Vector> ListOfActorsWithTitles = new Vector<Vector>(); // Purpose of 2D vector to store actors with corresponding titles.

        String Titles = "";

        System.out.println("Getting Titles of Actors");
        try
        {
          Statement stmt = connect.createStatement();

          boolean running = true;
          int index = 0;
          while(running)
          {
            sqlStatement = "SELECT * FROM name_basics WHERE primaryname = '" + listOfActors.get(index) +"'";

            //ListOfActorsWithTitles.add(listofActors.get(index));
            System.out.println("Getting Titles for Actor: " + listOfActors.get(index));

            ResultSet resultTitles = stmt.executeQuery(sqlStatement);

            while(resultTitles.next())
            {
              Vector listOfTitles = new Vector();

              Titles = resultTitles.getString("titles");

              Scanner s = new Scanner(Titles).useDelimiter(",|\r\n");

              int childIndex = 0;
              while(s.hasNext())
              {
                listOfTitles.add(s.next()); // retrieves titles for current actor

                Node TitlesFromActors = new Node(s.next());
                g.getNode(index).getChild(childIndex).addChildren(TitlesFromActors); // Adds the titles as children of the current actor

                childIndex++;
              }

              s.close();
              
              System.out.println("The titles: " + listOfTitles);

              ListOfActorsWithTitles.add(listOfTitles);
              //listOfTitles.clear();
            }

            if(index == listOfActors.size() - 1)
              break;
            else
              index++;

          }
        }
        catch (Exception e)
        {
          JOptionPane.showMessageDialog(null, e);
        }

        System.out.println("Size of List of Actors with Titles: " + ListOfActorsWithTitles.size());

        //Now looking for the startYear of the titles that were derived from the searched actors
        Vector<Vector> listOfAcceptableActors = new Vector<Vector>();
        Vector listOfTitlesInRange = new Vector<>();

        System.out.println("Getting title years");
        try
        {
          Statement stmt = connect.createStatement();

          boolean running = true;
          int rootIndex = 0;
          while(running)
          {
            for(int actorsIndex = 0; actorsIndex < ListOfActorsWithTitles.size(); actorsIndex++)
            {
              for(int titlesIndex = 0; titlesIndex < ListOfActorsWithTitles.get(actorsIndex).size(); titlesIndex++)
              {
                //int counter = 0;
                int yearStarted = 1880;
                int yearEnded = 1890;

                Vector listOfTitlesFromActor = ListOfActorsWithTitles.get(actorsIndex); // This is the list of titles from the current actor
                sqlStatement = "SELECT * FROM title_basics WHERE tconst = '" + listOfTitlesFromActor.get(titlesIndex) + "'"; // This is searching for the current title

                System.out.println("Finding startYear for actor: " + listOfActors.get(actorsIndex));
                System.out.println("Searching: " + listOfTitlesFromActor.get(titlesIndex));

                ResultSet resultTitles = stmt.executeQuery(sqlStatement);

                while(resultTitles.next())
                {
                  String startYear = resultTitles.getString("startyear");
                  String title = resultTitles.getString("primaryTitle"); // This gets the title that is related to the actor

                  System.out.println(startYear);

                  if(Integer.parseInt(startYear) >= yearStarted && Integer.parseInt(startYear) <= yearEnded)
                  {
                    listOfTitlesInRange.add(title);

                    Node year = new Node(startYear);
                    g.getNode(rootIndex).getChild(actorsIndex).getChild(titlesIndex).addChildren(year); // Adds the year as children of the title we just searched... This may not be necessary
                  }
                }

                listOfAcceptableActors.add(listOfTitlesInRange);
              }
            }
            break;
          }
        }
        catch (Exception e)
        {
          JOptionPane.showMessageDialog(null, e);
        }


        //For now... will not bother testing searching for the shortest list
        StringBuilder finalResult = new StringBuilder();
        for(int i = 0; i < listOfAcceptableActors.size(); i++)
        {
          for(int j = 0; j < listOfAcceptableActors.get(i).size(); j++)
          {
            finalResult.append(listOfAcceptableActors.get(i) + ": " + listOfAcceptableActors.get(i).get(j));
          }
          finalResult.append("\n");
        }

        JOptionPane.showMessageDialog(null, finalResult); //displays the result
    }

	public static void main(String[] args) {
        //Building the connection
        Connection conn = null;
        String username = "brian.lu32_605";
        String password = "studentpwd";
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu/brian.lu32_605",
                    username, password);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }//end try catch
        JOptionPane.showMessageDialog(null, "Opened database successfully");

		JButton question1,question2;
		question1 = new JButton("Find the closest connection between 2 cast members");
		question2 = new JButton("Find the Least ammount of cast members who have worked every year between two years");
		frame.add(question1);
		frame.add(question2);
		frame.setLayout(new GridLayout(2,1));
		question1.addActionListener(choice1);
		question2.addActionListener(choice2);
		frame.setLocation(1000, 1000);
		frame.setSize(800,300);
        frame.setVisible(true);
        
        Question2(conn);
	}
}