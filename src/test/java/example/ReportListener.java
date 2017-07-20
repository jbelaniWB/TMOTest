package example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

public class ReportListener implements IReporter {

	public String username = "flixstervideo";
	 public String accesskey = "4bff95a5-6938-405d-a3ae-856dc7e86da0";
	 private  final int BUFFER_SIZE = 4096;
	 private String sauceLabsJobId = "61fa59dfeeb5491a89598261f1821893";
	@Override

	public void generateReport(List<XmlSuite> arg0, List<ISuite> arg1,
			String outputDirectory) {
		// Second parameter of this method ISuite will contain all the suite
		// executed.
		for (ISuite iSuite : arg1) {
			// Get a map of result of a single suite at a time
			Map<String, ISuiteResult> results = iSuite.getResults();
			// Get the key of the result map
			Set<String> keys = results.keySet();
			// Go to each map value one by one
			for (String key : keys) {
				// The Context object of current result
				ITestContext context = results.get(key).getTestContext();
				// Print Suite detail in Console
				System.out.println("Suite Name->" + context.getName()
						+ "::Report output Ditectory->" + context.getOutputDirectory()
						+ "::Suite Name->" + context.getSuite().getName()
						+ "::Start Date Time for execution->" + context.getStartDate()
						+ "::End Date Time for execution->" + context.getEndDate());
				
				// Get Map for only failed test cases
				IResultMap resultMap = context.getFailedTests();

				// Get method detail of failed test cases
				Collection<ITestNGMethod> failedMethods = resultMap.getAllMethods();

				// Loop one by one in all failed methods
				System.out.println("--------FAILED TEST CASE---------");

				for (ITestNGMethod iTestNGMethod : failedMethods) {
					// Print failed test cases detail
					System.out.println("TESTCASE NAME->" + iTestNGMethod.getMethodName()
							+ "\nDescription->" + iTestNGMethod.getDescription()
							+ "\nPriority->" + iTestNGMethod.getPriority()
							+ "\n:Date->" + new Date(iTestNGMethod.getDate()));
					try {
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		try {
			this.uploadResultFiles(this.QMetryTest());
			System.out.println("TestNG is finished. Results uploaded to QMetry/JIRA");
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String QMetryTest() throws IOException, JSONException {
		URL url = new URL("https://importresults.qmetry.com/prod/importresults-qtm4j");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		StringBuilder jsonBody = new StringBuilder("{");
		jsonBody.append("\"format\":" + "\"testng/xml\",");
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		f.setTimeZone(TimeZone.getTimeZone("UTC"));
		System.out.println("\"Test Run - " + f.format(new Date()) + "\",");
		jsonBody.append("\"testRunName\":" + "\"Test Run - " + f.format(new Date()) + "\",");
		jsonBody.append("\"apiKey\":" + "\"999fbb5becbee59b72379207bb659a21d4015822a7a2ac6c2af06cb7d35612ad\"");
		jsonBody.append("}");

		OutputStream os = connection.getOutputStream();
		os.write(jsonBody.toString().getBytes("UTF-8"));
		InputStream fis = connection.getInputStream();

		StringWriter response = new StringWriter();
		String encoding = "UTF-8";
		IOUtils.copy(fis, response, encoding);
		System.out.println(response.toString());

		JSONObject obj = new JSONObject(response.toString());
		String uploadUrl = obj.getString("url");
		System.out.println("UploadUrl: " + uploadUrl);

		return uploadUrl;
	}

	public void uploadResultFiles(String importResultUrl) throws IOException {
		URL url = new URL(importResultUrl);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Content-Type", "multipart/form-data");
		connection.setDoOutput(true);
		connection.setDoInput(true);

		FileInputStream file = new FileInputStream(
				"/Users/qamacbookpro/Documents/workspace/WebdriverTest/target/surefire-reports/testng-results.xml");

		OutputStream os = connection.getOutputStream();
		IOUtils.copy(file, os);

		InputStream fis = connection.getInputStream();
		StringWriter writer = new StringWriter();
		String encoding = "UTF-8";
		IOUtils.copy(fis, writer, encoding);
		if (connection.getResponseCode() == 200) {
			System.out.println("Success");
		} else {
			System.out.println(writer.toString());
		}
	}
}
