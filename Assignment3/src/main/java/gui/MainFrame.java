package gui;
import businessLayer.ClientsBLL;
import businessLayer.OrdersBLL;
import businessLayer.ProductsBLL;
import dataAccessLayer.connection.ConnectionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dataAccessLayer.dao.AbstractDAO;
import dataAccessLayer.dao.ClientsDAO;
import dataAccessLayer.dao.OrdersDAO;
import dataAccessLayer.dao.ProductsDAO;
import model.Clients;
import model.Orders;
import model.Products;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;

/**
 * Clasa MainFrame este fereastra principala a programului, iar ea face conexiunea intre celelalte ferestre
 */
public class MainFrame extends ConnectionFactory {

	protected Connection connection = getConnection();
	protected JFrame frame; // orderFrame
	protected JPanel containerPanel;
	protected JLabel clientNameLbl, productNameLbl, quantityLbl;
	protected JButton goToClientsBtn, goToProductsBtn, goToOrdersBtn, placeOrderBtn;
	protected JComboBox<String> clientsComboBox, productsComboBox;
	protected JTextField quantityInput;
	protected Font fontMic = new Font("Times New Roman", Font.PLAIN, 14);
	protected Font fontMare = new Font("Times New Roman", Font.PLAIN, 16);

	/**
	 * Metoda creeaza si initializeaza container panel-ul pentru fereastra principala
	 */
	public void contPanel() {
		containerPanel = new JPanel();
		containerPanel.setLayout(null);
		containerPanel.setBackground(new Color(250, 200, 250));
		frame.getContentPane().add(containerPanel);
	}

