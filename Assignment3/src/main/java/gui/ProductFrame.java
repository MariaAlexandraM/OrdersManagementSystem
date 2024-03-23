package gui;

import java.awt.*;

import javax.swing.*;

import businessLayer.ProductsBLL;
import dataAccessLayer.connection.ConnectionFactory;
import dataAccessLayer.dao.AbstractDAO;
import dataAccessLayer.dao.ProductsDAO;
import dataAccessLayer.validators.ProductDataValidator;
import model.Products;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Objects;

/**
 * Represents a GUI frame for managing products data.
 * <p>
 * Provides functionality for adding, editing, deleting, and displaying products.
 */
public class ProductFrame extends ConnectionFactory {

	protected Connection connection = getConnection();
	protected JFrame productFrame;
	protected JPanel containerPanel;
	protected JLabel nameLbl, quantityLbl, priceLbl;
	protected JButton addProductTabBtn, editProductTabBtn, deleteProductTabBtn, showProductsTabBtn, addProductBtn, editProductBtn, deleteProductBtn;
	protected JTextField nameInput, quantityInput, priceInput;
	protected JTextArea productsOutput;
	protected JComboBox<String> productsNamesComboBox;
	protected JScrollPane productsScrollPane;
	protected JTable productsTable;
	protected Font fontMic = new Font("Times New Roman", Font.PLAIN, 14);
	protected Font fontMare = new Font("Times New Roman", Font.BOLD, 16);

	/**
	 * Displays the appropriate input fields (and corresponding labels)
	 * and buttons based on the selected operation.
	 * @param showInputs true if the input fields should be visible, false otherwise
	 * @param addProductOk  true if the addProduct button should be visible, false otherwise
	 * @param editProductOk true if the editProduct button should be visible, false otherwise
	 * @param deleteProductOk true if the deleteProduct button should be visible, false otherwise
	 * @param showProductsOk true if the products table should be visible, false otherwise
	 */
	public void chooseOperation(boolean showInputs, boolean addProductOk, boolean editProductOk, boolean deleteProductOk, boolean showProductsOk) {

		nameLbl.setVisible(showInputs);
		quantityLbl.setVisible(showInputs);
		priceLbl.setVisible(showInputs);

		addProductBtn.setVisible(addProductOk);
		editProductBtn.setVisible(editProductOk);
		deleteProductBtn.setVisible(deleteProductOk);

		productsScrollPane.setVisible(showProductsOk);

		if(addProductOk || editProductOk) {
			quantityInput.setVisible(true);
			priceInput.setVisible(true);
		}

		if(addProductOk) {
			nameInput.setVisible(true);
			productsNamesComboBox.setVisible(false);
		}
		if(editProductOk) {
			nameInput.setVisible(false);
			productsNamesComboBox.setVisible(true);
			productsNamesComboBox.setSelectedIndex(-1);
		}
		if(deleteProductOk) {
			nameInput.setVisible(false);
			productsNamesComboBox.setVisible(true);
			productsNamesComboBox.setSelectedIndex(-1);
			quantityLbl.setVisible(false);
			quantityInput.setVisible(false);
			priceLbl.setVisible(false);
			priceInput.setVisible(false);
		}
		if(showProductsOk) {
			nameLbl.setVisible(false);
			nameInput.setVisible(false);
			productsNamesComboBox.setVisible(false);
			quantityLbl.setVisible(false);
			quantityInput.setVisible(false);
			priceLbl.setVisible(false);
			priceInput.setVisible(false);
		}

		nameInput.setText("");
		quantityInput.setText("");
		priceInput.setText("");
	}

