import java.io.*;
import java.util.*;
import java.util.regex.*;

public class PolynomialSecret {

    // Decode a value from a specific base (supports decimal, hex, binary, etc.)
    public static int decodeValue(int base, String value) {
        int decodedValue = 0;
        for (int i = 0; i < value.length(); i++) {
            char digit = value.charAt(i);
            int digitValue = Character.digit(digit, base);
            decodedValue = decodedValue * base + digitValue;
        }
        return decodedValue;
    }

    // Perform Lagrange interpolation to find the constant term (c)
    public static int lagrangeInterpolation(int[][] points) {
        int n = points.length;
        int c = 0;

        for (int i = 0; i < n; i++) {
            int xi = points[i][0];
            int yi = points[i][1];

            int li = 1;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    int xj = points[j][0];
                    li = li * (0 - xj) / (xi - xj);
                }
            }
            c += yi * li;
        }

        return c;
    }

    // Read the file and return the content as a String
    public static String readFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    // Parse the JSON manually (simple string-based approach)
    public static Map<String, String> parseJson(String json) {
        Map<String, String> result = new HashMap<>();
        
        // Extracting the "keys" block for n and k
        int startIndex = json.indexOf("\"keys\":");
        int endIndex = json.indexOf("}", startIndex) + 1;
        String keysBlock = json.substring(startIndex, endIndex);
        
        // Extracting the n and k values
        String nValue = extractValue(keysBlock, "\"n\":");
        String kValue = extractValue(keysBlock, "\"k\":");

        if (nValue != null && kValue != null) {
            result.put("keys.n", nValue);
            result.put("keys.k", kValue);
        } else {
            throw new IllegalArgumentException("Missing 'n' or 'k' values in JSON.");
        }
        
        // Extracting the root values (1 to n)
        for (int i = 1; i <= 10; i++) {  // Assuming we are looking for 10 roots
            String baseKey = "\"" + i + "\":";
            String valueKey = "\"" + i + "\":";
            
            startIndex = json.indexOf(baseKey);
            if (startIndex != -1) {
                endIndex = json.indexOf("}", startIndex) + 1;
                String rootBlock = json.substring(startIndex, endIndex);
                
                String base = extractValue(rootBlock, "\"base\":");
                String value = extractValue(rootBlock, "\"value\":");
                
                if (base != null && value != null) {
                    result.put(i + ".base", base);
                    result.put(i + ".value", value);
                }
            }
        }
        
        return result;
    }

    // extract the value with a key
    private static String extractValue(String block, String key) {
        int startIndex = block.indexOf(key);
        if (startIndex == -1) return null;
        startIndex = block.indexOf(":", startIndex) + 1;
        int endIndex = block.indexOf(",", startIndex);
        if (endIndex == -1) endIndex = block.indexOf("}", startIndex);
        
        return block.substring(startIndex, endIndex).replace("\"", "").trim();
    }

    public static void main(String[] args) {
        // Read the JSON file
        String jsonData = readFile("test_case2.json");

        // Parse
        Map<String, String> inputData = parseJson(jsonData);

        // print 
        System.out.println("Parsed data: " + inputData);

        // Get the n and k values
        try {
            String nStr = inputData.get("keys.n");
            String kStr = inputData.get("keys.k");

            if (nStr == null || kStr == null) {
                throw new IllegalArgumentException("Missing 'n' or 'k' values in JSON.");
            }

            int n = Integer.parseInt(nStr);
            int k = Integer.parseInt(kStr);

            System.out.println("n = " + n + ", k = " + k);

            int[][] points = new int[n][2];
            int pointCount = 0;  // To track the number of valid points

            for (int i = 1; i <= n; i++) {
                String baseKey = i + ".base";
                String valueKey = i + ".value";

                // Extract base and value
                String baseStr = inputData.get(baseKey);
                String valueStr = inputData.get(valueKey);

                // If either base or value is missing, skip this root
                if (baseStr == null || valueStr == null) {
                    System.err.println("Warning: Missing base or value for root " + i);
                    continue;
                }

                int base = Integer.parseInt(baseStr);
                int decodedValue = decodeValue(base, valueStr);

                // Store the point (x, y)
                points[pointCount][0] = i;  // x = i
                points[pointCount][1] = decodedValue;  // y = decodedValue
                pointCount++;  // Increment valid points

                if (pointCount == n) break;  // Stop if we've collected enough points
            }

            // Check if we have enough points
            if (pointCount < n) {
                System.err.println("Error: Not enough valid points for interpolation.");
                return;
            }

            // Lagrange interpolation
            int secret = lagrangeInterpolation(points);

            System.out.println("The secret (constant term) of the polynomial is: " + secret);

        } catch (Exception e) {
            System.err.println("Error parsing JSON or processing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
