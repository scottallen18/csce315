import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import java.sql.DriverManager;
import java.util.*;

public class test {

  private static String sqlStatement = null;
  private static int first_year;
  private static int second_year;
  private static final JFrame frame = new JFrame("Database Queries");
  private static Connection conn = null;
  private static String username = "brian.lu32_605";
  private static String password = "studentpwd";
  private static Queue<node> qChildren = new LinkedList<>();
  private static Connection globalConnection;

  public static void findConnection(String s1, String s2){
    node node1 = new node(s1);
    node node2 = new node(s2);
    tree tree1 = new tree(node1);
    tree tree2 = new tree(node2);
    node1.setTree(tree1);
    node2.setTree(tree2);
    node1.setOtherTree(tree2);
    node2.setOtherTree(tree1);
    node1.setHeight(0);
    node2.setHeight(0);

    try {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu/brian.lu32_605",
                username, password);
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
    }//end try catch
    JOptionPane.showMessageDialog(null, "Opened database successfully");
      
    System.out.println("Starting the search!");	

    findRecursion(node1, node1.getHeight());
    findRecursion(node2, node2.getHeight());
    qChildren.add(node1);
    qChildren.add(node2);

    while(true){
        node temp = qChildren.peek();
        findRecursion(qChildren.remove(), temp.getHeight());
        qChildren.addAll(temp.getChildren());
        for (int i = 0; i < temp.getChildren().size(); i++) {
            if (temp.getTree().isConnectionFound())
                break;
        }
        if (temp.getTree().isConnectionFound())
            break;
    }
  }

  public static void findRecursion(node node, int heightOfTree){
    if (node.getTree().isConnectionFound())
        return;
    ArrayList<String> rawChildren = new ArrayList<String>();
    ArrayList<String> children = new ArrayList<String>();
    if (heightOfTree % 2 == 0) {
      try {
        sqlStatement = "SELECT titles FROM name_basics WHERE primaryName = '" + node.getData() + "';";

        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(sqlStatement);
        //Add the titles into the array list
        while (result.next()) {
            rawChildren.add(result.getString("titles"));
        }
      } catch (Exception e) {
          e.printStackTrace();
      }

      String temp = ",";
      Character comma = temp.charAt(0);

      for (String s : rawChildren) {
        if (s == null){
        }
        else {
          ArrayList<Character> c = new ArrayList<Character>();
          for (int i = 0; i < s.length(); i++)
            c.add(s.charAt(i));
          StringBuilder b = new StringBuilder();
          for (int j = 0; j < c.size(); j++) {
            if (c.get(j) != comma && j != c.size()-1) {
              b.append(c.get(j));
            } else if (j == c.size()-1){
              b.append(c.get(j));
              children.add(b.toString());
            }
            else {
              children.add(b.toString());
              b.delete(0,b.length());
            }
          }
        }
      }
    }
    else {
      try {
        sqlStatement = "SELECT primaryName FROM name_basics WHERE titles like '%" + node.getData() + "%';";

        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(sqlStatement);

        //Add the titles into the array list
        while (result.next()) {
          children.add(result.getString("primaryName"));
        }
      } catch (Exception e) {
          e.printStackTrace();
      }
    }

    for (String s : children) {
        node.addChild(s);
        if (node.getTree().isConnectionFound())
            break;
    }
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
							//printAnswer1(first_name, second_name);
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
					first_year = Integer.parseInt(answer.getText());
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
              second_year = Integer.parseInt(answer2.getText());
              sqlStatement = "SELECT * FROM title_basics WHERE startyear BETWEEN " + first_year + " AND " + second_year + " limit 25";
              printAnswer2(first_year, second_year);
              Question2(globalConnection, sqlStatement);
						}
					};
					answer2.addActionListener(name2_input);
				}
			};
			answer.addActionListener(name1_input);
    } 
  };
    
  public static void Question2(Connection connect, String statement)
  {
    String newStatement = statement;
    System.out.println("Starting Question 2");
    Graph g;
    int NodeCounter = 0;
    Vector<GraphNode> initialNodes = new Vector<GraphNode>();

    String startyear = "";
    Vector listOfTitleConst = new Vector();

    System.out.println(connect + statement);
    try 
    {
      Statement stmt = connect.createStatement();

      System.out.println("Got your Query, collecting data!");
      ResultSet result = stmt.executeQuery(newStatement);

      while (result.next()) { // This will retrieve the titles within the years specified
        String tconstTitle = result.getString("tconst");
        // String startyear = result.getString("startyear");

        // if(Integer.parseInt(startyear) > 1890) // Since the database is in ASC order, the moment the year passes the 2nd year, stop searching
        //   break;

        listOfTitleConst.add(tconstTitle);

        //System.out.println(tconstTitle);

        NodeCounter++;
        GraphNode addNode = new GraphNode(tconstTitle);
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
        newStatement = "SELECT * FROM name_basics WHERE titles LIKE '%" + listOfTitleConst.get(index) + "%' limit 25";

        ResultSet resultActors = stmt.executeQuery(newStatement);

        while(resultActors.next())
        {
          String Actors = resultActors.getString("primaryname");

          if(!(listOfActors.contains(Actors))){ // Checks for duplicates
            listOfActors.add(Actors);

            //System.out.println(Actors);

            //System.out.println("Inserting into Node");
            GraphNode actorNode = new GraphNode(Actors);
            //g.getNode(index).addChildren(actorNode); // Adds the actors as children of the title we searched for
            //System.out.println("Working");
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

    System.out.println("Getting Titles of Actors");
    try
    {
      Statement stmt = connect.createStatement();

      boolean running = true;
      int index = 0;
      while(running)
      {
        newStatement = "SELECT * FROM name_basics WHERE primaryname = '" + listOfActors.get(index) +"'";

        System.out.println("Getting Titles for Actor: " + listOfActors.get(index));
        ResultSet resultTitles = stmt.executeQuery(newStatement);

        Vector listOfTitles = new Vector();
        Vector listOfTitlesToPass = new Vector();
        while(resultTitles.next())
        {
          String Titles = resultTitles.getString("titles");
          String Actor = resultTitles.getString("primaryname");

          if(Titles != "" && Titles != null){ // In case the title that was read happens to be empty
            Scanner s = new Scanner(Titles).useDelimiter(",|\r\n");

            int childIndex = 0;
            while(s.hasNext())
            {
              listOfTitles.add(s.next()); // retrieves titles for current actor

              //GraphNode TitlesFromActors = new GraphNode(s.next());
              //g.getNode(index).getChild(childIndex).addChildren(TitlesFromActors); // Adds the titles as children of the current actor

              childIndex++;
            }
            s.close();
          }
        }

        //System.out.println(listOfTitles);
        
        for(int i = 0; i < listOfTitles.size(); i++) // deep copy
        {
          listOfTitlesToPass.add(listOfTitles.get(i));
        }

        ListOfActorsWithTitles.add(listOfTitlesToPass);
        
        listOfTitles.clear();

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

    //System.out.println("Size of List of Actors with Titles: " + ListOfActorsWithTitles.size());

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
            newStatement = "SELECT * FROM title_basics WHERE tconst = '" + listOfTitlesFromActor.get(titlesIndex) + "'"; // This is searching for the current title

            //System.out.println("Finding startYear for actor: " + listOfActors.get(actorsIndex));
            //System.out.println("Searching: " + listOfTitlesFromActor.get(titlesIndex));

            ResultSet resultTitles = stmt.executeQuery(newStatement);

            while(resultTitles.next())
            {
              String startYear = resultTitles.getString("startyear");
              String title = resultTitles.getString("primaryTitle"); // This gets the title that is related to the actor

              if(Integer.parseInt(startYear) >= yearStarted && Integer.parseInt(startYear) <= yearEnded)
              {
                listOfTitlesInRange.add(title);

                GraphNode year = new GraphNode(startYear);
                //g.getNode(rootIndex).getChild(actorsIndex).getChild(titlesIndex).addChildren(year); // Adds the year as children of the title we just searched... This may not be necessary
              }
            }
          }
          //System.out.println("Actor: " + listOfActors.get(actorsIndex) + "-> Titles in Range: " + listOfTitlesInRange);

          Vector deepCopy = new Vector<>();
          for(int i = 0; i < listOfTitlesInRange.size(); i++)
          {
            deepCopy.add(listOfTitlesInRange.get(i));
          }

          listOfAcceptableActors.add(deepCopy);
          listOfTitlesInRange.clear();
        }
        break;
      }
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(null, e);
    }


    // Retrieves smallest list
    int smallestSize = listOfAcceptableActors.get(0).size();
    int actorPos = 0;
    for(int i = 0; i < listOfAcceptableActors.size(); i++)
    {
      for(int j = 0; j < listOfAcceptableActors.get(i).size(); j++)
      {
        if(listOfAcceptableActors.get(i).size() < smallestSize)
        {
          smallestSize = listOfAcceptableActors.get(i).size();
          actorPos = i;
        }
      }
    }

    String finalAnswer = "The actor with the shortest list of movies released between 1880 and 1890: " + listOfActors.get(actorPos) + " -> " + listOfAcceptableActors.get(actorPos);

    JOptionPane.showMessageDialog(null, finalAnswer); //displays the result
    try
    {
      globalConnection.close();
      JOptionPane.showMessageDialog(null, "Connection Closed");
      System.exit(0);
    }
    catch(Exception e)
    {
      JOptionPane.showMessageDialog(null, "Connection NOT Closed.");
      System.exit(1);
    }
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

    globalConnection = conn;

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
	}
}
