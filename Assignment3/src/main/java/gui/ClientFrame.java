package gui;

import java.awt.*;

import javax.swing.*;

import businessLayer.ClientsBLL;
import dataAccessLayer.connection.ConnectionFactory;
import dataAccessLayer.dao.AbstractDAO;
import dataAccessLayer.dao.ClientsDAO;
import dataAccessLayer.validators.ClientDataValidator;
import model.Clients;

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
 * Represents a GUI frame for managing clients data.
 * <p>
 * Provides functionality for adding, editing, deleting, and displaying clients.
 */
public class ClientFrame extends ConnectionFactory {

	protected Connection connection = getConnection();
	protected JFrame clientFrame;
	protected JPanel containerPanel;
	protected JLabel nameLbl, addressLbl, emailLbl;
	protected JButton addClientTabBtn, editClientTabBtn, deleteClientTabBtn, showClientsTabBtn, addClientBtn, editClientBtn, deleteClientBtn;
	protected JTextField nameInput, addressInput, emailInput;
	protected JTextArea clientsOutput;
	protected JComboBox<String> clientsNamesComboBox;
	protected JScrollPane clientsScrollPane;
	protected JTable clientsTable;
	protected Font fontMic = new Font("Times New Roman", Font.PLAIN, 14);
	protected Font fontMare = new Font("Times New Roman", Font.BOLD, 16);

	/**
	 * Displays the appropriate input fields (and corresponding labels)
	 * and buttons based on the selected operation.
	 * @param showInputs true if the input fields should be visible, false otherwise
	 * @param addClientOk  true if the addClient button should be visible, false otherwise
	 * @param editClientOk true if the editClient button should be visible, false otherwise
	 * @param deleteClientOk true if the deleteClient button should be visible, false otherwise
	 * @param showClientsOk true if the clients table should be visible, false otherwise
	 */
	public void chooseOperation(boolean showInputs, boolean addClientOk, boolean editClientOk, boolean deleteClientOk, boolean showClientsOk) {

		nameLbl.setVisible(showInputs);
		addressLbl.setVisible(showInputs);
		emailLbl.setVisible(showInputs);

		addClientBtn.setVisible(addClientOk);
		editClientBtn.setVisible(editClientOk);
		deleteClientBtn.setVisible(deleteClientOk);

		clientsScrollPane.setVisible(showClientsOk);

		if(addClientOk || editClientOk) {
			addressInput.setVisible(true);
			emailInput.setVisible(true);
		}

		if(addClientOk) {
			nameInput.setVisible(true);
			clientsNamesComboBox.setVisible(false);
		}
		if(editClientOk) {
			nameInput.setVisible(false);
			clientsNamesComboBox.setVisible(true);
			clientsNamesComboBox.setSelectedIndex(-1);
		}
		if(deleteClientOk) {
			nameInput.setVisible(false);
			clientsNamesComboBox.setVisible(true);
			clientsNamesComboBox.setSelectedIndex(-1);
			addressLbl.setVisible(false);
			addressInput.setVisible(false);
			emailLbl.setVisible(false);
			emailInput.setVisible(false);
		}
		if(showClientsOk) {
			nameLbl.setVisible(false);
			nameInput.setVisible(false);
			clientsNamesComboBox.setVisible(false);
			addressLbl.setVisible(false);
			addressInput.setVisible(false);
			emailLbl.setVisible(false);
			emailInput.setVisible(false);
		}

		nameInput.setText("");
		addressInput.setText("");
		emailInput.setText("");
	}

