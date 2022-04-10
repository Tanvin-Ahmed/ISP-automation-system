import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationForm extends JFrame {
    private JTextField textName;
    private JTextField textEmail;
    private JPasswordField userPassword;
    private JPasswordField confirmPassword;
    private JButton registerButton;
    private JButton cancelButton;
    private JTextField textPhone;
    private JPanel registerPanel;
    private JButton haveAnAccountButton;

    Connection con;
    PreparedStatement pst;

    public void connect(){
        try{
            String url = "jdbc:mysql://localhost:4306/isp";
            String username = "root";
            String userPassword = "";
            con = DriverManager.getConnection(url, username, userPassword);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RegistrationForm(){
        setTitle("Register page");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(490, 474));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        connect();


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        haveAnAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            }
        });
    }

    public User user;
    private void registerUser() {
        String name = textName.getText();
        String email = textEmail.getText();
        String phone = textPhone.getText();
        String password = String.valueOf(userPassword.getPassword());
        String confPassword = String.valueOf(confirmPassword.getPassword());

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confPassword.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter all fields", "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!password.equals(confPassword)){
            JOptionPane.showMessageDialog(this, "Confirm Password does not match", "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDatabase(name, email, password, phone);
        if(user != null){
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Fail to create new user", "Try again", JOptionPane.ERROR_MESSAGE);
        }
    }

    private User addUserToDatabase(String name, String email, String password, String phone) {
        User user = null;
        try {
            Statement stmt = con.createStatement();
            pst = con.prepareStatement("insert into users(name, email, phone, password)values(?, ?, ?, ?)");
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, phone);
            pst.setString(4, password);

            int addRows = pst.executeUpdate();
            if(addRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.password = password;
            }

            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        RegistrationForm registerForm = new RegistrationForm();
        User user = registerForm.user;

        if(user != null){
            System.out.println("Successful registration of " + user.name);
        } else {
            System.out.println("Registration cancel");
        }
    }
}
