package test.Utility;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.DataProvider;

import utility.ExcelDataProvider;

public class DataProviderUtil {

	private String currentDirectory = System.getProperty("user.dir");
	private String linkedLoginExcel = "src" + File.separator + "test" + File.separator + "resources" + File.separator
			+ "linkedExceldata" + File.separator + "linkedlogin-data.xlsx";

	private ExcelDataProvider read = new ExcelDataProvider();

	// DataProvider method that uses getDataFromExcel
	@DataProvider(name = "Linkedlogindata")
	public Object[][] LinkedIndata() throws IOException {
		String filePath = currentDirectory + File.separator + linkedLoginExcel;
		System.out.println("Excel file path: " + filePath); // For debugging
		return read.getDataFromExcel(filePath);
	}

}
