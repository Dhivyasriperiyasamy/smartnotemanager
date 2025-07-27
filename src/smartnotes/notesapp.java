package smartnotes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class notesapp {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:notes.db"; // database file will be created here

        String sql = "CREATE TABLE IF NOT EXISTS notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "content TEXT," +
                "priority TEXT," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("✅ Table created or already exists.");

            // Example insert (optional)
            String insertSQL = "INSERT INTO notes (title, content, priority) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, "Finish Java project");
            pstmt.setString(2, "Smart Notes Manager with SQLite");
            pstmt.setString(3, "High");
            pstmt.executeUpdate();
            System.out.println("✅ Note inserted.");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}

