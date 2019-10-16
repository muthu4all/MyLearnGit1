package com.hotel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

/**
 * COPYRIGHT 2015 TupleMeOver. All Rights Reserved. 
 * Hotel Management 
 * CS157A Group Project
 * @author Kun Su, Ly Dang, Lynn Longboy
 * @version 1.00 2015/11/01
 */

/**
 * The view manager. Contains the frame and all different panels.
 */
public class View {
	private Model model;
	private JPanel cards;
	private CardLayout cardLayout;
	private JFrame frame;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
	final View view = this;

	/**
	 * Constructs the frame and adds panels to CardLayout.
	 */
	public View(Model model) {
		this.model = model;
		frame = new JFrame("Hotel");
		cards = new JPanel(cardLayout = new CardLayout());

		// add panels to the card layout
		cards.add(getLoginPanel(), "Login");
		cards.add(getChooseRolePanel(), "Choose Role");
		cards.add(getRegisterPanel(), "Register");
		cards.add(getForgotPasswordPanel(), "Forgot Password");
		cards.add(getWelcomePanel("Customer"), "Customer");
		cards.add(getWelcomePanel("Manager"), "Manager");
		cards.add(getWelcomePanel("Room Service"), "Room Service");
		cards.add(getWelcomePanel("Receptionist"), "Receptionist");

		// customer panels
		cards.add(getMakeReservationPanel(), "Book");
		cards.add(getReceiptPanel(), "Receipt");
		cards.add(getCustViewCancelPanel(), "View/Cancel");
		cards.add(getOrderRoomServicePanel(), "Order");
		cards.add(getFileComplaintPanel(), "File Complaint");

		// employee panels
		cards.add(getReservationsPanel(), "Reservations");
		cards.add(getRoomServicePanel(), "Tasks");
		cards.add(getStatisticsPanel(), "Statistics");
		cards.add(getArchivePanel(), "Archive");
		cards.add(getUsersPanel(), "Users");
		cards.add(getCheckOutPanel(), "Check out");

		cards.add(getComplaintsPanel(), "Complaints");		
		cards.add(getViewRoomServicePanel(), "View Room Service");

		frame.add(cards); // add the panel with card layout to the frame

		// below are the frame's characteristics
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * Gets the model of the view manager.
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * Switches the current panel.
	 * @param panelName
	 */
	public void switchPanel(String panelName) {
		if (panelName.equals("Login"))
			frame.setSize(400, 350);
		else 
			frame.setSize(600, 500);
		frame.setLocationRelativeTo(null);
		cardLayout.show(cards, panelName);
	}

	private JPanel getArchivePanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();

		c.gridwidth = 2;
		c.ipady = 30;
		panel.addLabel("Archive", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.ipady = 0;

		c.insets = new Insets(10,10,10,10);
		panel.addLabel("<html>Enter a date. Reservations, room service requests, and "
				+ "complaints will be archived from this date.</html>", 12, "left", null, null, 0, 1);

		c.gridwidth = 1;
		c.weighty = 1;
		panel.addLabel("Date to archive from (MM/DD/YYYY):", 12, "left", null, null, 0, 2);
		c.gridwidth = 1;
		final JTextField date = new JTextField();
		panel.addComponent(date, 1, 2);

		c.gridwidth = 2;
		JButton archiveBtn = new JButton("Archive");
		archiveBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
		archiveBtn.addActionListener(new ActionListener() {
			@Override() 
			public void actionPerformed(ActionEvent e) {
				String archiveDate = date.getText();
				GregorianCalendar cal;
				if (archiveDate.length() == 10) {
					cal = isValidDateFormat(archiveDate);
					if (cal != null) {
						int response = JOptionPane.showConfirmDialog(
								new JFrame(), "Are you sure you want to archive?",
								"Confirmation", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
						if (response == JOptionPane.NO_OPTION) ;
						if (response == JOptionPane.YES_OPTION) {
							if(!model.archive(cal.getTime())) 
								JOptionPane.showMessageDialog(new JFrame(), 
										"An unexpected error has occurred. Please contact your system admin.", "Error", 
										JOptionPane.ERROR_MESSAGE);	
							else {
								JOptionPane.showMessageDialog(new JFrame(), "Archive Successful", "Result", JOptionPane.DEFAULT_OPTION);
								panel.clearComponents();
								switchPanel("Manager");
							}
						}
					}
					else 
						JOptionPane.showMessageDialog(new JFrame(),
								"Error: Invalid format.", "Error",
								JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.addComponent(archiveBtn, 0, 3);

		panel.addNavigationButton("Back to main menu", 12, "Manager", 0, 4);
		return panel;
	}

	private JPanel getLoginPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.weighty = 1;
		c.gridwidth = 2;
		c.ipady = 15;
		panel.addLabel("Login", 36, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.insets = new Insets(10,15,5,15);
		c.weightx = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.ipady = 0;
		panel.addLabel("Username:", 20, "center", null, null, 0, 2);
		final JTextField usernameField = new JTextField();
		panel.addComponent(usernameField, 1, 2);

		c.insets = new Insets(5,15,5,15);

		panel.addLabel("Password:", 20, "center", null, null, 0, 3);
		final JPasswordField passwordField = new JPasswordField();
		panel.addComponent(passwordField, 1, 3);

		c.gridwidth = 2;
		JButton loginBtn = new JButton("Login");
		loginBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());

				if (username.length() < 6 || username.length() > 12) {
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: Entered user username is invalid.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!model.checkUserExistence(username)) {	
					JOptionPane.showMessageDialog(new JFrame(), 
							"Error: Username does not exist in the system.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!model.checkUserPassword(username, password)) {
					JOptionPane.showMessageDialog(new JFrame(), 
							"Error: Password is incorrect.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					panel.clearComponents();
					model.setCurrentUser(username);
					view.switchPanel(model.getCurrentRole());
				}
			}
		});
		panel.addComponent(loginBtn, 0, 4);

		c.gridwidth = 1;
		panel.addNavigationButton("Forgot Password?", 14, "Forgot Password", 0, 5);
		panel.addNavigationButton("Register", 14, "Choose Role", 1, 5);

		return panel;
	}

	private JPanel getChooseRolePanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.weighty = 1;

		panel.addLabel("Choose a role", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.insets = new Insets(10,25,10,25);
		JButton custBtn = new JButton("Customer");
		custBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		custBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCurrentRole("Customer");
				view.switchPanel("Register");
			}
		});
		panel.addComponent(custBtn, 0, 1);

		JButton mgrBtn = new JButton("Manager");
		mgrBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		mgrBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCurrentRole("Manager");
				view.switchPanel("Register");
			}
		});
		panel.addComponent(mgrBtn, 0, 2);