	/**
	 * Initializes the main buttons of the frame and sets their properties.
	 */
	public void mainButtons() {

		addClientTabBtn = new JButton("Adaugare");
		addClientTabBtn.setFont(fontMic);
		addClientTabBtn.setBackground(new Color(247, 218, 229));
		addClientTabBtn.setBounds(10, 10, 120, 30);
		addClientTabBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		addClientTabBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseOperation(true, true, false, false, false);
			}
		});
		containerPanel.add(addClientTabBtn);

		editClientTabBtn = new JButton("Editare");
		editClientTabBtn.setFont(fontMic);
		editClientTabBtn.setBackground(new	Color(226, 216, 234));
		editClientTabBtn.setBounds(130, 10, 120, 30);
		editClientTabBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		editClientTabBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseOperation(true, false, true, false, false);
			}
		});
		containerPanel.add(editClientTabBtn);

		deleteClientTabBtn = new JButton("Stergere");
		deleteClientTabBtn.setFont(fontMic);
		deleteClientTabBtn.setBackground(new Color(202, 225, 255));
		deleteClientTabBtn.setBounds(250, 10, 120, 30);
		deleteClientTabBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		deleteClientTabBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseOperation(true, false, false, true, false);
			}
		});
		containerPanel.add(deleteClientTabBtn);

		showClientsTabBtn = new JButton("Afisare");
		showClientsTabBtn.setVisible(true);
		showClientsTabBtn.setFont(fontMic);
		showClientsTabBtn.setBackground(new Color(204, 255, 153));
		showClientsTabBtn.setBounds(370, 10, 120, 30);
		showClientsTabBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		showClientsTabBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseOperation(false, false, false, false, true);
				AbstractDAO<Clients> clientsAbstractDAO = new ClientsDAO();
				clientsTable = new JTable(clientsAbstractDAO.createTable(clientsAbstractDAO.findAll()));
				clientsScrollPane.setViewportView(clientsTable);
			}
		});
		containerPanel.add(showClientsTabBtn);

		initFields();
	}


	/**
	 * Initializes the input fields and labels for adding, editing, deleting, and displaying clients.
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

		addressLbl = new JLabel("Adresa");
		addressLbl.setBounds(100, 150, 50, 30);
		addressLbl.setFont(fontMare);
		addressLbl.setVisible(false);
		containerPanel.add(addressLbl);

		addressInput = new JTextField();
		addressInput.setBounds(170, 150, 200, 30);
		addressInput.setFont(fontMic);
		addressInput.setVisible(false);
		containerPanel.add(addressInput);

		emailLbl = new JLabel("Email");
		emailLbl.setBounds(100, 200, 50, 30);
		emailLbl.setFont(fontMare);
		emailLbl.setVisible(false);
		containerPanel.add(emailLbl);

		emailInput = new JTextField();
		emailInput.setBounds(170, 200, 200, 30);
		emailInput.setVisible(false);
		emailInput.setFont(fontMic);
		containerPanel.add(emailInput);

		addClientBtn = new JButton("Adauga client");
		addClientBtn.setBackground(new Color(247, 218, 229));
		addClientBtn.setBounds(170, 275, 200, 50);
		addClientBtn.setFont(fontMare);
		addClientBtn.setVisible(false);
		addClientBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if(nameInput.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Campul de nume gol!", "Error", JOptionPane.ERROR_MESSAGE);
				} else if(addressInput.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Campul de adresa nu poate fi gol!", "Error", JOptionPane.ERROR_MESSAGE);
				} else if(emailInput.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Campul de email nu poate fi gol!", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					String clientName = nameInput.getText();
					String address = addressInput.getText();
					String email = emailInput.getText();

					Clients client = new Clients(clientName, address, email);
					ClientDataValidator validator = new ClientDataValidator();
					String validString = "Date client valide!";
					String result = validator.validationResult(client);
					if (Objects.equals(result, validString)) {
						ClientsBLL clientBll = new ClientsBLL();
						clientBll.insertObject(client);
						JOptionPane.showMessageDialog(null, clientName + " added", "yay", JOptionPane.INFORMATION_MESSAGE);
						nameInput.setText("");
						addressInput.setText("");
						emailInput.setText("");
					} else {
						JOptionPane.showMessageDialog(null, result, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		containerPanel.add(addClientBtn);

		clientsNamesComboBox = new JComboBox<>();
		clientsNamesComboBox.setBounds(170, 100, 200, 30);
		clientsNamesComboBox.setBackground(Color.WHITE);
		clientsNamesComboBox.setVisible(false);
		clientsNamesComboBox.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				clientsNamesComboBox.removeAllItems();
				try {
					Statement selectStatement = connection.createStatement();
					selectStatement.executeQuery("select * from Clients;");
					ResultSet selectStatementResultSet = selectStatement.getResultSet();

					while(selectStatementResultSet.next()) {
						clientsNamesComboBox.addItem(selectStatementResultSet.getString("clientName"));
					}
				} catch (SQLException exc) {
					exc.printStackTrace();
				}
			}
		});
		containerPanel.add(clientsNamesComboBox);

		editClientBtn = new JButton("Editeaza client");
		editClientBtn.setBackground(new Color(226, 216, 234));
		editClientBtn.setBounds(170, 275, 200, 50);
		editClientBtn.setFont(fontMare);
		editClientBtn.setVisible(false);
		editClientBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String clientName = (String) clientsNamesComboBox.getSelectedItem();
				if (addressInput.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,  "Campul de adresa nu poate fi gol", "oops", JOptionPane.ERROR_MESSAGE);
				} else if (emailInput.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,  "Campul de email nu poate fi gol", "oops", JOptionPane.ERROR_MESSAGE);
				} else {
					String address = addressInput.getText();
					String email = emailInput.getText();

					ClientsBLL clientBll = new ClientsBLL();
					int clientId = clientBll.getIdByName(clientName, "clientName", "clientId");
					Clients client = new Clients(clientId, clientName, address, email);
					clientBll.editObject(client, "clientId");

					JOptionPane.showMessageDialog(null, clientName + " edited", "yay", JOptionPane.INFORMATION_MESSAGE);
					addressInput.setText("");
					emailInput.setText("");
				}
			}
		});
		containerPanel.add(editClientBtn);

		deleteClientBtn = new JButton("Sterge client");
		deleteClientBtn.setBackground(new Color(202, 225, 255));
		deleteClientBtn.setBounds(170, 275, 200, 50);
		deleteClientBtn.setFont(fontMare);
		deleteClientBtn.setVisible(false);
		deleteClientBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String clientName = (String) clientsNamesComboBox.getSelectedItem();
				ClientsBLL clientBll = new ClientsBLL();
				int clientId = clientBll.getIdByName(clientName, "clientName", "clientId");
				Clients client = new Clients(clientId);
				try {
					clientBll.deleteObject(client, "clientId");
					JOptionPane.showMessageDialog(null, clientName + " deleted", "yay", JOptionPane.INFORMATION_MESSAGE);
				} catch (HeadlessException ex) {
					JOptionPane.showMessageDialog(null,  "Could not delete " + clientName, "yay", JOptionPane.ERROR_MESSAGE);
					throw new RuntimeException(ex);
				}
			}
		});
		containerPanel.add(deleteClientBtn);

		clientsOutput = new JTextArea();
		clientsOutput.setLineWrap(true);
		clientsOutput.setEditable(false);
		clientsOutput.setBounds(250, 80, 200, 285); // 530 450
		clientsOutput.setFont(fontMic);

		clientsTable = new JTable();
		clientsTable.setBounds(250, 80, 200, 285);

		clientsScrollPane = new JScrollPane(clientsOutput);
		clientsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		clientsScrollPane.setBounds(15, 50, 485, 285);
		clientsScrollPane.setViewportView(clientsTable);
		clientsScrollPane.setVisible(false);
		containerPanel.add(clientsScrollPane);

	}

	/**
	 * Creates the main/container panel for the client frame and adds it to the frame.
	 */
	public void contPanel() {

		containerPanel = new JPanel();
		containerPanel.setLayout(null);
		containerPanel.setVisible(true);
		containerPanel.setBackground(new Color(255,255,204));
		clientFrame.getContentPane().add(containerPanel);

		mainButtons();
	}

	/**
	 * Initializes the client frame by setting its properties,
	 * creating the main panel, and displaying the appropriate components.
	 * The work has been divided in multiple other methods.
	 */
	private void init() {

		clientFrame = new JFrame();
		clientFrame.setTitle("Clienti");
		clientFrame.setSize(530, 450);
		clientFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		//clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientFrame.setResizable(false);
		clientFrame.setLocationRelativeTo(null);

		contPanel();
		chooseOperation(true, true, false, false, false);
	}

	/**
	 * Constructs a new ClientFrame and initializes the main frame of the program.
	 */
	public ClientFrame() {
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
				ClientFrame window = new ClientFrame();
				window.clientFrame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}