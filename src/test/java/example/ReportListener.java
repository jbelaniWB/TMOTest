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
						this.createJIRAIssue(iTestNGMethod.getMethodName(), "Unable to locate element: Click here (WARNING: The server did not provide any stacktrace information)");
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

	public void createJIRAIssue(String failedMethod, String description) throws Exception {
		URL url = new URL("https://wbdigital.atlassian.net/rest/api/2/issue");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Authorization",
				"Basic amlnaXNoLmJlbGFuaUB3YmNvbnN1bHRhbnQuY29tOltqYmVsMDkwMTgyXQ==");
		connection.setRequestProperty("Cache-Control", "no-cache");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		String jsonString = new JSONObject()
				.put("fields",
						new JSONObject().put("project", new JSONObject().put("key", "VP")).put("summary", failedMethod)
								.put("description", description).put("issuetype", new JSONObject().put("name", "Bug")))
				.toString();

		System.out.println(jsonString);

		OutputStream os = connection.getOutputStream();
		os.write(jsonString.toString().getBytes("UTF-8"));
		InputStream fis = connection.getInputStream();

		StringWriter response = new StringWriter();
		String encoding = "UTF-8";
		IOUtils.copy(fis, response, encoding);
		System.out.println(response.toString());
	}
	
	public void getSauceLabsJobDetails(String jobId) throws Exception{
		//https://jbelaniWB:8c0ad879-a2ae-416c-850e-8d14d186a13d@saucelabs.com/rest/v1/users/jobs/2e60efae941644b5b5262b9fd9f9a3c9
		String url = "https://flixstervideo:4bff95a5-6938-405d-a3ae-856dc7e86da0@saucelabs.com/rest/v1/users/jobs/" + jobId;

		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

	    con.setRequestProperty("Content-Type", "application/json");
	    con.setDoOutput(true);

	 // optional default is GET
	 		con.setRequestMethod("GET");

	    String userpass = "flixstervideo" + ":" + "4bff95a5-6938-405d-a3ae-856dc7e86da0";
	    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
	    con.setRequestProperty ("Authorization", basicAuth);

		

		//add request header
		//con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Accept", "application/json");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
//    	System.out.println("Test");
		
	}
	
	public void getSauceLabsJobAssets(String jobId) throws Exception{
		String url = "https://flixstervideo:4bff95a5-6938-405d-a3ae-856dc7e86da0@saucelabs.com/rest/v1/users/jobs/" + jobId + "/assets";

		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

	    con.setRequestProperty("Content-Type", "application/json");
	    con.setDoOutput(true);

	 // optional default is GET
	 		con.setRequestMethod("GET");

	    String userpass = "flixstervideo" + ":" + "4bff95a5-6938-405d-a3ae-856dc7e86da0";
	    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
	    con.setRequestProperty ("Authorization", basicAuth);

		

		//add request header
		//con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Accept", "application/json");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		JSONObject jsonData = new JSONObject(response.toString());
		//String uploadUrl = test.getString("screenshots");
		JSONArray screenshotsArray = jsonData.getJSONArray("screenshots");
		Path path = Paths.get("downloads" + File.separator + "test/");
        Files.createDirectories( path);
        System.out.println("Path: " + path.toString());
	}
	
	public  void downloadFile(String fileURL, String saveDir)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        String userpass = username + ":" + accesskey;
	    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
	    httpConn.setRequestProperty ("Authorization", basicAuth);
        int responseCode = httpConn.getResponseCode();
 
        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();
 
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
 
            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);
 
            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;
             
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
 
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();
 
            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }
}
