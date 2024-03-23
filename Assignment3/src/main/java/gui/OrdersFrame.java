package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dataAccessLayer.connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Represents a GUI frame for displaying previous orders.
 */
public class OrdersFrame extends ConnectionFactory {

    protected Connection connection = getConnection();
    protected JFrame orderFrame;
    protected JPanel containerPanel;
    protected JTextArea ordersOutput;
    protected JScrollPane ordersScrollPane;
    protected JTable ordersTable;
    protected Font fontMic = new Font("Times New Roman", Font.PLAIN, 14);
    protected String query = "select Orders.orderId, Clients.clientName, Products.productName, Orders.orderedQuantity as quantity, " +
                             "Products.price, (Orders.orderedQuantity * Products.price) as totalPrice from Orders " +
                             "join Clients on Orders.clientId = Clients.clientId join Products on Orders.productId = Products.productId order by orderId asc;";

    /**
     * Populates the orders table with data from the database.
     */
    public void populateTable() {
        try {
            Statement selectStatement = connection.createStatement();
            selectStatement.executeQuery(query);
            ResultSet selectStatementResultSet = selectStatement.getResultSet();
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new Object[]{"Order ID", "Client Name", "Product Name", "Quantity", "Price", "Total Price"});
            while (selectStatementResultSet.next()) {
                model.addRow(new Object[]{
                        selectStatementResultSet.getInt("orderId"),
                        selectStatementResultSet.getString("clientName"),
                        selectStatementResultSet.getString("productName"),
                        selectStatementResultSet.getInt("quantity"),
                        selectStatementResultSet.getDouble("price"),
                        selectStatementResultSet.getDouble("totalPrice")
                });
            }

            ordersTable = new JTable(model);
            ordersScrollPane.setViewportView(ordersTable);
            ordersScrollPane.setVisible(true);
            containerPanel.add(ordersScrollPane);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the main/container panel for the orders frame and adds it to the frame.
     */
    public void contPanel() {

        containerPanel = new JPanel();
        containerPanel.setLayout(null);
        containerPanel.setVisible(true);
        containerPanel.setBackground(new Color(255, 255, 204));
        orderFrame.getContentPane().add(containerPanel);

        ordersOutput = new JTextArea();
        ordersOutput.setLineWrap(true);
        ordersOutput.setEditable(false);
        ordersOutput.setBounds(10, 10, 660, 420);
        ordersOutput.setFont(fontMic);

        ordersScrollPane = new JScrollPane(ordersTable);
        ordersScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ordersScrollPane.setBounds(20, 10, 700, 430);

        populateTable();

    }

    /**
     * Initializes and configures the frame for the orders frame
     */
    private void init() {

        orderFrame = new JFrame();
        orderFrame.setTitle("Comenzi");
        orderFrame.setSize(750, 500);
        orderFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //productFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        orderFrame.setResizable(false);
        orderFrame.setLocationRelativeTo(null);

        contPanel();
    }

    /**
     * Constructs a new OrdersFrame and initializes the main frame of the program.
     */
    public OrdersFrame() {
        init();
    }

    /**
     * The entry point of the program.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                OrdersFrame window = new OrdersFrame();
                window.orderFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}