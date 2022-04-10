import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Employee extends JFrame {
    private JTextField textId;
    private JTextField textJoinDate;
    private JTextField textAddress;
    private JTextField textMobile;
    private JTextField textName;
    private JButton DELETEButton;
    private JButton ADDButton;
    private JButton UPDATEButton;
    private JTable table1;
    private JPanel employeePanel;
    private JScrollPane employeeTable;
    private JButton backToDashboardButton;
    private JTextField textSearch;
    private JButton searchEmployeeButton;

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

    void tableLoad() {
        try {
            pst = con.prepareStatement("select * from employee");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee() {
        connect();
        tableLoad();
        setTitle("Employee panel");
        setContentPane(employeePanel);
        setMinimumSize(new Dimension(490, 474));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setVisible(true);

        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeName = textName.getText();
                String mobile = textMobile.getText();
                String address = textAddress.getText();
                String joinDate = textJoinDate.getText();

                if(employeeName.isEmpty() || mobile.isEmpty() || address.isEmpty() || joinDate.isEmpty()){
                    JOptionPane.showMessageDialog(Employee.this, "Please full fill all input filed. It is not necessary to write id, because id will auto generate", "Try again", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    try{
                        pst = con.prepareStatement("insert into employee(name, mobile, address, joinDate)values(?,?,?,?)");
                        pst.setString(1, employeeName);
                        pst.setString(2, mobile);
                        pst.setString(3, address);
                        pst.setString(4, joinDate);
                        pst.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Record Added!!!");
                        tableLoad();
                        textId.setText("");
                        textName.setText("");
                        textMobile.setText("");
                        textAddress.setText("");
                        textJoinDate.setText("");

                        textName.requestFocus();
                    } catch (SQLException e1){
                        e1.printStackTrace();
                    }
                }
            }
        });


        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel Df = (DefaultTableModel) table1.getModel();
                int selectedIndex = table1.getSelectedRow();
                textId.setText(Df.getValueAt(selectedIndex, 0).toString());
                textName.setText(Df.getValueAt(selectedIndex, 1).toString());
                textMobile.setText(Df.getValueAt(selectedIndex, 2).toString());
                textAddress.setText(Df.getValueAt(selectedIndex, 3).toString());
                textJoinDate.setText(Df.getValueAt(selectedIndex, 4).toString());
            }
        });


        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeId = textId.getText();
                String employeeName = textName.getText();
                String mobile = textMobile.getText();
                String address = textAddress.getText();
                String joinDate = textJoinDate.getText();

                try{
                    pst = con.prepareStatement("update employee set name = ?, mobile = ?, address = ?, joinDate = ? where id = ?");
                    pst.setString(1, employeeName);
                    pst.setString(2, mobile);
                    pst.setString(3, address);
                    pst.setString(4, joinDate);
                    pst.setString(5, employeeId);
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Record updated!!!");
                    tableLoad();
                    textId.setText("");
                    textName.setText("");
                    textMobile.setText("");
                    textAddress.setText("");
                    textJoinDate.setText("");

                    textName.requestFocus();
                } catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
        });


        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeId = textId.getText();

                try {
                    pst = con.prepareStatement("delete from employee where id = ?");
                    pst.setString(1, employeeId);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record deleted successfully!");
                    tableLoad();
                    textId.setText("");
                    textName.setText("");
                    textMobile.setText("");
                    textAddress.setText("");
                    textJoinDate.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        backToDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
            }
        });

        searchEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String empId = textSearch.getText();
                    pst = con.prepareStatement("select name, mobile, address, joinDate from employee where id=?");
                    pst.setString(1, empId);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next() == true){
                        String empName = rs.getString(1);
                        String mobile = rs.getString(2);
                        String address = rs.getString(3);
                        String joinDate = rs.getString(4);

                        textId.setText(empId);
                        textName.setText(empName);
                        textMobile.setText(mobile);
                        textAddress.setText(address);
                        textJoinDate.setText(joinDate);
                    } else {
                        textId.setText("");
                        textName.setText("");
                        textAddress.setText("");
                        textMobile.setText("");
                        textJoinDate.setText("");
                        JOptionPane.showMessageDialog(null, "Invalid Employee Number!");
                    }
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        Employee employee = new Employee();
    }
}