	/**
	 * Metoda genereaza o factura/un bon pentru comanda plasata, sub forma unui pdf
	 * @param orderId id-ul comenzii pentru care se va emite bonul
	 */
	public void makeBill(int orderId) {
		String path = "C:\\Users\\Maria\\Desktop\\comanda_" + orderId + ".pdf";
		Document bill = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(bill, new FileOutputStream(path));
			bill.open();
			bill.addTitle("comanda_" + orderId);

			AbstractDAO<Orders> orderDao = new OrdersDAO();
			Orders order = orderDao.findById(orderId, "orderId");

			AbstractDAO<Clients> clientDao = new ClientsDAO();
			int clientId = order.getClientId();
			Clients client = clientDao.findById(clientId, "clientId");
			String clientName = client.getClientName();
			String address = client.getAddress();
			String email = client.getEmail();

			LocalDate currentDate = LocalDate.now();
			String formattedDate = String.format("%02d-%02d-%d", currentDate.getDayOfMonth(), currentDate.getMonthValue(), currentDate.getYear());
			bill.add(new Paragraph("\n"));
			bill.add(new Paragraph(formattedDate));
			bill.add(new Paragraph("\n"));


			bill.add(new Paragraph("Client: " + clientName));
			bill.add(new Paragraph("Date de contact: " + "\n\t" + address + "\n\t" + email));
			bill.add(new Paragraph("\n"));

			AbstractDAO<Products> productDao = new ProductsDAO();
			int productId = order.getProductId();
			Products product = productDao.findById(productId, "productId");
			String productName = product.getProductName();
			int quantity = order.getOrderedQuantity();
			double price = product.getPrice();
			double totalPrice = quantity * price;

			PdfPTable orderTable = new PdfPTable(3);
			orderTable.addCell(new PdfPCell(new Phrase("Produs")));
			orderTable.addCell(new PdfPCell(new Phrase("Cantitate")));
			orderTable.addCell(new PdfPCell(new Phrase("Pret")));

			orderTable.addCell(new PdfPCell(new Phrase(productName)));
			orderTable.addCell(new PdfPCell(new Phrase(String.valueOf(quantity))));
			orderTable.addCell(new PdfPCell(new Phrase(String.valueOf(price))));
			bill.add(new Paragraph("\n"));

			bill.add(orderTable);
			bill.add(new Paragraph("\n"));
			bill.add(new Paragraph("Cost total: " + totalPrice)); // orderedQuantity * Products.price
			bill.close();

		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metoda de gestionare a butoanelor ferestrei
	 */
	public void btnListeners() {

		goToClientsBtn = new JButton("Clienti");
		goToClientsBtn.setBounds(70, 260, 120, 50);
		goToClientsBtn.setFont(fontMare);
		goToClientsBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		goToClientsBtn.setBackground(new Color(220, 210, 220));
		goToClientsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientFrame clientFrame = new ClientFrame();
				clientFrame.clientFrame.setVisible(true);
			}
		});
		containerPanel.add(goToClientsBtn);

		goToProductsBtn = new JButton("Produse");
		goToProductsBtn.setBounds(200, 260, 120, 50);
		goToProductsBtn.setFont(fontMare);
		goToProductsBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		goToProductsBtn.setBackground(new Color(220, 210, 220));
		goToProductsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProductFrame productFrame = new ProductFrame();
				productFrame.productFrame.setVisible(true);
			}
		});
		containerPanel.add(goToProductsBtn);

		goToOrdersBtn = new JButton("Comenzi");
		goToOrdersBtn.setBounds(330, 260, 120, 50);
		goToOrdersBtn.setFont(fontMare);
		goToOrdersBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		goToOrdersBtn.setBackground(new Color(220, 210, 220));
		goToOrdersBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OrdersFrame orderFrame = new OrdersFrame();
				orderFrame.orderFrame.setVisible(true);
			}
		});
		containerPanel.add(goToOrdersBtn);

		placeOrderBtn = new JButton("Plasare comanda");
		placeOrderBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		placeOrderBtn.setFont(fontMare);
		placeOrderBtn.setBackground(new Color(220, 210, 220));
		placeOrderBtn.setBounds(150, 190, 200, 50);
		placeOrderBtn.setVisible(true);
		placeOrderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(clientsComboBox.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(null, "Selectati un client!", "Error", JOptionPane.ERROR_MESSAGE);
				} else if(productsComboBox.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(null, "Selectati un produs!", "Error", JOptionPane.ERROR_MESSAGE);
				} else if(quantityInput.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Introduceti cantitatea!", "Error", JOptionPane.ERROR_MESSAGE);
				} else {

					int quantity = -1;
					try {
						quantity = Integer.parseInt(quantityInput.getText());
					} catch (NumberFormatException exc) {
						JOptionPane.showMessageDialog(null, "Cantitatea trebuie sa fie numar intreg", "Error", JOptionPane.ERROR_MESSAGE);
					}
					if(quantity == 0) {
						JOptionPane.showMessageDialog(null, "Cantitatea trebuie sa fie mai mare decat 0", "Error", JOptionPane.ERROR_MESSAGE);
					} else if(quantity != -1) {

						ClientsBLL clientBll = new ClientsBLL();
						String clientName = (String) clientsComboBox.getSelectedItem();
						int clientId = clientBll.getIdByName(clientName, "clientName", "clientId");

						ProductsBLL productBll = new ProductsBLL();
						String productName = (String) productsComboBox.getSelectedItem();
						int productId = productBll.getIdByName(productName, "productName", "productId");

						Products product = productBll.findById(productId, "productId");
						int availableQuantity = product.getQuantity();

						System.out.println("available " + availableQuantity + " ordered " + quantity);

						if (quantity > availableQuantity) {
							String msg = "Nu avem in stoc " + quantity + " " + productName + ".\nDisponibil: " + availableQuantity;
							JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
						} else {
							OrdersBLL orderBll = new OrdersBLL();
							try {
								int orderId = orderBll.makeOrder(clientId, productId, quantity);
								JOptionPane.showMessageDialog(null, "Comanda " + orderId + " plasata!", "yay", JOptionPane.INFORMATION_MESSAGE);
								clientsComboBox.setSelectedIndex(-1);
								productsComboBox.setSelectedIndex(-1);
								quantityInput.setText("");
								makeBill(orderId);

							} catch (HeadlessException ex) {
								JOptionPane.showMessageDialog(null, "Eroare la comanda", "Error", JOptionPane.ERROR_MESSAGE);
								throw new RuntimeException(ex);
							}
						}
					}
				}
			}
		});
		containerPanel.add(placeOrderBtn);


	}

	/**
	 * Metoda de gestionare a inputurilor de la utlizator, de la creeare pana la plasare in fereastra
	 */
	public void inputs() {
		clientNameLbl = new JLabel("Nume client");
		clientNameLbl.setBounds(60, 40, 100, 30);
		clientNameLbl.setFont(fontMare);
		containerPanel.add(clientNameLbl);

		clientsComboBox = new JComboBox<>();
		clientsComboBox.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent focusEvent) {
				clientsComboBox.removeAllItems();

				try{
					Statement selectStatement = connection.createStatement();
					selectStatement.executeQuery("select * from Clients");
					ResultSet selectStatementResultSet = selectStatement.getResultSet();

					while(selectStatementResultSet.next()) {
						clientsComboBox.addItem(selectStatementResultSet.getString("clientName"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		clientsComboBox.setFont(fontMare);
		clientsComboBox.setBackground(new Color(250, 240, 250));
		clientsComboBox.setBounds(190, 40, 240, 30);
		clientsComboBox.setSelectedIndex(-1);
		containerPanel.add(clientsComboBox);

		productNameLbl = new JLabel("Nume produs");
		productNameLbl.setBounds(60, 80, 100, 30);
		productNameLbl.setFont(fontMare);
		containerPanel.add(productNameLbl);

		productsComboBox = new JComboBox<>();
		productsComboBox.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent focusEvent) {
				productsComboBox.removeAllItems();

				try {
					Statement validProducts = connection.createStatement();
					validProducts.executeQuery("select * from Products where quantity > 0;");
					ResultSet validProductsResultSet = validProducts.getResultSet();

					while(validProductsResultSet.next())
						productsComboBox.addItem(validProductsResultSet.getString("productName"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		productsComboBox.setBounds(190, 80, 200, 30);
		productsComboBox.setBackground(new Color(250, 240, 250));
		productsComboBox.setFont(fontMare);
		containerPanel.add(productsComboBox);

		quantityLbl = new JLabel("Cantitate");
		quantityLbl.setFont(fontMare);
		quantityLbl.setBounds(60, 120, 100, 30);
		containerPanel.add(quantityLbl);

		quantityInput = new JTextField();
		quantityInput.setBounds(190, 120, 50, 30);
		quantityInput.setColumns(10);
		quantityInput.setFont(fontMic);
		containerPanel.add(quantityInput);
	}

	/**
	 * Metoda de initializare a ferestrei, care are loc la fiecare rulare
	 */
	private void init() {
		frame = new JFrame();
		frame.setTitle("Program pentru gestiunea comenzilor");
		frame.setSize(530, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		contPanel();
		inputs();
		btnListeners();
	}

	/**
	 * Constructs a new MainFrame and initializes the main frame of the program.
	 */
	public MainFrame() {
		init();
	}

	/**
	 * The entry point of the program.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
