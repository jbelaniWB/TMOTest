package example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.IExecutionListener;

public class ExecutionListener implements IExecutionListener{

	@Override
	public void onExecutionFinish() {
		// TODO Auto-generated method stub
		try {
			this.uploadResultFiles(this.QMetryTest());
			System.out.println("TestNG is finished. Results uploaded to QMetry/JIRA");
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onExecutionStart() {
		// TODO Auto-generated method stub
		
	}
	
	public String QMetryTest() throws IOException, JSONException{
		URL url = new URL("https://importresults.qmetry.com/prod/importresults-qtm4j");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		StringBuilder jsonBody = new StringBuilder("{");
		jsonBody.append("\"format\":" + "\"testng/xml\",");
		jsonBody.append("\"testRunName\":" + "\"New Test Run\",");
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
	
	public void uploadResultFiles(String importResultUrl) throws IOException{
		URL url = new URL(importResultUrl);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setRequestProperty("Content-Type", "multipart/form-data");
		connection.setDoOutput(true);
		connection.setDoInput(true);

		FileInputStream file = new FileInputStream("/Users/qamacbookpro/Documents/workspace/WebdriverTest/target/surefire-reports/testng-results.xml");

		OutputStream os = connection.getOutputStream();
		IOUtils.copy(file, os);

		InputStream fis = connection.getInputStream();
		StringWriter writer = new StringWriter();
		String encoding = "UTF-8";
		IOUtils.copy(fis, writer, encoding);
		if (connection.getResponseCode() == 200) {
			System.out.println("Success");
		}else{
			System.out.println(writer.toString());
		}
	}
	 
}
