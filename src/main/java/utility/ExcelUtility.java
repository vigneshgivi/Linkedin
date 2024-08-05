package utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {

	public static void writeKeyValuePairsToExcel(Map<String, String> keyValuePairs) {
		Workbook workbook = null;
		FileOutputStream fos = null;
		try {
			workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("KeyValuePairs");

			// Header row
			Row headerRow = sheet.createRow(0);
			Cell headerCell1 = headerRow.createCell(0);
			headerCell1.setCellValue("Name");
			Cell headerCell2 = headerRow.createCell(1);
			headerCell2.setCellValue("URL");

			// Data rows
			int rowNum = 1;
			for (Map.Entry<String, String> entry : keyValuePairs.entrySet()) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(entry.getKey());
				row.createCell(1).setCellValue(entry.getValue());
			}

			// Generate dynamic file name with current date and time
			String filePath = generateFileName();

			// Write the output to the file
			fos = new FileOutputStream(filePath);
			workbook.write(fos);
			System.out.println("Excel file written successfully to: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (workbook != null)
					workbook.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static String generateFileName() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		LocalDateTime now = LocalDateTime.now();
		String fileName = "key_value_pairs_" + dtf.format(now) + ".xlsx";

		// Get current project directory
		String projectDir = System.getProperty("user.dir");

		// Construct the path for the resources directory
		String resourcesPath = Paths.get(projectDir, "src", "test", "resources").toString();

		// Ensure resources directory exists
		File resourcesDir = new File(resourcesPath);

		if (!resourcesDir.exists()) {
			if (resourcesDir.mkdirs()) {
				System.out.println("Created resources directory.");
			} else {
				System.err.println("Failed to create resources directory.");
			}
		}

		// Return the full path of the file
		return Paths.get(resourcesPath, fileName).toString();
	}
}
