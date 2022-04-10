import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class CreatePlan extends JFrame {
    private JPanel createPlanPanel;
    private JTextField textId;
    private JTextField textPlanName;
    private JTextField textSpeed;
    private JTextField textCost;
    private JTextField textDuration;
    private JButton updateButton;
    private JButton addButton;
    private JTable table1;
    private JButton button1;
    private JButton deleteButton;
    private JScrollPane dataTable;

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
            pst = con.prepareStatement("select * from internetplan");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CreatePlan(){
        connect();
        tableLoad();
        setTitle("Create plan");
        setContentPane(createPlanPanel);
        setMinimumSize(new Dimension(490, 474));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setVisible(true);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String planName = textPlanName.getText();
                String speed = textSpeed.getText();
                String cost = textCost.getText();
                String duration = textDuration.getText();

                if(planName.isEmpty() || speed.isEmpty() || cost.isEmpty() || duration.isEmpty()){
                    JOptionPane.showMessageDialog(CreatePlan.this, "Please full fill all input filed. It is not necessary to write id, because id will auto generate", "Try again", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    try{
                        pst = con.prepareStatement("insert into internetplan(planName, speed, cost, duration)values(?,?,?,?)");
                        pst.setString(1, planName);
                        pst.setString(2, speed);
                        pst.setString(3, cost);
                        pst.setString(4, duration);
                        pst.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Record Added!!!");
                        tableLoad();
                        textId.setText("");
                        textPlanName.setText("");
                        textSpeed.setText("");
                        textCost.setText("");
                        textDuration.setText("");

                        textPlanName.requestFocus();
                    } catch (SQLException e1){
                        e1.printStackTrace();
                    }
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String planId = textId.getText();
                String planName = textPlanName.getText();
                String speed = textSpeed.getText();
                String cost = textCost.getText();
                String duration = textDuration.getText();

                try{
                    pst = con.prepareStatement("update internetplan set planName = ?, speed = ?, cost = ?, duration = ? where id = ?");
                    pst.setString(1, planName);
                    pst.setString(2, speed);
                    pst.setString(3, cost);
                    pst.setString(4, duration);
                    pst.setString(5, planId);
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Record updated!!!");
                    tableLoad();
                    textId.setText("");
                    textPlanName.setText("");
                    textSpeed.setText("");
                    textCost.setText("");
                    textDuration.setText("");

                    textPlanName.requestFocus();
                } catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String planId = textId.getText();

                try {
                    pst = con.prepareStatement("delete from internetplan where id = ?");
                    pst.setString(1, planId);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record deleted successfully!");
                    tableLoad();
                    textId.setText("");
                    textPlanName.setText("");
                    textCost.setText("");
                    textSpeed.setText("");
                    textDuration.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        dataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel Df = (DefaultTableModel) table1.getModel();
                int selectedIndex = table1.getSelectedRow();
                textId.setText(Df.getValueAt(selectedIndex, 0).toString());
                textPlanName.setText(Df.getValueAt(selectedIndex, 1).toString());
                textSpeed.setText(Df.getValueAt(selectedIndex, 2).toString());
                textCost.setText(Df.getValueAt(selectedIndex, 3).toString());
                textDuration.setText(Df.getValueAt(selectedIndex, 4).toString());
            }
        });
    }

    public static void main(String[] args) {
        CreatePlan createPlan = new CreatePlan();
    }
}
