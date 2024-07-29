package compile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.*;

public class LoginTest {
    private static final String USER_DATA_FILE = "user_data.txt";
    private static ArrayList<String> users = new ArrayList<>();
    private static ArrayList<User> usersList = new ArrayList<>();    
    private static int currentUserIndex;
    private static User user;
    private static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        
        
        
        //Login 
    	user = new User("", "", "", "");
        boolean login = false;
        String username;
        
        do{
        	loadUserData();
        	DateTime();
        	System.out.println("\t\t\tLogin");
			System.out.println("---------------------------------------------------------------");
            System.out.print("Username ('new' to create a new user): ");
            username = input.nextLine();

            if ("new".equalsIgnoreCase(username)) {
                createNewUser(user,input);
                login = true;
            }
            else if (isValidUser(username)) {
                System.out.print("Enter your password: ");
                String password = input.nextLine();
                if (isValidPassword(username, password)) {
                    System.out.println("Login successful!\n"); 
                    user.setUsername(username);
                    login = true;
                }
                else  
                    System.out.println("Invalid password. Try again.\n");
                }
            else {
                System.out.println("Invalid username. Please enter a valid username or 'new' to create a new user.\n");
            }
        }while (login != true);
        
        //Options display
        char option;
		do {
			DateTime();
			System.out.println("Welcome " + user.getUsername());
			System.out.println("<1> Add appointment");
			System.out.println("<2> Cancel apppointment");
			System.out.println("<3> Upcoming appointment");
			System.out.println("<4> Feedback");
			System.out.println("<5> Appointment history");
			System.out.println("<6> Edit profile");
			System.out.println("<7> Exit");
			System.out.println("-------------------------------------------");
			System.out.print("Enter an option \t>> ");
			option = input.next().charAt(0);
		
			switch(option)
			{
			case '1': AddAppointment(); break;
			case '2': CancelAppointment(); break;
			case '3': UpcomingAppointment(); break;
			case '4': Feedbacks(username); break;
			case '5': AppointmentHistory(); break;
			case '6': EditProfile(); break;
			case '7': System.out.println("Exiting..."); break;
			default: System.out.println("Invalid option. Enter option again. "); break;
			}
			System.out.println();
		}while(option != '7');
		input.close();
    }
    
    private static void EditProfile() {
        boolean valid;
        String username = user.getUsername();
        loadUserData();
        usersList = loadusersList();
        currentUserIndex = loadcurrentUserIndex(username);
        valid = false;

        // Create a sample user
        User _user = usersList.get(currentUserIndex);

        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // Display the current user profile information
        System.out.println("Current User Profile Information:");
        System.out.println("To remain existing information please press enter.");
        System.out.println("Username: " + _user.getUsername());
        System.out.println("Email: " + _user.getEmail());
        System.out.println("Phone number: " + _user.getPhoneNo());

        // Prompt the user to update their profile information
        System.out.println("\nUpdate User Profile Information:");
        input.nextLine();
        System.out.print("Enter new Username: ");
        String newUsername = input.nextLine();

        if (!newUsername.equals("")) {
            // Update the username in user_data.txt
            users.set(currentUserIndex, newUsername + ";" + _user.getPassword() + ";" + _user.getEmail() + ";" + _user.getPhoneNo());

            // Update the username in user_appointment.txt
            if (!newUsername.equals("") && !newUsername.equals(username)) {
                if (updateFeedbacksUsername(username, newUsername)) {
                    System.out.println("Feedback records updated with the new username.");
                } else {
                    System.out.println("Failed to update feedback records with the new username.");
                }

                if (updateAppointmentsUsername(username, newUsername)) {
                    System.out.println("Appointment records updated with the new username.");
                } else {
                    System.out.println("Failed to update appointment records with the new username.");
                }
                
            // Update username in feedback.txt
            if (!newUsername.equals("") && !newUsername.equals(username)) {
                if (updateFeedbacksUsername(username, newUsername)) {
                    System.out.println("Feedback records updated with the new username.");
                } else {
                    System.out.println("Failed to update feedback records with the new username.");
                }
            }

            // Set the new username in the User object
            _user.setUsername(newUsername);
            user.setUsername(newUsername); // Update the user object with the new username

            // Update the username in feedback.txt
            updateFeedbacksUsername(username, newUsername);
            // Update for appointment.txt
            updateAppointmentsUsername(username, newUsername);

            System.out.println("Username updated successfully.");
        }

        System.out.print("Enter new password: ");
        String newPassword = input.nextLine();
        if (!newPassword.equals("")) {
            _user.setPassword(newPassword);
            System.out.println("Password updated successfully.");
        }

        valid = true;
        while (valid) {
            System.out.print("Enter new email: ");
            String newEmail = input.nextLine();

            // Regular Expression
            String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
            // Compile regular expression to get the pattern
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(newEmail);
            if (matcher.matches() || newEmail.equals("")) {
                valid = false;
                if (!newEmail.equals("")) {
                    _user.setEmail(newEmail);
                    System.out.println("Email updated successfully.");
                }
            } else {
                System.out.print("Wrong email format. Please re-enter the email.\n");
            }
        }

        valid = true;
        while (valid) {
            System.out.print("Enter new Phone number (***-{7/8 digits}): ");
            String newPhoneNo = input.nextLine();

            // Regular Expression
            String regex = "^[0-9]{10,11}$";
            // Compile regular expression to get the pattern
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(newPhoneNo);
            if (matcher.matches() || newPhoneNo.equals("")) {
                valid = false;
                if (!newPhoneNo.equals("")) {
                    _user.setPhoneNo(newPhoneNo);
                    System.out.println("Phone number updated successfully.");
                }
            } else {
                System.out.print("Wrong Phone number format. Please re-enter the Phone number.\n");
            }
        }

        // Display the updated user profile information
        System.out.println("\nUpdated User Profile Information:");
        System.out.println("Username: " + _user.getUsername());
        System.out.println("Password: " + _user.getPassword());
        System.out.println("Email: " + _user.getEmail());
        System.out.println("Phone number: " + _user.getPhoneNo());

        // Update the user data in user_data.txt
        writeUserData();

        // Ask the user to press Enter to continue
        System.out.println("Please press Enter to continue...");
        input.nextLine();
        input.nextLine(); // Wait for Enter key
    }}

    private static boolean updateAppointmentsUsername(String username, String newUsername) {
        // Load the content of the user_appointment.txt file into a List
        List<String> appointmentLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("user_appointment.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                appointmentLines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading user_appointment.txt: " + e.getMessage());
            return false; // Return false to indicate failure.
        }

        // Update the usernames in the List
        boolean updated = false;
        for (int i = 0; i < appointmentLines.size(); i++) {
            String[] parts = appointmentLines.get(i).split(";");
            if (parts.length == 3 && parts[0].equals(username)) {
                appointmentLines.set(i, newUsername + ";" + parts[1] + ";" + parts[2]);
                updated = true;
            }
        }

        // Write the updated List back to the user_appointment.txt file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_appointment.txt"))) {
            for (String line : appointmentLines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
            return updated; // Return true if any record was updated.
        } catch (IOException e) {
            System.err.println("Error updating user_appointment.txt: " + e.getMessage());
            return false; // Return false to indicate failure.
        }
    }
    
    private static void AppointmentHistory() {
        // Load the appointment data from the file
        List<Appointment> appointments = loadAppointmentsFromFile("user_appointment.txt");

        String username = user.getUsername();

        // Filter appointments to show only the user's appointments
        List<Appointment> userAppointments = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Appointment appointment : appointments) {
            if (username.equals(appointment.getUsername())) {
                userAppointments.add(appointment);
            }
        }

        // Display 
        if (!userAppointments.isEmpty()) {
            System.out.println("Appointment History for " + username + ":");
            for (Appointment appointment : userAppointments) {
                String formattedDateTime = dateFormat.format(appointment.getAppointmentDate());
                String counselorName = appointment.getCounselorName();
                System.out.println("Date/Time: " + formattedDateTime + ", Counselor Name: " + counselorName);
            }
        } else {
            System.out.println("No appointment history found for " + username + ".");
        }

        // Ask the user to press Enter to continue
        System.out.println("Please press enter to return to the menu...");
        input.nextLine();
        input.nextLine(); 
    }
    
    
    private static boolean updateFeedbacksUsername(String username, String newUsername) {
        List<Feedback> feedbacks = loadFeedbackFromFile("feedback.txt");
        boolean updated = false;

        for (Feedback feedback : feedbacks) {
            if (username.equals(feedback.getUsername())) {
                feedback.setUsername(newUsername);
                updated = true;
            }
        }

        // Write the updated feedbacks back to the file
        if (writeFeedbacksToFile(feedbacks, "feedback.txt")) {
            return updated;
        } else {
            // If there was an error writing to the file, you might want to handle it accordingly
            return false;
        }
    }
    
    private static boolean writeFeedbacksToFile(List<Feedback> feedbacks, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Feedback feedback : feedbacks) {
                writer.write(feedback.getFileString());
                writer.newLine();
            }
            writer.flush();
            return true; // Return true to indicate success
        } catch (IOException e) {
            System.err.println("Error writing feedback data: " + e.getMessage());
            return false; // Return false to indicate failure
        }}
        
    private static void Feedbacks(String username) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Feedback Menu:");
        System.out.println("1. Send Feedback");
        System.out.println("2. Display Feedback from all users");
        System.out.print("Choose an option (1/2): ");
        int feedbackOption = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (feedbackOption) {
            case 1:
                SendFeedback();
                break;
            case 2:
                DisplayFeedbacks(username);
                break;
            default:
                System.out.println("Invalid option. Please choose a valid option.");
                break;
        }
    }

    private static void SendFeedback() {
        // Load the list of available counselors from a text file
        List<String> counselors = loadCounselorsFromFile("counselors.txt");

        if (counselors.isEmpty()) {
            System.out.println("No counselors available. Please add counselors before sending feedback.");
        } else {
            Scanner scanner = new Scanner(System.in);

            // Display the list of available counselors
            System.out.println("Available Counselors:");
            for (int i = 0; i < counselors.size(); i++) {
                System.out.println((i + 1) + ": " + counselors.get(i));
            }

            int counselorNumber;
            boolean validCounselorNumber = false;

            while (!validCounselorNumber) {
                // Ask the user to choose a counselor
                System.out.print("Enter the number of the counselor you want to send feedback to: ");
                counselorNumber = scanner.nextInt();

                if (counselorNumber >= 1 && counselorNumber <= counselors.size()) {
                    validCounselorNumber = true; // Valid counselor number entered
                    String counselorName = counselors.get(counselorNumber - 1);

                    // Ask the user for feedback text
                    scanner.nextLine(); // Consume the newline character
                    System.out.print("Enter your feedback for " + counselorName + ": ");
                    String feedbackText = scanner.nextLine();

                    // Save the feedback to the feedback.txt file
                    saveFeedbackToFile(user.getUsername(), counselorName, feedbackText);

                    System.out.println("Feedback submitted successfully.");
                } else {
                    System.out.println("Invalid counselor number. Please enter a valid number.");
                }
            }
        }
    }
    	
    private static void DisplayFeedbacks(String username) {
        // Load the list of available counselors from feedback data in the feedback.txt file
        Set<String> counselorNames = loadCounselorNamesFromFeedback("feedback.txt");

        if (counselorNames.isEmpty()) {
            System.out.println("No counselors available. Please add feedback before viewing feedback.");
        } else {
            Scanner scanner = new Scanner(System.in);

            // Display the list of available counselors and let the user choose one
            System.out.println("Available Counselors:");
            int counselorNumber = 1;
            for (String counselorName : counselorNames) {
                System.out.println(counselorNumber + ": " + counselorName);
                counselorNumber++;
            }

            int selectedCounselorIndex;
            boolean validCounselorNumber = false;

            while (!validCounselorNumber) {
                // Ask the user to choose a counselor
                System.out.print("Enter the number of the counselor whose feedback you want to view (0 to cancel): ");
                selectedCounselorIndex = scanner.nextInt();

                if (selectedCounselorIndex == 0) {
                    return; // Cancel the operation
                } else if (selectedCounselorIndex >= 1 && selectedCounselorIndex <= counselorNames.size()) {
                    validCounselorNumber = true; // Valid counselor number entered

                    // Get the selected counselor's name
                    String[] counselorNamesArray = counselorNames.toArray(new String[0]);
                    String selectedCounselorName = counselorNamesArray[selectedCounselorIndex - 1];

                    // Display feedback for the selected counselor
                    displayFeedbackForCounselor(selectedCounselorName);
                } else {
                    System.out.println("Invalid counselor number. Please enter a valid number.");
                }
            }
        }
    }


    private static void displayFeedbackForCounselor(String counselorName) {
        // Load feedback data from the feedback.txt file
        List<Feedback> feedbacks = loadFeedbackFromFile("feedback.txt");

        // Check if there is feedback for the selected counselor
        boolean hasFeedback = false;

        System.out.println("Feedback for " + counselorName + ":");

        int feedbackNumber = 1;

        for (Feedback feedback : feedbacks) {
            if (counselorName.equals(feedback.getCounselorName())) {
                System.out.println(feedbackNumber + ". Feedback from " + feedback.getUsername() + " to " + counselorName + ":");
                System.out.println("   " + feedback.getFeedbackText());
                System.out.println();
                hasFeedback = true;
                feedbackNumber++;
            }
        }

        // If no feedback was found, display a message
        if (!hasFeedback) {
            System.out.println("Counselor: " + counselorName + " has no feedback available.");
        }
    }

    private static Set<String> loadCounselorNamesFromFeedback(String filename) {
        Set<String> counselorNames = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String counselorName = parts[1].trim();
                    counselorNames.add(counselorName);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading feedback data: " + e.getMessage());
        }

        return counselorNames;
    }
    
    private static List<Feedback> loadFeedbackFromFile(String filename) {
        List<Feedback> feedbacks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String username = parts[0].trim();
                    String counselorName = parts[1].trim();
                    String feedbackText = parts[2].trim();

                    feedbacks.add(new Feedback(username, counselorName, feedbackText));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading feedback data: " + e.getMessage());
        }

        return feedbacks;
    }

    private static void saveFeedbackToFile(String username, String counselorName, String feedbackText) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("feedback.txt", true))) {
            String entry = username + ";" + counselorName + ";" + feedbackText;
            writer.write(entry);
            writer.newLine();
            System.out.println("Feedback data saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving feedback data: " + e.getMessage());
        }
    }
    
    
    private static void UpcomingAppointment() {
        // Get the user's username
        String username = user.getUsername();

        // Load the appointment data from the file
        List<Appointment> appointments = loadAppointmentsFromFile("user_appointment.txt");

        // Get the current system date and time
        Date currentDateTime = new Date();

        // Display the user's upcoming appointments
        boolean hasUpcomingAppointments = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Appointment appointment : appointments) {
            if (username.equals(appointment.getUsername()) && currentDateTime.before(appointment.getAppointmentDate())) {
                if (!hasUpcomingAppointments) {
                    System.out.println("Upcoming Appointments for " + username + ":");
                    hasUpcomingAppointments = true;
                }
                String formattedDateTime = dateFormat.format(appointment.getAppointmentDate());
                System.out.println("Date/Time: " + formattedDateTime + "; Counselor Name: " + appointment.getCounselorName());
            }
        }

        if (!hasUpcomingAppointments) {
            System.out.println("You have no upcoming appointments.");
        }

        // Ask the user to press Enter to return to the menu
        System.out.println("Please press enter to return to the menu...");
        input.nextLine();
        input.nextLine(); // Wait for Enter key
    }
	
	private static void CancelAppointment() {
	    // Get the user's username
	    String username = user.getUsername();

	    // Load the appointment data from the file
	    List<Appointment> appointments = loadAppointmentsFromFile("user_appointment.txt");

	    // Filter appointments to show only the user's upcoming appointments
	    List<Appointment> userAppointments = new ArrayList<>();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	    for (Appointment appointment : appointments) {
	        if (username.equals(appointment.getUsername())) {
	            userAppointments.add(appointment);
	        }
	    }

	    // Display the user's appointments
	    if (!userAppointments.isEmpty()) {
	        System.out.println("Appointments be made for " + username + ":");
	        for (int i = 0; i < userAppointments.size(); i++) {
	            Appointment appointment = userAppointments.get(i);
	            System.out.println((i + 1) + ": " + dateFormat.format(appointment.getAppointmentDate()) + " with Counselor: " + appointment.getCounselorName());
	        }

	        while (true) {
	            // Ask the user to choose which appointment to cancel
	            System.out.print("Enter the number of the appointment to cancel (or 0 to cancel nothing): ");
	            int cancelNumber = input.nextInt();

	            if (cancelNumber > 0 && cancelNumber <= userAppointments.size()) {
	                // Calculate the index to remove
	                int cancelIndex = cancelNumber - 1;
	                Appointment canceledAppointment = userAppointments.remove(cancelIndex);
	                System.out.println("Appointment with " + canceledAppointment.getCounselorName() + " on " + dateFormat.format(canceledAppointment.getAppointmentDate()) + " canceled.");
	                appointments.remove(canceledAppointment); // Remove the appointment from the list
	                saveAppointmentsToFile(appointments); // Save the updated appointments
	                break; // Exit the loop once a valid appointment is canceled
	            } else if (cancelNumber == 0) {
	                System.out.println("No appointment canceled.");
	                break; // Exit the loop if the user chooses not to cancel anything
	            } else {
	                System.out.println("Invalid appointment number. No appointment canceled. Please re-enter a valid number.");
	            }
	        }
	    } else {
	        System.out.println("You have no upcoming appointments to cancel.");
	    }
	 // Ask the user to press Enter to return to the menu
        System.out.println("Please press enter to return to the menu...");
        input.nextLine();
        input.nextLine();// Wait for Enter key
	}
	private static List<Appointment> loadAppointmentsFromFile(String filename) {
	    List<Appointment> appointments = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(";");
	            if (parts.length == 3) {
	                String storedUsername = parts[0].trim();
	                String dateStr = parts[1].trim();
	                String counselorName = parts[2].trim();

	                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	                Date date = dateFormat.parse(dateStr);

	                appointments.add(new Appointment(storedUsername, date, counselorName));
	            }
	        }
	    } catch (IOException | ParseException e) {
	        System.err.println("Error loading appointment data: " + e.getMessage());
	    }

	    return appointments;
	}

	private static void saveAppointmentsToFile(List<Appointment> appointments) {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_appointment.txt"))) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        for (Appointment appointment : appointments) {
	            String entry = appointment.getUsername() + ";" + dateFormat.format(appointment.getAppointmentDate()) + ";" + appointment.getCounselorName();
	            writer.write(entry);
	            writer.newLine();
	        }
	        System.out.println("Appointments data saved to file.");
	    } catch (IOException e) {
	        System.err.println("Error saving appointments data: " + e.getMessage());
	    }
	}
	
	private static void AddAppointment() {
	    // Get the user's username
	    String username = user.getUsername();

	    // Load the list of available counselors from a text file
	    List<String> counselors = loadCounselorsFromFile("counselors.txt");

	    if (counselors.isEmpty()) {
	        System.out.println("No counselors available.");
	    } else {
	        // Create a Scanner object for user input
	        Scanner scanner = new Scanner(System.in);

	        // Display the list of available counselors
	        System.out.println("Available Counselors:");
	        for (int i = 0; i < counselors.size(); i++) {
	            System.out.println((i + 1) + ": " + counselors.get(i));
	        }

	        int counselorNumber;
	        boolean validCounselorNumber = false;

	        while (!validCounselorNumber) {
	            // Ask the user to choose a counselor
	            System.out.print("Enter the number of the counselor you want to book: ");
	            counselorNumber = scanner.nextInt();

	            if (counselorNumber >= 1 && counselorNumber <= counselors.size()) {
	                validCounselorNumber = true; // Valid counselor number entered
	                String counselorName = counselors.get(counselorNumber - 1);

	                // Ask the user for the date and time of the appointment
	                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	                System.out.print("Enter the date and time for the appointment (yyyy-MM-dd HH:mm): ");
	                scanner.nextLine();
	                String dateTimeStr = scanner.nextLine();

	                try {
	                    Date appointmentDate = dateFormat.parse(dateTimeStr);

	                    // Create a new appointment and add it to the list
	                    Appointment appointment = new Appointment(username, appointmentDate, counselorName);
	                    List<Appointment> appointments = loadAppointmentsFromFile("user_appointment.txt");
	                    appointments.add(appointment);
	                    saveAppointmentsToFile(appointments);
	                    System.out.println("Appointment with " + counselorName + " on " + dateFormat.format(appointmentDate) + " created.");
	                    
	                } catch (ParseException e) {
	                    System.out.println("Invalid date and time format. Appointment not created.");
	                }
	            } else {
	                System.out.println("Invalid counselor number. Please re-enter a valid counselor number.");
	            }
	        }
	    }
	}
	
	private static List<String> loadCounselorsFromFile(String filename) {
	    List<String> counselors = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            counselors.add(line.trim());
	        }
	    } catch (IOException e) {
	        System.err.println("Error loading counselors data: " + e.getMessage());
	    }

	    return counselors;
	}
            
    private static void loadUserData() {
    	users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                users.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading user data file: " + e.getMessage());
        }
    }
    
	private static boolean isValidEmail(String email,String pattern) {
		Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(email);
        return matcher.matches();
	    }
    
    private static boolean isValidContact(String contact, String pattern) {
    	Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(contact);
        return matcher.matches();
    }
    
    private static void createNewUser(User user, Scanner input) {
    	String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        String phonePattern = "^[0-9]{10,11}$";
        String newUsername, newPassword, newEmail, newContact;
        boolean valid1 = false , valid2 = false;
        System.out.print("Enter a new username: ");
	    newUsername = input.nextLine();
	    System.out.print("Enter a password: ");
	    newPassword = input.nextLine();
	    
	    do {    
	        System.out.print("Enter email address: ");
	        newEmail = input.nextLine();
	        if (isValidEmail(newEmail, emailPattern)) //Check for valid email address
	        	valid1 = true;
	        else 
	            System.out.println("Email is invalid.");
	    }while(valid1 !=true);
	    do {
	        System.out.print("Enter contact number: ");
	        newContact = input.nextLine();
	        if (isValidContact(newContact, phonePattern)) //Check for valid contact number
	        	valid2 = true; 
	        else 
	            System.out.println("Phone number is invalid.");	        
        }while(valid2 != true);
        String newUserEntry = newUsername + ";" + newPassword + ";" + newEmail + ";" + newContact;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE, true))) {
            writer.write(newUserEntry);
            writer.newLine();
            users.add(newUserEntry);
            System.out.println("User created successfully.");
        } catch (IOException e) {
            System.err.println("Error creating a new user: " + e.getMessage());
        }
        user.setUsername(newUsername);
    }
    
    private static boolean isValidUser(String username) {
        for (String user : users) {
            String[] parts = user.split(";");
            if (parts.length == 4 && parts[0].equals(username)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidPassword(String username, String password) {
        for (String user : users) {
            String[] parts = user.split(";");
            if (parts.length == 4 && parts[0].equals(username) && parts[1].equals(password)) {
                return true;
            }
        }
        return false;
    }
    
    public static void DateTime()
	{
		System.out.println("Date: \t\t\t\t\t Time:");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(("dd/MM/yyyy \t\t\t\t HH:mm:ss"));
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));
		System.out.println("---------------------------------------------------------------");
	}
    private static ArrayList<User> loadusersList() {
		ArrayList<User> ul= new ArrayList<User>();
		for (int i=0;i<users.size();i++) {
			String[] userSplit=users.get(i).split(";");
			User user = new User(userSplit[0],userSplit[1],userSplit[2],userSplit[3]);
			ul.add(user);
		}
       return ul;
    }
    private static int loadcurrentUserIndex(String username) {
		for (int i=0;i<usersList.size();i++) {
			if(usersList.get(i).getUsername().equals(username)){
				return i;
			}
		}
       return -1; //throw Error
    }
    private static void writeUserData() {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE, false))) {
    		for (int i=0;i<users.size();i++) {
    			if (i==currentUserIndex) {
    				writer.write(usersList.get(i).getFileString());
    			}else {
    				writer.write(users.get(i));
    			}
	            writer.newLine();
    		}
            System.out.println("User updated successfully.");
        } catch (IOException e) {
            System.err.println("Error updating a user: " + e.getMessage());
        }
    }
    
}
	    
	    
 
