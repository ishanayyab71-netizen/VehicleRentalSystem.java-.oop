import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class VehicleRentalSystem extends JFrame {

    // ================= OOP CONCEPT: ABSTRACTION & ENCAPSULATION =================
    abstract static class Vehicle {
        private int id;
        private String brand;
        private double rentPerDay;
        private boolean available = true;

        Vehicle(int id, String brand, double rentPerDay) {
            this.id = id; this.brand = brand; this.rentPerDay = rentPerDay;
        }

        public int getId() { return id; }
        public String getBrand() { return brand; }
        public double getRentPerDay() { return rentPerDay; }
        public boolean isAvailable() { return available; }
        public void setBrand(String b) { this.brand = b; }
        public void setRentPerDay(double r) { this.rentPerDay = r; }
        public void setAvailable(boolean a) { this.available = a; }

        abstract double calculateRent(int days);
        abstract String display();
        abstract String toFileString();
    }

    // ================= OOP CONCEPT: INHERITANCE & POLYMORPHISM (CAR) =================
    static class Car extends Vehicle {
        private int doors;
        Car(int id, String b, double r, int d) { super(id, b, r); this.doors = d; }

        @Override double calculateRent(int d) { return getRentPerDay() * d; }

        @Override
        String display() {
            return " 🚗  CAR   |  ID: " + String.format("%-6d", getId()) + " |  Brand: " + String.format("%-14s", getBrand()) + " |  Doors: " + String.format("%-4d", doors) + " |  Rent/Day: Rs " + String.format("%-9.2f", getRentPerDay()) + " |  Status: " + (isAvailable() ? "🟢 AVAILABLE" : "🔴 RENTED");
        }

        @Override String toFileString() { return "CAR," + getId() + "," + getBrand() + "," + getRentPerDay() + "," + doors + "," + isAvailable(); }
    }

    // ================= OOP CONCEPT: INHERITANCE & POLYMORPHISM (BIKE) =================
    static class Bike extends Vehicle {
        private int cc;
        Bike(int id, String b, double r, int cc) { super(id, b, r); this.cc = cc; }

        @Override double calculateRent(int d) { return getRentPerDay() * d; }

        @Override
        String display() {
            return " 🏍  BIKE  |  ID: " + String.format("%-6d", getId()) + " |  Brand: " + String.format("%-14s", getBrand()) + " |  Engine: " + String.format("%-7s", cc+" CC") + " |  Rent/Day: Rs " + String.format("%-9.2f", getRentPerDay()) + " |  Status: " + (isAvailable() ? "🟢 AVAILABLE" : "🔴 RENTED");
        }

        @Override String toFileString() { return "BIKE," + getId() + "," + getBrand() + "," + getRentPerDay() + "," + cc + "," + isAvailable(); }
    }

    static class Customer { String name; Customer(String n) { this.name = n; } }

    static class Rental {
        private Customer customer; private Vehicle vehicle; private int days;
        Rental(Customer c, Vehicle v, int d) { this.customer = c; this.vehicle = v; this.days = d; }

        public String generateBillReceipt() {
            return "\n"
                 + "   ========================================================\n"
                 + "                 VEHICLE RENTAL SYSTEM INVOICE             \n"
                 + "   ========================================================\n\n"
                 + "    👤  Customer Name    :  " + customer.name + "\n"
                 + "    🚘  Allocated Unit   :  " + vehicle.getBrand() + " (ID: " + vehicle.getId() + ")\n"
                 + "    ⏱  Lease Timeline    :  " + days + " Day(s)\n"
                 + "    💵  Daily Base Rent  :  Rs " + vehicle.getRentPerDay() + "\n"
                 + "   --------------------------------------------------------\n"
                 + "    🔥  TOTAL AMOUNT DUE :  Rs " + vehicle.calculateRent(days) + "\n\n"
                 + "   ========================================================\n";
        }
    }

    private ArrayList<Vehicle> list = new ArrayList<>();
    private JTextArea area = new JTextArea();
    private JLabel total = new JLabel("0"), cars = new JLabel("0"), bikes = new JLabel("0");

    // ================= MAIN GUI FRAME CONSTRUCTOR =================
    public VehicleRentalSystem() {
        setTitle("Vehicle Rental System - Premium Edition");
        setSize(1350, 830);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Premium Gradient Header Banner
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(24, 28, 36), getWidth(), 0, new Color(41, 128, 185)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(100, 100));

        // Rounded Decorative Metallic Logo
        JPanel logoPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(250, 250, 250), 70, 70, new Color(110, 115, 120))); g2.fillOval(0, 0, 70, 70);
                g2.setColor(new Color(140, 145, 150)); g2.fillOval(3, 3, 64, 64);
                g2.setPaint(new GradientPaint(0, 0, new Color(20, 40, 80), 70, 70, new Color(40, 70, 130))); g2.fillOval(5, 5, 60, 60);
                g2.setColor(new Color(240, 245, 250)); g2.setStroke(new BasicStroke(2.0f)); g2.drawOval(8, 8, 54, 54);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18)); g2.setColor(Color.WHITE); g2.drawString("VRS", 18, 36);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 8)); g2.setColor(new Color(210, 225, 245)); g2.drawString("PREMIUM", 18, 48);
            }
        };
        logoPanel.setPreferredSize(new Dimension(70, 70));
        logoPanel.setOpaque(false);
        header.add(logoPanel);

        JLabel title = new JLabel("VEHICLE RENTAL SYSTEM SUITE");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.add(title);

        // Modern Sidebar Control Panel
        JPanel side = new JPanel(new GridLayout(8, 1, 0, 15));
        side.setBackground(new Color(24, 28, 36));
        side.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        side.setPreferredSize(new Dimension(240, 100));

        JButton[] btns = {
            btn("Add Vehicle", new Color(52, 152, 219)),  
            btn("View All Units", new Color(46, 204, 113)),
            btn("Search By ID", new Color(155, 89, 182)),  
            btn("Update Records", new Color(241, 196, 15)),
            btn("Delete Vehicle", new Color(231, 76, 60)),
            btn("Rent Process", new Color(26, 188, 156)),
            btn("Save Dataset", new Color(127, 140, 141)),      
            btn("Exit Session", new Color(192, 57, 43))
        };
        for (JButton b : btns) side.add(b);

        // Status Widgets Layout Panel
        JPanel dash = new JPanel(new GridLayout(1, 3, 25, 25));
        dash.setBackground(new Color(245, 246, 250));
        dash.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        dash.add(card("TOTAL CONTROL FLEET", total, new Color(41, 128, 185), "📊"));
        dash.add(card("TOTAL CARS LOGGED", cars, new Color(39, 174, 96), "🚗"));
        dash.add(card("TOTAL BIKES LOGGED", bikes, new Color(142, 68, 173), "🏍"));

        // Custom Dark Arcade Output Screen
        area.setFont(new Font("Consolas", Font.PLAIN, 16));
        area.setEditable(false);
        area.setBackground(new Color(18, 20, 26));
        area.setForeground(new Color(241, 196, 15));
        area.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
       
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1));
       
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                this.thumbColor = new Color(52, 73, 94);
                this.trackColor = new Color(28, 30, 38);
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 246, 250));
        centerPanel.add(dash, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 25));
       
        add(header, BorderLayout.NORTH);
        add(side, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        // Action Mapping
        btns[0].addActionListener(e -> addVehicle());  
        btns[1].addActionListener(e -> viewAll());
        btns[2].addActionListener(e -> search());      
        btns[3].addActionListener(e -> update());
        btns[4].addActionListener(e -> delete());      
        btns[5].addActionListener(e -> rentVehicle());
        btns[6].addActionListener(e -> { save(); JOptionPane.showMessageDialog(this, "Data successfully saved to data.txt!", "Success", JOptionPane.INFORMATION_MESSAGE); });        
        btns[7].addActionListener(e -> { save(); System.exit(0); });

        load(); updateDashboard(); viewAll(); setVisible(true);
    }

    // ================= SECURE BLANK LOGIN INTERFACE COMPONENT =================
    static class LoginWindow extends JFrame {
        private JTextField userField;
        private JPasswordField passField;
        private JLabel statusLabel;

        public LoginWindow() {
            setTitle("VRS - Secure Access");
            setSize(450, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setUndecorated(true); // Borderless modern aesthetic
           
            JPanel panel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setPaint(new GradientPaint(0, 0, new Color(24, 28, 36), 0, getHeight(), new Color(34, 40, 52)));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            panel.setLayout(null);
            setContentPane(panel);

            JLabel lblTitle = new JLabel("SYSTEM SECURITY");
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
            lblTitle.setForeground(new Color(52, 152, 219));
            lblTitle.setBounds(40, 40, 300, 30);
            panel.add(lblTitle);

            JLabel lblSub = new JLabel("Please sign in to access control deck");
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblSub.setForeground(new Color(127, 140, 141));
            lblSub.setBounds(40, 70, 300, 20);
            panel.add(lblSub);

            // Username input layout elements (Blank)
            JLabel lblUser = new JLabel("USERNAME");
            lblUser.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblUser.setForeground(new Color(189, 195, 199));
            lblUser.setBounds(40, 130, 300, 20);
            panel.add(lblUser);

            userField = new JTextField("");
            userField.setBounds(40, 155, 370, 40);
            userField.setBackground(new Color(44, 52, 68));
            userField.setForeground(Color.WHITE);
            userField.setCaretColor(Color.WHITE);
            userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            userField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(52, 73, 94), 1),
                    BorderFactory.createEmptyBorder(0, 10, 0, 10)));
            panel.add(userField);

            // Password layout elements (Blank)
            JLabel lblPass = new JLabel("PASSWORD");
            lblPass.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblPass.setForeground(new Color(189, 195, 199));
            lblPass.setBounds(40, 215, 300, 20);
            panel.add(lblPass);

            passField = new JPasswordField("");
            passField.setBounds(40, 240, 370, 40);
            passField.setBackground(new Color(44, 52, 68));
            passField.setForeground(Color.WHITE);
            passField.setCaretColor(Color.WHITE);
            passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            passField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(52, 73, 94), 1),
                    BorderFactory.createEmptyBorder(0, 10, 0, 10)));
            panel.add(passField);

            // Dynamic Error Message Status Feedback
            statusLabel = new JLabel("");
            statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            statusLabel.setForeground(new Color(231, 76, 60));
            statusLabel.setBounds(40, 295, 370, 20);
            panel.add(statusLabel);

            // Access Control Custom Action Buttons
            JButton loginBtn = loginCustomBtn("AUTHENTICATE", new Color(41, 128, 185));
            loginBtn.setBounds(40, 330, 370, 45);
            loginBtn.addActionListener(e -> processLogin());
            panel.add(loginBtn);

            JButton closeBtn = loginCustomBtn("CANCEL SESSION", new Color(192, 57, 43));
            closeBtn.setBounds(40, 390, 370, 45);
            closeBtn.addActionListener(e -> System.exit(0));
            panel.add(closeBtn);

            userField.addActionListener(e -> processLogin());
            passField.addActionListener(e -> processLogin());

            setVisible(true);
        }

        private void processLogin() {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setForeground(new Color(231, 76, 60));
                statusLabel.setText("Please fill out both entry fields.");
                return;
            }

            // ================= NEW CREDENTIALS CHECK =================
            if (username.equals("Isha") && password.equals("Isha123")) {
                statusLabel.setForeground(new Color(46, 204, 113));
                statusLabel.setText("Access Granted! Syncing Core Engine...");
               
                Timer timer = new Timer(600, event -> {
                    this.dispose();
                    new VehicleRentalSystem();
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                statusLabel.setForeground(new Color(231, 76, 60));
                statusLabel.setText("Authentication Failed: Invalid credentials.");
            }
        }

        private JButton loginCustomBtn(String text, Color bg) {
            JButton b = new JButton(text) {
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getModel().isPressed()) g2.setColor(bg.darker());
                    else if (getModel().isRollover()) g2.setColor(bg.brighter());
                    else g2.setColor(bg);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                   
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(getText())) / 2;
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(getText(), x, y);
                }
            };
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return b;
        }
    }

    // ================= CUSTOM ROUNDED BUTTON GENERATOR =================
    private JButton btn(String t, Color bg) {
        JButton b = new JButton(t) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(bg.darker());
                else if (getModel().isRollover()) g2.setColor(bg.brighter());
                else g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
               
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ================= PREMIUM WIDGET CARD GENERATOR =================
    private JPanel card(String titleText, JLabel valueLabel, Color accentColor, String icon) {
        JPanel cardBody = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(accentColor);
                g2.fillRect(0, 0, 10, getHeight());
            }
        };
        cardBody.setOpaque(false);
        cardBody.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));
       
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setForeground(new Color(110, 120, 135));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
       
        valueLabel.setForeground(new Color(40, 45, 55));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 38));
       
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setForeground(accentColor.brighter());
       
        JPanel textLayout = new JPanel(new GridLayout(2, 1, 0, 2));
        textLayout.setOpaque(false);
        textLayout.add(titleLabel);
        textLayout.add(valueLabel);
       
        cardBody.add(textLayout, BorderLayout.WEST);
        cardBody.add(iconLabel, BorderLayout.EAST);
        return cardBody;
    }

    private String input(String msg) {
        String v = JOptionPane.showInputDialog(this, msg);
        return (v == null) ? "" : v.trim();
    }
   
    private Vehicle find(int id) {
        return list.stream().filter(v -> v.getId() == id).findFirst().orElse(null);
    }

    // ================= CENTRAL WORKING LOGIC ENGINE =================
    private void addVehicle() {
        try {
            String idStr = input("Enter New Unique Vehicle ID:"); if(idStr.isEmpty()) return;
            int id = Integer.parseInt(idStr);
            if (find(id) != null) {
                JOptionPane.showMessageDialog(this, "Operation Error: Vehicle ID already exists!", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String type = input("Enter Vehicle Type (Car / Bike):");
            if (type.isEmpty() || (!type.equalsIgnoreCase("Car") && !type.equalsIgnoreCase("Bike"))) {
                JOptionPane.showMessageDialog(this, "Invalid Type! Type must be 'Car' or 'Bike'.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String brand = input("Enter Brand Name:"); if (brand.isEmpty()) return;
            String rentStr = input("Enter Daily Rental Rent Structure:"); if (rentStr.isEmpty()) return;
            double rent = Double.parseDouble(rentStr);

            if (type.equalsIgnoreCase("Car")) {
                String doorsStr = input("Specify Cabin Doors Count:"); if (doorsStr.isEmpty()) return;
                list.add(new Car(id, brand, rent, Integer.parseInt(doorsStr)));
            } else {
                String ccStr = input("Specify Engine Displacement CC Metric:"); if (ccStr.isEmpty()) return;
                list.add(new Bike(id, brand, rent, Integer.parseInt(ccStr)));
            }

            updateDashboard(); save(); viewAll();
            JOptionPane.showMessageDialog(this, "Success: Vehicle Added Successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Parsing Error: Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAll() {
        area.setText("");
        area.setForeground(new Color(46, 204, 113));
        if(list.isEmpty()) {
            area.setText("\n ❌ No active files registered in database storage container.");
        } else {
            area.append(" ========================================== SYSTEM REGISTRY ENTRIES ==========================================\n\n");
            list.forEach(v -> area.append(v.display() + "\n"));
        }
    }

    private void search() {
        try {
            String idStr = input("Identify Search Query ID Key:"); if(idStr.isEmpty()) return;
            Vehicle v = find(Integer.parseInt(idStr)); area.setText("");
            if (v == null) {
                area.setForeground(new Color(231, 76, 60));
                area.setText("\n ❌ Object query reference index unmatched.");
            } else {
                area.setForeground(new Color(52, 152, 219));
                area.setText(" === QUERY RECORD REPORT FOUND ===\n\n" + v.display());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void update() {
        try {
            String idStr = input("Enter Target ID Index to Modify Metrics:"); if (idStr.isEmpty()) return;
            Vehicle v = find(Integer.parseInt(idStr));
            if (v != null) {
                String b = input("Modify Manufacturer Brand (Leave blank to preserve):");
                String r = input("Modify Rental Metric Daily Value (Leave blank to preserve):");
                if(!b.isEmpty()) v.setBrand(b);
                if(!r.isEmpty()) v.setRentPerDay(Double.parseDouble(r));
                save(); viewAll();
                JOptionPane.showMessageDialog(this, "Success: Target row values modified.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Vehicle ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number inputs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void delete() {
        try {
            String idStr = input("Identify Target ID Pointer to Purge:"); if (idStr.isEmpty()) return;
            Vehicle v = find(Integer.parseInt(idStr));
            if (v != null) {
                list.remove(v); updateDashboard(); save(); viewAll();
                JOptionPane.showMessageDialog(this, "Vehicle removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Vehicle ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) { }
    }

    private void rentVehicle() {
        try {
            String idStr = input("Enter Target Dispatch Vehicle Unit ID:"); if (idStr.isEmpty()) return;
            Vehicle v = find(Integer.parseInt(idStr));
            if (v != null && v.isAvailable()) {
                String daysStr = input("Specify Total Booking Days:"); if (daysStr.isEmpty()) return;
                int d = Integer.parseInt(daysStr);
                String n = input("Specify End-User Legal Name:"); if(n.isEmpty()) return;
               
                v.setAvailable(false); save();
                area.setForeground(new Color(241, 196, 15));
                area.setText(new Rental(new Customer(n), v, d).generateBillReceipt());
                updateDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Alert: Vehicle completely booked or does not exist.", "Constraint Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) { }
    }

    private void updateDashboard() {
        total.setText(String.valueOf(list.size()));
        cars.setText(String.valueOf(list.stream().filter(v -> v instanceof Car).count()));
        bikes.setText(String.valueOf(list.stream().filter(v -> v instanceof Bike).count()));
    }

    // ================= FILE HANDLING WORKSPACE =================
    private void save() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data.txt"))) {
            for (Vehicle v : list) writer.println(v.toFileString());
        } catch (IOException e) { System.err.println("Error saving file: " + e.getMessage()); }
    }

    private void load() {
        list.clear();
        File file = new File("data.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
               
                try {
                    String[] tokens = line.split(",");
                    if (tokens.length < 6) continue;
                   
                    String type = tokens[0];
                    int id = Integer.parseInt(tokens[1]);
                    String brand = tokens[2];
                    double rent = Double.parseDouble(tokens[3]);
                    int attr = Integer.parseInt(tokens[4]);
                    boolean avail = Boolean.parseBoolean(tokens[5]);

                    if (type.equals("CAR")) {
                        Car c = new Car(id, brand, rent, attr);
                        c.setAvailable(avail);
                        list.add(c);
                    } else if (type.equals("BIKE")) {
                        Bike b = new Bike(id, brand, rent, attr);
                        b.setAvailable(avail);
                        list.add(b);
                    }
                } catch (Exception e) {
                    System.err.println("Skipping corrupted line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
   
    // ================= MAIN THREAD DISPATCHER =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception e) { }
            new LoginWindow();
        });
    }
}
