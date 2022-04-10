import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField textEmail;
    private JPasswordField userPassword;
    private JButton loginButton;
    private JButton cancelButton;
    private JPanel loginPanel;
    private JButton reginsterNewAdminButton;

    public User user;

    Connection con;
    PreparedStatement pst;

    public void connect(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:4306/isp", "root", "");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LoginForm(){
        setTitle("Login page");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(490, 474));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        connect();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textEmail.getText();
                String pass = String.valueOf(userPassword.getPassword());

                user = getAuthenticatedUser(email, pass);

                if(user != null){
                    setVisible(false);
                    Dashboard dashboard = new Dashboard();
                    dashboard.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Email or password invalid", "Try again", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        setVisible(true);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                textEmail.setText("");
                userPassword.setText("");
            }
        });

        reginsterNewAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                RegistrationForm registrationForm = new RegistrationForm();
                registrationForm.setVisible(true);
            }
        });
    }

    private User getAuthenticatedUser(String email, String password) {
        User user = null;
        try {
            Statement stmt = con.createStatement();
            pst = con.prepareStatement("select * from users where email = ? and password = ?");
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                user = new User();
                user.name = rs.getString("name");
                user.email = rs.getString("email");
                user.phone = rs.getString("phone");
                user.password = rs.getString("password");
            }

            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return user;
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
        User user = loginForm.user;

        if(user != null){
            System.out.println("User name: " + user.name + " User email: " + user.email + " user phone: " + user.password);
        } else {
            System.out.println("Authentication cancel");
        }
    }
}
