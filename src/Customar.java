import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Customar extends JFrame {
    private JTextField textId;
    private JTextField textName;
    private JTextField textMobile;
    private JTextField textAddress;
    private JTextField textPlan;
    private JComboBox comboBoxGender;
    private JPanel customerPanel;
    private JTable table1;
    private JScrollPane planTable;
    private JComboBox comboBoxPurpose;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton buttonBackToDashboard;
    private JTable table2;
    private JScrollPane customerTable;

    Connection con;
    PreparedStatement pst;

    public void connect(){
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:4306/isp", "root", "");
            System.out.println("Database connect successfully!");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void customerTableLoad() {
        try {
            pst = con.prepareStatement("select * from customer");
            ResultSet rs = pst.executeQuery();
            table2.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void planTableLoad() {
        try {
            pst = con.prepareStatement("select * from internetplan");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String gender = "Male";
    String purpose = "Business";

    public Customar() {
        connect();
        customerTableLoad();
        planTableLoad();
        setTitle("Customer panel");
        setContentPane(customerPanel);
        setMinimumSize(new Dimension(490, 474));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setVisible(true);

        comboBoxGender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gender = (String) comboBoxGender.getSelectedItem();
            }
        });
        comboBoxPurpose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purpose = (String) comboBoxPurpose.getSelectedItem();
            }
        });

        planTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                DefaultTableModel Df = (DefaultTableModel) table1.getModel();
                int selectedIndex = table1.getSelectedRow();
                textPlan.setText(Df.getValueAt(selectedIndex, 1).toString());
            }
        });


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerName = textName.getText();
                String mobile = textMobile.getText();
                String address = textAddress.getText();
                String plan = textPlan.getText();

                if(customerName.isEmpty() || mobile.isEmpty() || address.isEmpty() || plan.isEmpty()){
                    JOptionPane.showMessageDialog(Customar.this, "Please full fill all input filed. It is not necessary to write id, because id will auto generate", "Try again", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    try{
                        pst = con.prepareStatement("insert into customer(name, mobile, gender, purpose, address, plan)values(?,?,?,?,?,?)");
                        pst.setString(1, customerName);
                        pst.setString(2, mobile);
                        pst.setString(3, gender);
                        pst.setString(4, purpose);
                        pst.setString(5, address);
                        pst.setString(6, plan);
                        pst.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Record Added!!!");
                        customerTableLoad();
                        textId.setText("");
                        textName.setText("");
                        textMobile.setText("");
                        textAddress.setText("");
                        textPlan.setText("");

                        textName.requestFocus();
                    } catch (SQLException e1){
                        e1.printStackTrace();
                    }
                }
            }
        });


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerId = textId.getText();
                String customerName = textName.getText();
                String mobile = textMobile.getText();
                String address = textAddress.getText();
                String plan = textPlan.getText();

                try{
                    pst = con.prepareStatement("update customer set name = ?, mobile = ?, gender=?, purpose=?, address = ?, plan = ? where id = ?");
                    pst.setString(1, customerName);
                    pst.setString(2, mobile);
                    pst.setString(3, gender);
                    pst.setString(4, purpose);
                    pst.setString(5, address);
                    pst.setString(6, plan);
                    pst.setString(7, customerId);
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Record updated!!!");
                    customerTableLoad();
                    textId.setText("");
                    textName.setText("");
                    textMobile.setText("");
                    textAddress.setText("");
                    textPlan.setText("");

                    textName.requestFocus();
                } catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerId = textId.getText();

                try {
                    pst = con.prepareStatement("delete from customer where id = ?");
                    pst.setString(1, customerId);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record deleted successfully!");
                    customerTableLoad();
                    textId.setText("");
                    textName.setText("");
                    textMobile.setText("");
                    textAddress.setText("");
                    textPlan.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                DefaultTableModel Df = (DefaultTableModel) table2.getModel();
                int selectedIndex = table2.getSelectedRow();
                textId.setText(Df.getValueAt(selectedIndex, 0).toString());
                textName.setText(Df.getValueAt(selectedIndex, 1).toString());
                textMobile.setText(Df.getValueAt(selectedIndex, 2).toString());
                textAddress.setText(Df.getValueAt(selectedIndex, 5).toString());
                textPlan.setText(Df.getValueAt(selectedIndex, 6).toString());

                DefaultComboBoxModel Dc = (DefaultComboBoxModel) comboBoxGender.getModel();
                Dc.setSelectedItem(Df.getValueAt(selectedIndex, 3).toString());

                DefaultComboBoxModel Dp = (DefaultComboBoxModel) comboBoxPurpose.getModel();
                Dp.setSelectedItem(Df.getValueAt(selectedIndex, 4).toString());

            }
        });


        buttonBackToDashboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        Customar customar = new Customar();
    }
}
