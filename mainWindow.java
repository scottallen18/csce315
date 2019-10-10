import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import java.sql.DriverManager;

public class mainWindow {
    private static String sqlStatement = null;
    private static final JFrame frame = new JFrame("Database Queries");
    private static final JPanel panel = new JPanel();
    private static final JLabel lbl = new JLabel("Select one of the possible choices and click OK");
    private static final String[] choices = { "name_basics", "title_basics" };
    private static String[] choices2 = {"temp", "temp", "temp", "temp"};
    private static final JComboBox<String> cb = new JComboBox<String>(choices);
    private static JComboBox<String> cb2 = new JComboBox<String>(choices2);
    private static final JButton btn = new JButton("OK");
    private static final JTextField search = new JTextField();
    private static boolean finishedGUI = false;

    private static final ActionListener AL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cb.getSelectedItem() == "name_basics") {
                System.out.println("Name basics is selected.");
                choices2[0] = "primaryName";
                choices2[1] = "birthYear";
                choices2[2] = "deathYear";
                choices2[3] = "Profession";
            } else {
                System.out.println("Title basics is selected.");
                choices2[0] = "primaryTitle";
                choices2[1] = "originalTitle";
                choices2[2] = "startYear";
                choices2[3] = "genres";
            }
            cb2 = new JComboBox<String>(choices2);
            cb2.setMaximumSize(cb2.getPreferredSize());
            cb2.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(cb2);

            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(btn);
            btn.removeActionListener(AL);
            btn.addActionListener(AL2);
            frame.setVisible(true);
        }
    };
    private static final ActionListener AL2 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            search.setAlignmentX(Component.CENTER_ALIGNMENT);
            search.setSize(panel.getWidth(), 100);
            panel.add(search);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(btn);
            btn.removeActionListener(AL2);
            btn.addActionListener(AL3);
            frame.setVisible(true);
        }
    };
    private static final ActionListener AL3 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cb.getSelectedItem() == "name_basics") {
                sqlStatement = "SELECT primaryName FROM " + cb.getSelectedItem() + " WHERE " + cb2.getSelectedItem() + " = '" + search.getText() + "'";
            }
            else {
                sqlStatement = "SELECT primaryTitle FROM " + cb.getSelectedItem() + " WHERE " + cb2.getSelectedItem() + " = '" + search.getText() + "'";
            }
            finishedGUI = true;
            frame.setVisible(false);
        }
    };

    public static void main(String[] args) {
        //Building the connection
        Connection conn = null;
        String username = "brian.lu32_605";
        String password = "studentpwd";
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
        StringBuilder cus_lname = new StringBuilder();
        try {
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            //Add GUI here to figure out what statement the user wants from us

            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setLocation(430, 100);

            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            frame.add(panel);

            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(lbl);

            cb.setMaximumSize(cb.getPreferredSize());
            cb.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(cb);

            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(btn);

            frame.setVisible(true);
            btn.addActionListener(AL);
            while (true) {
                if (finishedGUI)
                    break;
                else
                    Thread.sleep(1000);
            }

            //send statement to DBMS
            System.out.println("Got your Query, collecting data!");
            ResultSet result = stmt.executeQuery(sqlStatement);

            //OUTPUT
            while (result.next()) {
                //System.out.println(result.getString("cus_lname"));
                cus_lname.append(result.getString("primaryName")).append("\n");
                // Do this so the list doesnt get too big
                if (cus_lname.length() >= 1000) {
                    cus_lname.append("These are the first chunk of responses");
                    JOptionPane.showMessageDialog(null, cus_lname.toString());
                    // Add a diaglog box that aks if the user wants more responses
                    // If yes, clear the list and repopulate it with more responses, if no, then break
                    cus_lname.delete(0,cus_lname.length());
                    //break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error accessing Database.");
        }
        JOptionPane.showMessageDialog(null, cus_lname.toString());
        //closing the connection
        try {
            conn.close();
            JOptionPane.showMessageDialog(null, "Connection Closed.");
            System.exit(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection NOT Closed.");
            System.exit(1);
        }
    }
    
    public void findConnection(String s1, String s2){
        node node1 = new node(s1);
        node node2 = new node(s2);
        tree tree1 = new tree(node1);
        tree tree2 = new tree(node2);
        node1.setTree(tree1);
        node2.setTree(tree2);

        /*SQL logic???????????? send halp.
        What do i need?
            - An SQL statement that I can input a name and it gives me the list of titles they were involved in
            - An SQL statement that lets me search name_basics for title t###### numbers
        */

        /* tree logic
        How do I go through the tree from Root -> children -> ... -> connection -> previous -> ... -> Root
         */

        /* ALSO! just need ideas on how to make this smaller, because it's bound to be GIANTIC right now
         */
    }
}
