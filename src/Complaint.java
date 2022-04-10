import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Complaint extends JFrame {
    private JPanel complaintPanel;
    private JTextField textId;
    private JTextField textDate;
    private JTable table1;
    private JButton addComplaintButton;
    private JButton solvedButton;
    private JScrollPane complaintTable;
    private JTextArea textAreaComplaint;
    private JButton buttonBackToDashboard;


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
            pst = con.prepareStatement("select * from complaint");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Complaint(){
        connect();
        tableLoad();
        setTitle("Complaint panel");
        setContentPane(complaintPanel);
        setMinimumSize(new Dimension(490, 474));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setVisible(true);


        buttonBackToDashboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
            }
        });


        addComplaintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String complaint = textAreaComplaint.getText();
                String date = textDate.getText();

                if(complaint.isEmpty() || date.isEmpty()){
                    JOptionPane.showMessageDialog(Complaint.this, "Please full fill all input filed. It is not necessary to write id, because id will auto generate", "Try again", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    try{
                        pst = con.prepareStatement("insert into complaint(complaint, date, action)values(?,?,?)");
                        pst.setString(1, complaint);
                        pst.setString(2, date);
                        pst.setString(3, "unsolved");
                        pst.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Record Added!!!");
                        tableLoad();
                        textId.setText("");
                        textAreaComplaint.setText("");
                        textDate.setText("");

                        textAreaComplaint.requestFocus();
                    } catch (SQLException e1){
                        e1.printStackTrace();
                    }
                }
            }
        });


        solvedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String complaintId = textId.getText();
                if(complaintId.isEmpty()){
                    JOptionPane.showMessageDialog(Complaint.this, "Please select any complaint from table first!", "Try again", JOptionPane.ERROR_MESSAGE);
                } else {
                    try{
                        pst = con.prepareStatement("update complaint set action = ? where id = ?");
                        pst.setString(1, "solved");
                        pst.setString(2, complaintId);
                        pst.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Record updated!!!");
                        tableLoad();
                        textId.setText("");
                        textAreaComplaint.setText("");
                        textDate.setText("");

                        textAreaComplaint.requestFocus();
                    } catch (SQLException e1){
                        e1.printStackTrace();
                    }
                }
            }
        });

        complaintTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel Df = (DefaultTableModel) table1.getModel();
                int selectedIndex = table1.getSelectedRow();
                textId.setText(Df.getValueAt(selectedIndex, 0).toString());
                textAreaComplaint.setText(Df.getValueAt(selectedIndex, 1).toString());
                textDate.setText(Df.getValueAt(selectedIndex, 2).toString());
            }
        });
    }

    public static void main(String[] args) {
        Complaint complaint = new Complaint();
    }
}