	/**
	 * Initializes the main buttons of the frame and sets their properties.
	 */
	public void mainButtons() {

		addProductTabBtn = new JButton("Adaugare");
		addProductTabBtn.setFont(fontMic);
		addProductTabBtn.setBackground(new Color(247, 218, 229));
		addProductTabBtn.setBounds(10, 10, 120, 30);
		addProductTabBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		addProductTabBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseOperation(true, true, false, false, false);
			}
		});
		containerPanel.add(addProductTabBtn);

		editProductTabBtn = new JButton("Editare");
		editProductTabBtn.setFont(fontMic);
		editProductTabBtn.setBackground(new	Color(226, 216, 234));
		editProductTabBtn.setBounds(130, 10, 120, 30);
		editProductTabBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		editProductTabBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseOperation(true, false, true, false, false);
			}
		});
		containerPanel.add(editProductTabBtn);

		deleteProductTabBtn = new JButton("Stergere");
		deleteProductTabBtn.setFont(fontMic);
		deleteProductTabBtn.setBackground(new Color(202, 225, 255));
		deleteProductTabBtn.setBounds(250, 10, 120, 30);
		deleteProductTabBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		deleteProductTabBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseOperation(true, false, false, true, false);
			}
		});
		containerPanel.add(deleteProductTabBtn);

		showProductsTabBtn = new JButton("Afisare");
		showProductsTabBtn.setVisible(true);
		showProductsTabBtn.setFont(fontMic);
		showProductsTabBtn.setBackground(new Color(204, 255, 153));
		showProductsTabBtn.setBounds(370, 10, 120, 30);
		showProductsTabBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		showProductsTabBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseOperation(false, false, false, false, true);
				AbstractDAO<Products> productsAbstractDAO = new ProductsDAO();
				productsTable = new JTable(productsAbstractDAO.createTable(productsAbstractDAO.findAll()));
				productsScrollPane.setViewportView(productsTable);
			}
		});
		containerPanel.add(showProductsTabBtn);

		initFields();
	}

	/**
	 * Initializes the input fields and labels for adding, editing, deleting, and displaying products.
	 */
	public void initFields() {

		nameLbl = new JLabel("Nume");
		nameLbl.setBounds(100, 100, 50, 30);
		nameLbl.setFont(fontMare);
		nameLbl.setVisible(false);
		containerPanel.add(nameLbl);

		nameInput = new JTextField();
		nameInput.setBounds(170, 100, 200, 30);
		nameInput.setFont(fontMic);
		nameInput.setVisible(false);
		containerPanel.add(nameInput);

		quantityLbl = new JLabel("Cantitate");
		quantityLbl.setBounds(80, 150, 80, 30);
		quantityLbl.setFont(fontMare);
		quantityLbl.setVisible(false);
		containerPanel.add(quantityLbl);

		quantityInput = new JTextField();
		quantityInput.setBounds(170, 150, 200, 30);
		quantityInput.setFont(fontMic);
		quantityInput.setVisible(false);
		containerPanel.add(quantityInput);

		priceLbl = new JLabel("Pret");
		priceLbl.setBounds(110, 200, 50, 30);
		priceLbl.setFont(fontMare);
		priceLbl.setVisible(false);
		containerPanel.add(priceLbl);

		priceInput = new JTextField();
		priceInput.setBounds(170, 200, 200, 30);
		priceInput.setVisible(false);
		priceInput.setFont(fontMic);
		containerPanel.add(priceInput);

		addProductBtn = new JButton("Adauga produs");
		addProductBtn.setBackground(new Color(247, 218, 229));
		addProductBtn.setBounds(170, 275, 200, 50);
		addProductBtn.setFont(fontMare);
		addProductBtn.setVisible(false);
		addProductBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if(nameInput.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Campul de nume gol!", "Error", JOptionPane.ERROR_MESSAGE);
				} else if(quantityInput.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Campul de cantitate nu poate fi gol!", "Error", JOptionPane.ERROR_MESSAGE);
				} else if(priceInput.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Campul de pret nu poate fi gol!", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					String productName = nameInput.getText();
					int quantity = -1;
					double price = -1;
					try {
						quantity = Integer.parseInt(quantityInput.getText());
					} catch(NumberFormatException exc) {
						JOptionPane.showMessageDialog(null, "Cantitatea trebuie sa fie numar intreg", "Error", JOptionPane.ERROR_MESSAGE);
					}
					try {
						price = Double.parseDouble(priceInput.getText());
					} catch(NumberFormatException exc) {
						JOptionPane.showMessageDialog(null, "Pretul trebuie sa fie numar rational", "Error", JOptionPane.ERROR_MESSAGE);
					}

					if(quantity == 0) {
						JOptionPane.showMessageDialog(null, "Cantitatea trebuie sa fie mai mare decat 0", "Error", JOptionPane.ERROR_MESSAGE);
					} else if(price == 0) {
						JOptionPane.showMessageDialog(null, "Pretul trebuie sa fie mai mare decat 0", "Error", JOptionPane.ERROR_MESSAGE);
					} else if(quantity != -1 && price != -1) {
						Products product = new Products(productName, quantity, price);
						ProductDataValidator validator = new ProductDataValidator();
						String validString = "Date produs valide!";
						String result = validator.validationResult(product);
						if (Objects.equals(result, validString)) {
							ProductsBLL productBll = new ProductsBLL();
							productBll.insertObject(product);
							JOptionPane.showMessageDialog(null, productName + " added", "yay", JOptionPane.INFORMATION_MESSAGE);
							nameInput.setText("");
							quantityInput.setText("");
							priceInput.setText("");
						} else {
							JOptionPane.showMessageDialog(null, result, "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
		containerPanel.add(addProductBtn);

		productsNamesComboBox = new JComboBox<>();
		productsNamesComboBox.setBounds(170, 100, 200, 30);
		productsNamesComboBox.setBackground(Color.WHITE);
		productsNamesComboBox.setVisible(false);
		productsNamesComboBox.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				productsNamesComboBox.removeAllItems();
				try {
					Statement selectStatement = connection.createStatement();
					selectStatement.executeQuery("select * from Products;");
					ResultSet selectStatementResultSet = selectStatement.getResultSet();

					while(selectStatementResultSet.next()) {
						productsNamesComboBox.addItem(selectStatementResultSet.getString("productName"));
					}
				} catch (SQLException exc) {
					exc.printStackTrace();
				}
			}
		});
		containerPanel.add(productsNamesComboBox);

		editProductBtn = new JButton("Editeaza produs");
		editProductBtn.setBackground(new Color(226, 216, 234));
		editProductBtn.setBounds(170, 275, 200, 50);
		editProductBtn.setFont(fontMare);
		editProductBtn.setVisible(false);
		editProductBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String productName = (String) productsNamesComboBox.getSelectedItem();
				if (quantityInput.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,  "Campul de cantitate nu poate fi gol", "oops", JOptionPane.ERROR_MESSAGE);
				} else if (priceInput.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,  "Campul de pret nu poate fi gol", "oops", JOptionPane.ERROR_MESSAGE);
				} else {
					int quantity = -1;
					double price = -1;
					try {
						quantity = Integer.parseInt(quantityInput.getText());
					} catch (NumberFormatException exc) {
						JOptionPane.showMessageDialog(null, "Cantitatea trebuie sa fie numar intreg", "Error", JOptionPane.ERROR_MESSAGE);
					}
					try {
						price = Double.parseDouble(priceInput.getText());
					} catch (NumberFormatException exc) {
						JOptionPane.showMessageDialog(null, "Pretul trebuie sa fie numar rational", "Error", JOptionPane.ERROR_MESSAGE);
					}

					if(quantity == 0) {
						JOptionPane.showMessageDialog(null, "Cantitatea trebuie sa fie mai mare decat 0", "Error", JOptionPane.ERROR_MESSAGE);
					} else if(price == 0) {
						JOptionPane.showMessageDialog(null, "Pretul trebuie sa fie mai mare decat 0", "Error", JOptionPane.ERROR_MESSAGE);
					} else if (quantity != -1 && price != -1) {
						ProductsBLL productBll = new ProductsBLL();
						int productId = productBll.getIdByName(productName, "productName", "productId");
						Products product = new Products(productId, productName, quantity, price);
						productBll.editObject(product, "productId");
						JOptionPane.showMessageDialog(null, productName + " edited", "yay", JOptionPane.INFORMATION_MESSAGE);
						quantityInput.setText("");
						priceInput.setText("");
					}
				}
			}
		});
		containerPanel.add(editProductBtn);

		deleteProductBtn = new JButton("Sterge produs");
		deleteProductBtn.setBackground(new Color(202, 225, 255));
		deleteProductBtn.setBounds(170, 275, 200, 50);
		deleteProductBtn.setFont(fontMare);
		deleteProductBtn.setVisible(false);
		deleteProductBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String productName = (String) productsNamesComboBox.getSelectedItem();
				ProductsBLL productBll = new ProductsBLL();
				int productId = productBll.getIdByName(productName, "productName", "productId");
				Products product = new Products(productId);
				try {
					productBll.deleteObject(product, "productId");
					JOptionPane.showMessageDialog(null, productName + " deleted", "yay", JOptionPane.INFORMATION_MESSAGE);
				} catch (HeadlessException ex) {
					JOptionPane.showMessageDialog(null,  "Could not delete " + productName, "yay", JOptionPane.ERROR_MESSAGE);
					throw new RuntimeException(ex);
				}
			}
		});
		containerPanel.add(deleteProductBtn);

		productsOutput = new JTextArea();
		productsOutput.setLineWrap(true);
		productsOutput.setEditable(false);
		productsOutput.setBounds(250, 80, 200, 285); // 530 450
		productsOutput.setFont(fontMic);

		productsTable = new JTable();
		productsTable.setBounds(250, 80, 200, 285);

		productsScrollPane = new JScrollPane(productsOutput);
		productsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		productsScrollPane.setBounds(15, 50, 485, 285);
		productsScrollPane.setViewportView(productsTable);
		productsScrollPane.setVisible(false);
		containerPanel.add(productsScrollPane);

	}

	/**
	 * Creates the main/container panel for the client frame and adds it to the frame.
	 */
	public void contPanel() {

		containerPanel = new JPanel();
		containerPanel.setLayout(null);
		containerPanel.setVisible(true);
		containerPanel.setBackground(new Color(255,255,204));
		productFrame.getContentPane().add(containerPanel);

		mainButtons();
	}

	/**
	 * Initializes the product frame by setting its properties,
	 * creating the main panel, and displaying the appropriate components.
	 * The work has been divided in multiple other methods.
	 */
	private void init() {

		productFrame = new JFrame();
		productFrame.setTitle("Produse");
		productFrame.setSize(530, 450);
		productFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		//productFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		productFrame.setResizable(false);
		productFrame.setLocationRelativeTo(null);

		contPanel();
		chooseOperation(true, true, false, false, false);
	}

	/**
	 * Constructs a new ProductFrame and initializes the main frame of the program.
	 */
	public ProductFrame() {
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
				ProductFrame window = new ProductFrame();
				window.productFrame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}