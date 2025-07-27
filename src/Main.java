package smartnotes;
import java.sql.*;
import java.util.Scanner;
public class Main {
    private static final String DB_URL = "jdbc:sqlite:notes.db";
    public static void main(String[] args) {
        createTableIfNotExists();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n📒 Smart Notes Manager");
            System.out.println("1. Add Note");
            System.out.println("2. View All Notes");
            System.out.println("3. Search Notes by Priority");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear input buffer

            switch (choice) {
                case 1:
                    addNote(scanner);
                    break;
                case 2:
                    viewAllNotes();
                    break;
                case 3:
                    searchByPriority(scanner);
                    break;
                case 4:
                    System.out.println("✅ Exiting... Bye!");
                    return;
                default:
                    System.out.println("⚠️ Invalid choice. Try again.");
            }
        }
    }
    private static void createTableIfNotExists() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS notes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "content TEXT," +
                    "priority TEXT," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(sql);
            System.out.println("✅ Table ready.");
        } catch (SQLException e) {
            System.out.println("❌ DB Error: " + e.getMessage());
        }
    }
    private static void addNote(Scanner scanner) {
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Content: ");
        String content = scanner.nextLine();
        System.out.print("Enter Priority (High/Medium/Low): ");
        String priority = scanner.nextLine();

        String sql = "INSERT INTO notes (title, content, priority) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, priority);
            pstmt.executeUpdate();
            System.out.println("✅ Note added!");
        } catch (SQLException e) {
            System.out.println("❌ Insert Error: " + e.getMessage());
        }
    }
    private static void viewAllNotes() {
        String sql = "SELECT * FROM notes ORDER BY timestamp DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n📝 All Notes:");
            while (rs.next()) {
                System.out.println("-----------------------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Content: " + rs.getString("content"));
                System.out.println("Priority: " + rs.getString("priority"));
                System.out.println("Timestamp: " + rs.getString("timestamp"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Fetch Error: " + e.getMessage());
        }
    }
    private static void searchByPriority(Scanner scanner) {
        System.out.print("Enter priority to search (High/Medium/Low): ");
        String priority = scanner.nextLine();

        String sql = "SELECT * FROM notes WHERE priority = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, priority);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n🔍 Notes with Priority: " + priority);
            while (rs.next()) {
                System.out.println("-----------------------------------");
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Content: " + rs.getString("content"));
                System.out.println("Timestamp: " + rs.getString("timestamp"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Search Error: " + e.getMessage());
        }
    }
}


