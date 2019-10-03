import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GUI {

    public static void main(String[] args){
        MyFrame f = new MyFrame();
        f.setVisible(true);
    }
}

class MyFrame extends JFrame {

    private JButton search  = new JButton("Search");

    private JTextField searchField = new JTextField();

    private JLabel searchLbl = new JLabel("Search :");

    public MyFrame(){
        setTitle("Penjumlahan");
        setSize(800,400);
        setLocation(new Point(300,200));
        setLayout(null);
        setResizable(false);

        initComponent();
        initEvent();
    }

    private void initComponent(){
        search.setBounds(300,130, 80,25);

        searchField.setBounds(100,10,100,20);

        searchLbl.setBounds(20,10,100,20);


        add(search);
        add(searchLbl);
        add(searchField);
    }

    private void initEvent(){

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(1);
            }
        });

        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnSearchClick(e);
            }
        });
    }


    private void btnSearchClick(ActionEvent evt){
        try{
            String x = search.getText();

        }catch(Exception e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null,
                    e.toString(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}