		JButton recBtn = new JButton("Receptionist");
		recBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		recBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCurrentRole("Receptionist");
				view.switchPanel("Register");
			}
		});
		panel.addComponent(recBtn, 0, 3);

		JButton rsBtn = new JButton("Room Service");
		rsBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
		rsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCurrentRole("Room Service");
				view.switchPanel("Register");
			}
		});
		panel.addComponent(rsBtn, 0, 4);

		panel.addNavigationButton("Back", 12, "Login", 0, 11);

		return panel;
	}

	private JPanel getRegisterPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.weighty = 1;
		c.gridwidth = 4;
		final JLabel instructions = panel.addLabel("", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);
		model.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (model.getCurrentRole() != null) {
					String text = model.getCurrentRole();
					if (text.equalsIgnoreCase("room service"))
						text += " Staff";
					instructions.setText(text + " Registration");
				}
			};
		});

		c.weightx = 0;
		c.insets = new Insets(5,25,5,25);
		c.gridwidth = 1;
		panel.addLabel("First name (max 15 chars)", 12, "left", null, null, 0, 1);
		panel.addLabel("Last name (max 15 chars)", 12, "left", null, null, 0, 2);
		panel.addLabel("Username (6 to 12 chars)", 12, "left", null, null, 0, 3);
		panel.addLabel("Password (8 to 20 chars)", 12, "left", null, null, 0, 4);
		panel.addLabel("Age", 12, "left", null, null, 0, 5);
		panel.addLabel("Gender", 12, "left", null, null, 0, 6);
		panel.addLabel("Security Question (10 to 50 chars)", 12, "left", null, null, 0, 7);
		panel.addLabel("Security Answer (5 to 30 chars)", 12, "left", null, null, 0, 8);
		panel.addNavigationButton("Back", 16, "Choose Role", 0, 9);

		c.weightx = 1;
		c.gridwidth = 3;
		final JTextField firstName = new JTextField();
		panel.addComponent(firstName, 1, 1);

		final JTextField lastName = new JTextField();
		panel.addComponent(lastName, 1, 2);

		final JTextField username = new JTextField();
		panel.addComponent(username, 1, 3);

		final JPasswordField password = new JPasswordField();
		panel.addComponent(password, 1, 4);

		List<String> age = new ArrayList<String>();
		age.add("Select Age");
		for (int i = 18; i < 100; ++i)
			age.add(String.valueOf(i));
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final JComboBox ageComboBox = new JComboBox(age.toArray());
		panel.addComponent(ageComboBox, 1, 5);

		List<String> gender = new ArrayList<String>();
		gender.add("Select Gender");
		gender.add("Female");
		gender.add("Male");
		gender.add("Decline to state");
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final JComboBox genderComboBox = new JComboBox(gender.toArray());
		panel.addComponent(genderComboBox, 1, 6);

		final JTextField securityQuestion = new JTextField();
		panel.addComponent(securityQuestion, 1, 7);

		final JTextField securityAnswer = new JTextField();
		panel.addComponent(securityAnswer, 1, 8);

		JButton registerBtn = new JButton("Register");
		registerBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
		registerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean validEntry = true;
				String errors = "<html>The following are not valid entries:<br>";

				String first = firstName.getText();
				if (first.isEmpty() || first.length() > 15) {
					firstName.setText("");
					validEntry = false;
					errors += "First name<br>";
				}

				String last = lastName.getText();
				if (last.isEmpty() || last.length() > 15) {
					lastName.setText("");
					validEntry = false;
					errors += "Last name<br>";
				}

				String login = username.getText();
				if (login.length() < 6 || login.length() > 12 || model.checkUserExistence(login)) {
					username.setText("");
					validEntry = false;
					errors += "Username<br>";
				}

				String pass = new String(password.getPassword()); 
				if (pass.length() > 20 && pass.length() < 8) {
					password.setText("");
					validEntry = false;
					errors += "Password<br>";
				}

				Integer age = null;
				try {
					age = Integer.parseInt((String)ageComboBox.getSelectedItem());
				}
				catch (Exception e) {
					validEntry = false;
					errors += "Age<br>";
				}

				String gen = (String)genderComboBox.getSelectedItem();
				if (gen.equals("Female"))
					gen = "F";
				else if (gen.equals("Male"))
					gen = "M";
				else if (gen.equals("Decline to state"))
					gen = "D";
				else {
					validEntry = false;
					errors += "Gender<br>";
				}

				String secQuestion = securityQuestion.getText();
				if (secQuestion.length() > 50 || secQuestion.length() < 10) {
					securityQuestion.setText("");
					validEntry = false;
					errors += "Security Question<br>";
				}

				String secAnswer = securityAnswer.getText();
				if (secQuestion.length() > 30 || secQuestion.length() < 5) {
					securityAnswer.setText("");
					validEntry = false;
					errors += "Security Answer";
				}

				if (validEntry) {
					panel.clearComponents();
					if (model.addAccount(login, pass, first, last, age, gen, model.getCurrentRole(), secQuestion, secAnswer))
						view.switchPanel(model.getCurrentRole());
					else
						JOptionPane.showMessageDialog(new JFrame(), 
								"An unexpected error has occurred. Please contact your system admin.", "Error", 
								JOptionPane.ERROR_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(new JFrame(), errors + "</html>", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		panel.addComponent(registerBtn, 1, 9);

		return panel;
	}

	private JPanel getForgotPasswordPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.gridwidth = 2;
		panel.addLabel("Retrieve Password", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.insets = new Insets(20, 20, 20, 20);
		c.ipady = 25;
		panel.addInstructions("<html>Enter a valid username and a question will appear."
				+ "<br>Answer the question to receive your password.</html>");

		c.weighty = 1;
		c.gridwidth = 1;
		panel.addLabel("Enter your username:", 16, "left", null, null, 0, 2);
		panel.addLabel("Your security question:", 16, "left", null, null, 0, 3);
		panel.addLabel("Enter your answer:", 16, "left", null, null, 0, 4);

		final JTextField userField = new JTextField();
		panel.addComponent(userField, 1, 2);

		final JLabel questionField = new JLabel();
		panel.addComponent(questionField, 1, 3);

		final JTextField answerField = new JTextField();
		panel.addComponent(answerField, 1, 4);

		userField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String username = userField.getText();
				String question = model.getUserSecurityQuestion(username);
				if (question != null) {
					questionField.setText(question);
				}
			}
		});

		panel.addNavigationButton("Back", 16, "Login", 0, 5);

		JButton submitButton = new JButton("Get Password");
		submitButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String password = null;
				String username = userField.getText();
				String answer = answerField.getText();
				if (questionField.getText().isEmpty())
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: Enter a valid username", "Error",
							JOptionPane.ERROR_MESSAGE);
				else if (answer.length() < 1)
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: Answer the security question", "Error",
							JOptionPane.ERROR_MESSAGE);
				else if ((password = model.checkSecurityAnswer(username, answer)) != null) {
					JOptionPane.showMessageDialog(new JFrame(),
							"Your password is " + password, "Password",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: Wrong security answer.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.addComponent(submitButton, 1, 5);

		return panel;
	}

	private JPanel getWelcomePanel(String role) {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.weighty = 1;
		c.insets = new Insets(10,10,10,10);

		final JLabel profile = new JLabel();
		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event)
			{
				if (model.getCurrentUser() != null) {
					Account user = model.getCurrentUser();
					profile.setText("<html>Username: " + user.getUsername() 
					+ "<br>Name: " + user.getFirstName() + " " + user.getLastName()
					+ "<br>Role: " + user.getRole());
				}
			}
		});
		panel.addComponent(profile, 0, 0);

		panel.addSignOutButton(16, "Login", 1, 0);

		c.gridwidth = 2;
		if (role.equalsIgnoreCase("manager")) {
			panel.addNavigationButton("Reservations", 16, "Reservations", 0, 1);
			panel.addNavigationButton("Complaints", 16, "Complaints", 0, 2); 
			panel.addNavigationButton("Users", 16, "Users", 0, 3);
			panel.addNavigationButton("Statistics", 16, "Statistics", 0, 4);
			panel.addNavigationButton("Archive Database", 16, "Archive", 0, 5);
		}
		else if (role.equalsIgnoreCase("customer")) {
			panel.addNavigationButton("Book a reservation", 16, "Book", 0, 1);
			panel.addNavigationButton("View/Cancel Reservations", 16, "View/Cancel", 0, 2);
			panel.addNavigationButton("Order Room Service", 16, "Order", 0, 3);
			panel.addNavigationButton("File Complaint", 16, "File Complaint", 0, 4);
		}
		else if (role.equalsIgnoreCase("room service")) {
			panel.addNavigationButton("Tasks", 16, "Tasks", 0, 1);
		}
		else {
			panel.addNavigationButton("Check Out a Customer", 16, "Check out", 0, 3);
		}

		return panel;
	}

	private JPanel getMakeReservationPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.gridwidth = 2;
		c.ipady = 30;
		panel.addLabel("Reserve a Room", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.insets = new Insets(10, 10, 10, 10);
		c.gridwidth = 1;
		c.ipady = 0;
		panel.addLabel("Check-in (MM/DD/YYYY):", 12, "left", null, null, 0, 1);
		panel.addLabel("Check-out (MM/DD/YYYY):", 12, "left", null, null, 1, 1);

		c.gridwidth = 1;
		final JTextField checkIn = new JTextField();
		panel.addComponent(checkIn, 0, 2);

		final JTextField checkOut = new JTextField();
		panel.addComponent(checkOut, 1, 2);

		c.gridwidth = 2;
		c.weighty = 1;
		@SuppressWarnings({ "rawtypes"})
		final JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		panel.addComponent(list);
		JScrollPane listScroller = new JScrollPane(list);
		panel.addComponent(listScroller, 0, 4);

		c.weighty = 0;

		JButton searchBtn = new JButton("Search for rooms");
		searchBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		searchBtn.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override() 
			public void actionPerformed(ActionEvent e) {
				list.setListData(new Object[1]);
				String in = checkIn.getText();
				String out = checkOut.getText();
				GregorianCalendar inCal;
				GregorianCalendar outCal;
				if (in.length() == 10 && out.length() == 10) {
					inCal = isValidDateFormat(in);
					outCal = isValidDateFormat(out);
					if (inCal != null && outCal != null) {
						if (inCal.before(Model.TODAY) || outCal.before(Model.TODAY))
							JOptionPane.showMessageDialog(new JFrame(),
									"Error: Date(s) prior to today.", "Error",
									JOptionPane.ERROR_MESSAGE);
						else if (outCal.before(inCal))
							JOptionPane.showMessageDialog(new JFrame(),
									"Error: Check-in after check-out.", "Error",
									JOptionPane.ERROR_MESSAGE);
						else if (checkDaysBetween(inCal, outCal) > 60)
							JOptionPane.showMessageDialog(new JFrame(),
									"Error: Reservation cannot be longer than 60 days.", "Error",
									JOptionPane.ERROR_MESSAGE);
						else if (inCal.equals(outCal))
							JOptionPane.showMessageDialog(new JFrame(),
									"Error: Check-in and check-out cannot be the same day.", "Error",
									JOptionPane.ERROR_MESSAGE);
						else {
							if (model.getAvailRooms(in, out) != null)
								list.setListData(model.getAvailRooms(in, out).toArray());
						}
					}
					else 
						JOptionPane.showMessageDialog(new JFrame(),
								"Error: Invalid formats.", "Error",
								JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.addComponent(searchBtn, 0, 3);

		c.gridwidth = 1;
		JButton confirmBtn = new JButton("Confirm");
		confirmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Room room = (Room)list.getSelectedValue();
				if (room != null) {
					if (model.addReservation(room.getRoomId(), checkIn.getText(), checkOut.getText())) {
						int response = JOptionPane.showConfirmDialog(
								new JFrame(), "<html>Your reservation has been saved.<br>"
										+ "Would you like to make more transactions?</html>",
										"Confirmation", JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE);
						if (response == JOptionPane.NO_OPTION) switchPanel("Receipt");
						if (response == JOptionPane.YES_OPTION) ;
						panel.clearComponents();
					}
				}
				else
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: No room has been selected.", "Error",
							JOptionPane.ERROR_MESSAGE);
			}
		});
		panel.addComponent(confirmBtn, 0, 5);

		JButton doneBtn = new JButton("Transaction Done");
		doneBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.getReservations().isEmpty()) 
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: No reservations have been made.", "Error",
							JOptionPane.ERROR_MESSAGE);
				else {
					panel.clearComponents();
					switchPanel("Receipt");
				}
			}
		});
		panel.addComponent(doneBtn, 1, 5);

		c.gridwidth = 2;
		JButton backBtn = new JButton("Back to main menu");
		backBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.getReservations().isEmpty()) {
					panel.clearComponents();
					view.switchPanel("Customer");
				} else {
					JOptionPane.showMessageDialog(new JFrame(),
							"Error: You must complete your transaction.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.addComponent(backBtn, 0, 6);

		return panel;
	}

	private JPanel getReceiptPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();

		c.gridwidth = 1;
		c.ipady = 30;
		panel.addLabel("Receipt", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.ipady = 0;
		c.weighty = 1;
		c.insets = new Insets(10,10,10,10);
		final JTextArea receipt = new JTextArea();
		receipt.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(receipt,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		DefaultCaret caret = (DefaultCaret) receipt.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		panel.addComponent(scrollPane, 0, 2);
		panel.addComponent(receipt);

		model.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!model.getReservations().isEmpty()) {
					Account user = model.getCurrentUser();
					String text = "Username: " + user.getUsername() + "\nName: " + user.getFirstName() 
					+ " " + user.getLastName() + "\nReservations made: " + model.getReservations().size();

					double cost = 0;
					int i = 1;
					for (Reservation r : model.getReservations()) {
						text += String.format("\n\nReservation # %d\n%s", i, r.toString());
						cost += r.getTotalCost();
						i++;
					}

					text += String.format("\n\nTotal: $%.2f", cost);

					receipt.setText(text);
				}
			};
		});

		c.weighty = 0;
		JButton backBtn = new JButton("Back to main menu");
		backBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.clearResrvations();
				panel.clearComponents();
				view.switchPanel("Customer");
			}
		});
		panel.addComponent(backBtn, 0, 3);
		return panel;
	}

	private JPanel getCustViewCancelPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();

		c.gridwidth = 1;
		c.ipady = 30;
		panel.addLabel("View or Cancel a Reservation", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.ipady = 0;
		c.insets = new Insets(10,10,10,10);
		panel.addLabel("<html>Below are all your reservations.<br>To cancel a "
				+ "reservation: Select the one you wish to cancel. Press cancel.<br>"
				+ "If the list is empty, then you have not made any reservations.</html>", 12, "left", 
				null, null, 0, 1);

		@SuppressWarnings("rawtypes")
		final JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		c.weighty = 1;
		panel.addComponent(listScroller, 0, 2);
		panel.addComponent(list);

		model.addChangeListener(new ChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void stateChanged(ChangeEvent e) {
				ArrayList<Reservation> notCanceled = new ArrayList<Reservation>();
				if (model.getCurrentUser() != null) {
					for (Reservation r : model.getCurrentUser().getReservations())
						if (!r.getCanceled())
							notCanceled.add(r);

					list.setListData(notCanceled.toArray());
				}
				else list.setListData(new Reservation[0]);
			}
		});

		c.weighty = 0;
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!list.isSelectionEmpty()) {
					int response = JOptionPane.showConfirmDialog(new JFrame(),
							"Are you sure you want to cancel this reservation?",
							"Confirmation", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.NO_OPTION) ;
					if (response == JOptionPane.YES_OPTION) {
						if (!model.cancelReservation((Reservation) list.getSelectedValue()))
							JOptionPane.showMessageDialog(new JFrame(), 
									"An unexpected error has occurred. Please contact your system admin.", "Error", 
									JOptionPane.ERROR_MESSAGE);;
					}
				}
			}
		});
		panel.addComponent(cancelButton, 0, 3);

		JButton backBtn = new JButton("Back to main menu");
		backBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.switchPanel("Customer");
			}
		});
		panel.addComponent(backBtn, 0, 4);
		return panel;
	}

	private JPanel getReservationsPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();

		c.gridwidth = 4;
		c.ipady = 30;
		c.weighty = 0;
		panel.addLabel("Reservations", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.ipady = 0;
		c.insets = new Insets(10,10,10,10);
		panel.addLabel("Enter a min and max and sort by room or customer.", 12, "left", 
				null, null, 0, 1);

		c.gridwidth = 1;
		panel.addLabel("Min cost (optional)", 12, "left", null, null, 0, 2);
		final JTextField minTF = new JTextField();
		panel.addComponent(minTF, 1, 2);

		panel.addLabel("Max cost (optional)", 12, "left", null, null, 2, 2);
		final JTextField maxTF = new JTextField();
		panel.addComponent(maxTF, 3, 2);

		c.gridwidth = 2;
		JButton roomBtn = new JButton("Order by Rooms");
		roomBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.addComponent(roomBtn, 0, 3);
		JButton customerBtn = new JButton("Order by Customer");
		roomBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.addComponent(customerBtn, 2, 3);

		c.weightx = 1;
		c.gridwidth = 4;
		c.weighty = 1;
		c.insets = new Insets(10,10,10,10);
		final JTextArea list = new JTextArea();
		list.setEditable(false);
		list.setWrapStyleWord(true);
		list.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(list,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		DefaultCaret caret = (DefaultCaret) list.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		panel.addComponent(scrollPane, 0, 4);
		panel.addComponent(list);

		roomBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Double min = null, max = null;

				try {
					if (!minTF.getText().equals("")) 
						min = Double.parseDouble(minTF.getText());
					if (!maxTF.getText().equals(""))
						max = Double.parseDouble(maxTF.getText());

					ArrayList<Reservation> res = model.getReservations("roomType", min, max);
					if (res != null)
						list.setText(formatReservations(res));
					else 
						JOptionPane.showMessageDialog(new JFrame(), 
								"An unexpected error has occurred. Please contact your system admin.", "Error", 
								JOptionPane.ERROR_MESSAGE);
				}
				catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(new JFrame(), 
							"Error: Invalid input(s).", "Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		customerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Double min = null, max = null;

				if (!minTF.getText().equals("")) 
					min = Double.parseDouble(minTF.getText());
				if (!maxTF.getText().equals(""))
					max = Double.parseDouble(maxTF.getText());

				ArrayList<Reservation> res = model.getReservations("customer", min, max);
				if (res != null)
					list.setText(formatReservations(res));
				else
					JOptionPane.showMessageDialog(new JFrame(), 
							"An unexpected error has occurred. Please contact your system admin.", "Error", 
							JOptionPane.ERROR_MESSAGE);
			}
		});

		c.weighty = 0;
		JButton backBtn = new JButton("Back to main menu");
		backBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.clearComponents();
				view.switchPanel(model.getCurrentRole());
			}
		});
		panel.addComponent(backBtn, 0, 5);

		return panel;
	}

	private JPanel getUsersPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();

		c.gridwidth = 2;
		c.ipady = 30;
		panel.addLabel("Users", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.ipady = 0;
		c.insets = new Insets(10,10,10,10);
		panel.addLabel("To view all users do not enter a min #.", 12, "left", null, null, 0, 1);

		c.gridwidth = 1;
		panel.addLabel("Min # of Reservations (Only customers)", 12, "left", null, null, 0, 2);
		final JTextField numRes = new JTextField();
		panel.addComponent(numRes, 1, 2);

		c.gridwidth = 2;
		c.weighty = 0;
		JButton searchBtn = new JButton("Search for customers");
		searchBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.addComponent(searchBtn, 0, 3);

		c.weighty = 1;
		final JTextArea list = new JTextArea();
		list.setEditable(false);
		list.setWrapStyleWord(true);
		list.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(list,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		DefaultCaret caret = (DefaultCaret) list.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		panel.addComponent(scrollPane, 0, 4);
		panel.addComponent(list);

		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer num = null;

				try {
					if (!numRes.getText().equals(""))
						num = Integer.parseInt(numRes.getText());

					ArrayList<Account> users = model.getUsers(num);
					if (users != null)
						list.setText(formatUsers(users));
					else 
						JOptionPane.showMessageDialog(new JFrame(), 
								"An unexpected error has occurred. Please contact your system admin.", "Error", 
								JOptionPane.ERROR_MESSAGE);
				}
				catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(new JFrame(), 
							"Error: Invalid input(s).", "Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		c.weighty = 0;
		JButton backBtn = new JButton("Back to main menu");
		backBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.clearComponents();
				view.switchPanel(model.getCurrentRole());
			}
		});
		panel.addComponent(backBtn, 0, 5);

		return panel;
	}

	/**
	 * Get view of room service requested by Customer
	 * @return panel
	 */
	private JPanel getViewRoomServicePanel(){
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();
		c.weightx = 1;
		c.weighty = 0;

		/** need to retrieve info from database **/
		String userID = "USERID";
		String roomID= "ROOMID";
		String task = "TASK";

		panel.addLabel("View of Room Service (CUSTOMER)", 16, "center", null, null, 0, 0);
		panel.addLabel("UserID:  " + userID, 16, "left", null, null, 0, 1);

		panel.addLabel("RoomID:  " + roomID, 16, "left", null, null, 0, 2);
		panel.addLabel("Task: "  + task, 16, "left", null, null, 0, 3);

		panel.addNavigationButton("CHANGE", 16, "Room Service", 0, 6);
		panel.addNavigationButton("BACK", 16, "Customer", 1, 6);
		panel.addNavigationButton("CANCEL", 16,"Customer", 2,6);
		return panel;
	}

	private JPanel getFileComplaintPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();

		c.weighty = 1;
		c.ipady = 30;
		panel.addLabel("File a Complaint", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.ipady = 0;
		c.insets = new Insets(10,10,10,10);
		panel.addLabel("<html>We apologize for any inconvenience. "
				+ "<br>Please file your complaint here and a hotel manager "
				+ "will contact you as soon as possible.<html>", 12, "left", null, null, 0, 1);

		final JTextArea complaint = new JTextArea();
		complaint.setWrapStyleWord(true);
		complaint.setLineWrap(true);
		panel.addComponent(complaint, 0, 2);

		c.weighty = 0;
		panel.addNavigationButton("Back", 16, "Customer", 0, 3);

		JButton SumbitButton = new JButton("Sumbit");
		SumbitButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		SumbitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean validEntry = true;
				String complaintTest = complaint.getText();
				if (complaintTest.isEmpty() || complaintTest.length() < 1 || complaintTest.length() > 100) 
					validEntry = false;

				if (validEntry) {
					panel.clearComponents();
					if (model.addComplaint(model.getCurrentUser().getUsername(), complaintTest)) {	
						JOptionPane.showMessageDialog(new JFrame(), "Your complaint has been filed.", 
								"Result", JOptionPane.DEFAULT_OPTION);
						view.switchPanel(model.getCurrentRole());
					}
					else
						JOptionPane.showMessageDialog(new JFrame(), 
								"An unexpected error has occurred. Please contact your system admin.", "Error", 
								JOptionPane.ERROR_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(new JFrame(), "Error: Must be between 1 and 100 characters.", 
							"Error", JOptionPane.ERROR_MESSAGE);
			};
		});
		panel.addComponent(SumbitButton, 0, 4);

		return panel;
	}

	private JPanel getStatisticsPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();

		c.weighty = 1;
		c.ipady = 30;
		panel.addLabel("Statistics", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.ipady = 0;
		c.insets = new Insets(10,10,10,10);
		panel.addLabel("<html>Here are several commonly calculated statistics.<html>", 12, "left", null, null, 0, 1);

		final JTextArea stats = new JTextArea();
		stats.setWrapStyleWord(true);
		stats.setLineWrap(true);
		stats.setLineWrap(true);
		stats.setWrapStyleWord(true);
		stats.setOpaque(false);
		stats.setEditable(false);
		stats.setBorder(new EmptyBorder(10,10,2,2));
		panel.addComponent(stats, 0, 2);

		c.weighty = 0;
		c.gridx = 1;
		JButton backBtn = new JButton("Back to main menu");
		backBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.switchPanel("Manager");
			}
		});
		panel.addComponent(backBtn, 0, 3);

		model.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String output = model.getStatistics();
				if (output != null){
					stats.setText(output);
				}
				else {
					JOptionPane.showMessageDialog(new JFrame(), 
							"An unexpected error has occurred. Please contact your system admin.", "Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			};
		});

		return panel;
	}

	private JPanel getComplaintsPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();

		c.ipady = 30;
		c.gridwidth = 2;
		panel.addLabel("Customer Complaints", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.weightx = 0;
		c.ipady = 0;
		c.gridwidth = 1;
		c.insets = new Insets(10,10,10,10);
		panel.addLabel("Enter a complaint ID and solution to resolve the complaint.", 12, "left", null, null, 1, 1);

		panel.addLabel("Complaint ID: ", 12, "left", null, null, 1, 2);
		panel.addLabel("Solution (Between 1 and 100 chars):", 12, "left", null, null, 1, 4);

		final JTextField idTF = new JTextField();
		panel.addComponent(idTF, 1, 3);

		final JTextArea solTA = new JTextArea();
		solTA.setWrapStyleWord(true);
		solTA.setLineWrap(true);
		panel.addComponent(solTA, 1, 5);

		JButton submitBtn = new JButton("Resolve Complaint");
		submitBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.addComponent(submitBtn, 1, 6);

		JButton backBtn = new JButton("Back to main menu");
		backBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.switchPanel(model.getCurrentRole());
			}
		});
		panel.addComponent(backBtn, 0, 6);

		c.gridheight = 5;
		final JTextArea list = new JTextArea();
		list.setWrapStyleWord(true);
		list.setLineWrap(true);
		list.setEditable(false);
		panel.addComponent(list);
		JScrollPane listScroller = new JScrollPane(list);
		panel.addComponent(listScroller, 0, 1);

		model.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String output = "";
				ArrayList<Complaint> complaints = model.getComplaints();
				if (complaints != null){
					output += "Number of complaints: " + complaints.size();
					for (Complaint c : complaints)
						output += "\n\n" + c.toString();
					list.setText(output);
				}
				else {
					JOptionPane.showMessageDialog(new JFrame(), 
							"An unexpected error has occurred. Please contact your system admin.", "Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			};
		});

		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Integer id = null;
				String solution = solTA.getText();

				try {
					if (!idTF.getText().equals(""))
						id = Integer.parseInt(idTF.getText());

					Complaint c = model.getComplaint(id);
					if (c == null)
						JOptionPane.showMessageDialog(new JFrame(), 
								"Error: Complaint does not exist.", "Error", 
								JOptionPane.ERROR_MESSAGE);
					else if (solution.length() < 1 || solution.length() > 100)
						JOptionPane.showMessageDialog(new JFrame(), 
								"Error: Solution must be between 1 and 100 characters.", "Error", 
								JOptionPane.ERROR_MESSAGE);
					else {
						if (c.getResolvedBy() == null) {
							panel.clearComponents();
							model.updateComplaint(id, model.getCurrentUser().getUsername(), solution);
							JOptionPane.showMessageDialog(new JFrame(), "Complaint resolved", "Result", JOptionPane.DEFAULT_OPTION);
						}
						else {
							JOptionPane.showMessageDialog(new JFrame(), 
									"Error: The complaint has already been resolved.", "Error", 
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(new JFrame(), 
							"Error: Invalid input(s).", "Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			};
		});

		return panel;
	}

	private JPanel getRoomServicePanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();

		c.ipady = 30;
		panel.addLabel("Room Service Tasks", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.weightx = 0;
		c.ipady = 0;
		c.gridwidth = 1;
		c.insets = new Insets(10,10,10,10);
		panel.addLabel("Select a task", 12, "left", null, null, 0, 1);

		@SuppressWarnings("rawtypes")
		final JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		c.weighty = 1;
		panel.addComponent(listScroller, 0, 2);
		panel.addComponent(list);

		model.addChangeListener(new ChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void stateChanged(ChangeEvent e) {
				ArrayList<RoomService> current = model.getRoomService();
				if (current != null) 
					list.setListData(current.toArray());
				else {
					list.setListData(new RoomService[0]);
					JOptionPane.showMessageDialog(new JFrame(), 
							"An unexpected error has occurred. Please contact your system admin.", "Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton submitBtn = new JButton("Complete");
		submitBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!list.isSelectionEmpty()) {
					int response = JOptionPane.showConfirmDialog(new JFrame(),
							"Are you sure you want to complete this task?",
							"Confirmation", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.NO_OPTION) ;
					if (response == JOptionPane.YES_OPTION) {
						RoomService rs = (RoomService)list.getSelectedValue();
						if (!model.resolveTask(rs.getTaskId()))
							JOptionPane.showMessageDialog(new JFrame(), 
									"An unexpected error has occurred. Please contact your system admin.", "Error", 
									JOptionPane.ERROR_MESSAGE);
					}
				}
			};
		});
		panel.addComponent(submitBtn, 0, 3);

		panel.addNavigationButton("Back to main menu", 12, "Room Service", 0, 4);

		return panel;
	}

	private JPanel getCheckOutPanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();

		c.ipady = 30;
		panel.addLabel("Check Out", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.ipady = 0;
		c.gridwidth = 1;
		c.insets = new Insets(10,10,10,10);
		panel.addLabel("<html>Select a reservation to check out.<br>Note: The query has been fast forwarded 20 days</html>", 12, "left", null, null, 0, 1);

		@SuppressWarnings("rawtypes")
		final JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		c.weighty = 1;
		panel.addComponent(listScroller, 0, 2);
		panel.addComponent(list);

		model.addChangeListener(new ChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void stateChanged(ChangeEvent e) {
				ArrayList<Reservation> current = model.getCheckOut();
				if (current != null) 
					list.setListData(current.toArray());
				else list.setListData(new Reservation[0]);
			}
		});

		c.weighty = 0;
		JButton checkOutBtn = new JButton("Cancel");
		checkOutBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!list.isSelectionEmpty()) {
					int response = JOptionPane.showConfirmDialog(new JFrame(),
							"Are you sure you want to check out this reservation?",
							"Confirmation", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.NO_OPTION) ;
					if (response == JOptionPane.YES_OPTION) {
						if (!model.cancelReservation((Reservation) list.getSelectedValue()))
							JOptionPane.showMessageDialog(new JFrame(), 
									"An unexpected error has occurred. Please contact your system admin.", "Error", 
									JOptionPane.ERROR_MESSAGE);;
					}
				}
			}
		});
		panel.addComponent(checkOutBtn, 0, 3);

		JButton backBtn = new JButton("Back to main menu");
		backBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.switchPanel("Receptionist");
			}
		});
		panel.addComponent(backBtn, 0, 4);
		return panel;
	}

	private GregorianCalendar isValidDateFormat(String input) {
		try {
			dateFormatter.setLenient(false);
			GregorianCalendar cal = new GregorianCalendar();
			Date d = dateFormatter.parse(input);
			cal.setTime(d);

			return cal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private int checkDaysBetween(GregorianCalendar checkIn, GregorianCalendar checkOut) {
		GregorianCalendar temp = (GregorianCalendar) checkIn.clone();
		int count = 0;
		while (!temp.equals(checkOut)) {
			temp.add(Calendar.DATE, 1);
			count++;
		}
		return count;
	}

	private String formatReservations(ArrayList<Reservation> res) {
		String result = "Total reservations: " + res.size();
		if (!res.isEmpty()) {
			for (Reservation r : res) {
				String in = dateFormatter.format(r.getStartDate());
				String out = dateFormatter.format(r.getEndDate());

				result += "\n\nReservation # " + r.getReservationId()
				+ "\nUsername: " + r.getCustomer()
				+ "\nRoom: " + r.getRoom().getRoomType()
				+ "\nStart: " + in
				+ "\nEnd: " + out
				+ "\nCost: $" + Double.toString(r.getTotalCost());

				if (r.getCanceled())
					result += "\nThis reservation has been canceled";
			}
		}
		return result;
	}

	private String formatUsers(ArrayList<Account> users) {
		String result = "Total users: " + users.size();
		if (!users.isEmpty()) {
			for (Account a : users) {
				result += "\n\nUsername: " + a.getUsername()
				+ "\nName: " + a.getFirstName() + " " + a.getLastName()
				+ "\nUser role: " + a.getRole();
				if (a.getRole().equals("Customer")) {
					int count = 0;
					for (Reservation r : a.getReservations())
						if (!r.getCanceled()) count++;
					result += "\nNumber of Reservations: " + count;
				}
			}
		}
		return result;
	}

	private JPanel getOrderRoomServicePanel() {
		final BasicPanel panel = new BasicPanel(this);
		GridBagConstraints c = panel.getConstraints();

		c.ipady = 30;
		c.gridwidth = 2;
		panel.addLabel("Order Room Service", 24, "center", Color.white, new Color(0, 0, 128), 0, 0);

		c.ipady = 0;
		c.insets = new Insets(10,10,10,10);
		panel.addLabel("<html>Select reservation and choose room service.<br>"
				+ "Note: For testing purposes, comparison date to get "
				+ "reservations is 20 days from today.</html>", 12, "left", null, null, 0, 1);

		@SuppressWarnings("rawtypes")
		final JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		c.weighty = 1;
		panel.addComponent(listScroller, 0, 2);
		panel.addComponent(list);

		c.gridheight = 1;
		List<String> Item = new ArrayList<String>();
		Item.add("Select a meal");
		Item.add("Breakfast");
		Item.add("Lunch");
		Item.add("Dinner");
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final JComboBox ServiceComboBox = new JComboBox(Item.toArray());
		panel.addComponent(ServiceComboBox, 0, 3);

		c.gridwidth = 1;
		JButton submitBtn = new JButton("Order");
		submitBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String service = (String)ServiceComboBox.getSelectedItem();
				double cost = 0;
				if (service.equals("Breakfast"))
					cost = 10;
				else if (service.equals("Lunch"))
					cost = 15;
				else if (service.equals("Dinner"))
					cost = 20;
				else 
					JOptionPane.showMessageDialog(new JFrame(), 
							"Error: Select a service.", "Error", 
							JOptionPane.ERROR_MESSAGE);

				if (!list.isSelectionEmpty() && cost != 0) {
					int response = JOptionPane.showConfirmDialog(new JFrame(),
							"Are you sure you want to order this service?",
							"Confirmation", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.NO_OPTION) ;
					if (response == JOptionPane.YES_OPTION) {
						if (!model.addRoomService((Reservation)list.getSelectedValue(), service, cost)) 
							JOptionPane.showMessageDialog(new JFrame(), 
									"An unexpected error has occurred. Please contact your system admin.", "Error", 
									JOptionPane.ERROR_MESSAGE);
						else
							JOptionPane.showMessageDialog(new JFrame(), 
									"Room service ordered", "Result", JOptionPane.DEFAULT_OPTION);
					}
				}

			}
		});
		panel.addComponent(submitBtn, 1, 4);

		model.addChangeListener(new ChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void stateChanged(ChangeEvent e) {
				ArrayList<Reservation> notCanceled = new ArrayList<Reservation>();
				if (model.getCurrentUser() != null) {
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DATE, 20);
					Date compDate = c.getTime();
					for (Reservation r : model.getCurrentUser().getReservations()) {
						if (!r.getCanceled() && (compDate.after(r.getStartDate()) && compDate.before(r.getEndDate())))							
							notCanceled.add(r);
					}
					list.setListData(notCanceled.toArray());
				}
				else list.setListData(new Reservation[0]);
			}
		});

		JButton backBtn = new JButton("Back to main menu");
		backBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.switchPanel("Customer");
			}
		});
		panel.addComponent(backBtn, 0, 4);

		return panel;
	}
}