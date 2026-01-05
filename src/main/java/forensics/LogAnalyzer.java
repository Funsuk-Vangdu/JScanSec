package forensics;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LogAnalyzer implements ForensicAnalyzer {

    @Override
    public void analyze() {
        System.out.println("📑 Analyzing system logs...");

        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("sample_auth.log")) {

            if (is == null) {
                System.out.println("❌ sample_auth.log not found!");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            int failedCount = 0;

            // Simple heuristic: detect failed authentication attempts
            while ((line = reader.readLine()) != null) {
                if (line.contains("Failed password")) {
                    failedCount++;
                }
            }

            System.out.println("🔎 Total Failed Login Attempts: " + failedCount);

        } catch (Exception e) {
            System.out.println("❌ Error reading log file: " + e.getMessage());
        }
    }
}
