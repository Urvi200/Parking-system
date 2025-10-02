import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;

class Vehicle {
    String ownerName;
    String mobileNumber;
    String vehicleNumber;
    String vehicleType;
    int fee;

    public Vehicle(String ownerName, String mobileNumber, String vehicleNumber, String vehicleType) {
        this.ownerName = ownerName;
        this.mobileNumber = mobileNumber;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        if(vehicleType.equalsIgnoreCase("Bike")) fee = 20;
        else if(vehicleType.equalsIgnoreCase("Car")) fee = 50;
        else if(vehicleType.equalsIgnoreCase("Cycle")) fee = 5;
        else fee = 0;
    }
}

public class ParkingSystemHTML extends JFrame {

    JTextField nameField, mobileField, vehicleNumberField;
    JComboBox<String> vehicleTypeBox;
    JPanel ticketPanel;
    JButton generateButton, printButton;

    static int bikeCount = 0;
    static int carCount = 0;
    static int cycleCount = 0;

    static final int MAX_BIKE = 800;
    static final int MAX_CAR = 500;
    static final int MAX_CYCLE = 1000;

    public ParkingSystemHTML() {
        setTitle("Parking Ticket System");
        setSize(600, 700);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Parking Ticket System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(50, 10, 500, 30);
        add(title);

        JLabel nameLabel = new JLabel("Owner Name:");
        nameLabel.setBounds(50, 60, 120, 25);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(180, 60, 350, 25);
        add(nameField);

        JLabel mobileLabel = new JLabel("Mobile Number:");
        mobileLabel.setBounds(50, 100, 120, 25);
        add(mobileLabel);
        mobileField = new JTextField();
        mobileField.setBounds(180, 100, 350, 25);
        add(mobileField);

        JLabel vehicleLabel = new JLabel("Vehicle Number:");
        vehicleLabel.setBounds(50, 140, 120, 25);
        add(vehicleLabel);
        vehicleNumberField = new JTextField();
        vehicleNumberField.setBounds(180, 140, 350, 25);
        add(vehicleNumberField);

        JLabel typeLabel = new JLabel("Vehicle Type:");
        typeLabel.setBounds(50, 180, 120, 25);
        add(typeLabel);
        String[] types = {"Bike", "Car", "Cycle"};
        vehicleTypeBox = new JComboBox<>(types);
        vehicleTypeBox.setBounds(180, 180, 350, 25);
        add(vehicleTypeBox);

        generateButton = new JButton("Generate Ticket");
        generateButton.setBounds(50, 220, 180, 30);
        add(generateButton);

        printButton = new JButton("Print Ticket");
        printButton.setBounds(250, 220, 180, 30);
        add(printButton);

        ticketPanel = new JPanel();
        ticketPanel.setBounds(50, 270, 480, 350);
        ticketPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        ticketPanel.setBackground(new Color(255, 255, 230)); // light yellow
        ticketPanel.setLayout(new BorderLayout());
        add(ticketPanel);

        generateButton.addActionListener(e -> generateTicket());
        printButton.addActionListener(e -> printTicket());

        setVisible(true);
    }

    private void generateTicket() {
        String name = nameField.getText();
        String mobile = mobileField.getText();
        String vehicleNo = vehicleNumberField.getText();
        String type = vehicleTypeBox.getSelectedItem().toString();

        if(name.isEmpty() || mobile.isEmpty() || vehicleNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        // Capacity check
        if(type.equalsIgnoreCase("Bike") && bikeCount >= MAX_BIKE) {
            JOptionPane.showMessageDialog(this, "Bike parking full! Max 800 allowed.");
            return;
        }
        if(type.equalsIgnoreCase("Car") && carCount >= MAX_CAR) {
            JOptionPane.showMessageDialog(this, "Car parking full! Max 500 allowed.");
            return;
        }
        if(type.equalsIgnoreCase("Cycle") && cycleCount >= MAX_CYCLE) {
            JOptionPane.showMessageDialog(this, "Cycle parking full! Max 1000 allowed.");
            return;
        }

        Vehicle vehicle = new Vehicle(name, mobile, vehicleNo, type);

        if(type.equalsIgnoreCase("Bike")) bikeCount++;
        else if(type.equalsIgnoreCase("Car")) carCount++;
        else if(type.equalsIgnoreCase("Cycle")) cycleCount++;

        // HTML Styled Ticket
        String ticketHTML = "<html><body style='font-family:monospace; color:#2e2e2e;'>"
                + "<div style='text-align:center; border-bottom:2px solid black; padding-bottom:5px;'>"
                + "<h2 style='color:#1a73e8;'>PARKING TICKET</h2>"
                + "</div>"
                + "<p><b>Parking Address:</b> RAM NAGER COLONY, MINRANPUR, KATRA</p>"
                + "<p><b>Owner Name:</b> " + vehicle.ownerName + "</p>"
                + "<p><b>Mobile Number:</b> " + vehicle.mobileNumber + "</p>"
                + "<p><b>Vehicle Number:</b> " + vehicle.vehicleNumber + "</p>"
                + "<p><b>Vehicle Type:</b> " + vehicle.vehicleType + "</p>"
                + "<p><b>Fee (12 hours):</b> ₹" + vehicle.fee + "</p>"
                + "<div style='text-align:center; margin-top:10px; border-top:2px solid black; padding-top:5px;'>"
                + "<h4 style='color:green;'>THANK YOU! VISIT AGAIN</h4>"
                + "</div>"
                + "</body></html>";

        ticketPanel.removeAll();
        JLabel ticketLabel = new JLabel(ticketHTML);
        ticketLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ticketPanel.add(ticketLabel, BorderLayout.CENTER);
        ticketPanel.revalidate();
        ticketPanel.repaint();
    }

    private void printTicket() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Parking Ticket Print");

        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if(pageIndex > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            ticketPanel.printAll(g2d);
            return Printable.PAGE_EXISTS;
        });

        try {
            if(job.printDialog()) job.print();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Printing Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ParkingSystemHTML();
    }
}
