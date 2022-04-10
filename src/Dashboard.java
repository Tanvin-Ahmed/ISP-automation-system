import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dashboard extends JFrame {
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton createPlanButton;
    private JButton employeeButton;
    private JButton customarButton;
    private JButton complaintButton;
    private JButton logoutButton;
    private JLabel showTime;
    private JLabel showDate;
    private JButton registerButton;


    public Dashboard() {
        setTitle("Dashboard");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(490, 474));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        date();
        time();

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            }
        });


        createPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                CreatePlan createPlan = new CreatePlan();
                createPlan.setVisible(true);
            }
        });


        employeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Employee employee = new Employee();
                employee.setVisible(true);
                setVisible(false);
            }
        });
        customarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Customar customar = new Customar();
                customar.setVisible(true);
            }
        });
        complaintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Complaint complaint = new Complaint();
                complaint.setVisible(true);
            }
        });
    }

    void date(){
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("YYYY-MM-dd");
        showDate.setText(s.format(d));
    }

    void time(){
        new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
                showTime.setText(s.format(d));
            }
        }).start();
    }

//    private boolean connectToDatabase() {
//        boolean hasRegisteredUser = false;
//
//        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost:4306/";
//        final String DB_URL = "jdbc:mysql://localhost:4306/isp";
//        final String USERNAME = "root";
//        final String PASSWORD = "";
//        try{
//            //First, connect to MYSQL server and create the database if not created
//            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
//            Statement statement = conn.createStatement();
//            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS isp");
//            statement.close();
//            conn.close();
//
//            //Second, connect to the database and create the table "users" if cot created
//            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
//            statement = conn.createStatement();
//            String sql = "CREATE TABLE IF NOT EXISTS users ("
//                    + "id INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
//                    + "name VARCHAR(200) NOT NULL,"
//                    + "email VARCHAR(200) NOT NULL UNIQUE,"
//                    + "phone VARCHAR(200),"
//                    + "password VARCHAR(200) NOT NULL"
//                    + ")";
//            statement.executeUpdate(sql);
//
//            //check if we have users in the table users
//            statement = conn.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
//
//            if (resultSet.next()) {
//                int numUsers = resultSet.getInt(1);
//                if (numUsers > 0) {
//                    hasRegisteredUser = true;
//                }
//            }
//
//            statement.close();
//            conn.close();
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return hasRegisteredUser;
//    }

    public static void main(String[] args) {
        Dashboard dashboard = new Dashboard();
    }
}
