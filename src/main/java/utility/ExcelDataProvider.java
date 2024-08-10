package utility;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelDataProvider {

	// Method to get data from Excel file with file path parameter
	public Object[][] getDataFromExcel(String filePath) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheetAt(0);

		List<Object[]> data = new ArrayList<>();

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			Object[] rowData = new Object[row.getLastCellNum()];

			for (int j = 0; j < row.getLastCellNum(); j++) {
				rowData[j] = row.getCell(j).toString();
			}

			data.add(rowData);
		}

		workbook.close();
		fis.close();

		return data.toArray(new Object[0][]);
	}
}
