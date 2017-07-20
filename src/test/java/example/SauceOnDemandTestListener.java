package example;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.common.Utils;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

public class SauceOnDemandTestListener extends TestListenerAdapter{
	
	private static final String SELENIUM_BROWSER = "SELENIUM_BROWSER";
    private static final String SELENIUM_PLATFORM = "SELENIUM_PLATFORM";
    private static final String SELENIUM_VERSION = "SELENIUM_VERSION";
    private static final String SELENIUM_IS_LOCAL = "SELENIUM_IS_LOCAL";
    public String username = "flixstervideo";
	public String accesskey = "4bff95a5-6938-405d-a3ae-856dc7e86da0";
	private  final int BUFFER_SIZE = 4096;
	private String sauceLabsSessionId = "";
	private ITestResult sauceResults;
    
    /**
     * The instance of the Sauce OnDemand Java REST API client.
     */
    private SauceREST sauceREST;

    /**
     * Treat this test as a local test or run in SauceLabs.
     */
    private boolean isLocal = false;

    /**
     * Boolean indicating whether to print the log messages to the stdout.
     */
    public static boolean verboseMode = true;

    /**
     * Check to see if environment variables that define the Selenium browser to be used have been set (typically by
     * a Sauce OnDemand CI plugin).  If so, then populate the appropriate system parameter, so that tests can use
     * these values.
     *
     * @param testContext Current test context
     */
    @Override
    public void onStart(ITestContext testContext) {
        super.onStart(testContext);
        String local = Utils.readPropertyOrEnv(SELENIUM_IS_LOCAL, "");
        if (local != null && !local.equals("")) {
            isLocal = true;
        }
        String browser = System.getenv(SELENIUM_BROWSER);
        if (browser != null && !browser.equals("")) {
            System.setProperty("browser", browser);
        }
        String platform = System.getenv(SELENIUM_PLATFORM);
        if (platform != null && !platform.equals("")) {
            System.setProperty("os", platform);
        }
        String version = System.getenv(SELENIUM_VERSION);
        if (version != null && !version.equals("")) {
            System.setProperty("version", version);
        }
    }

    /**
     * @param result Test Result for the test being started
     */
    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
System.out.println("This is called on every method test start");
        if (isLocal) {
            return;
        }

        SauceOnDemandAuthentication sauceOnDemandAuthentication;
        if (result.getInstance() instanceof SauceOnDemandAuthenticationProvider) {
            //use the authentication information provided by the test class
            SauceOnDemandAuthenticationProvider provider = (SauceOnDemandAuthenticationProvider) result.getInstance();
            sauceOnDemandAuthentication = provider.getAuthentication();
        } else {
            //otherwise use the default authentication
            sauceOnDemandAuthentication = new SauceOnDemandAuthentication();
        }
        this.sauceREST = new SauceREST(sauceOnDemandAuthentication.getUsername(), sauceOnDemandAuthentication.getAccessKey());
        
        //Temp place
        if(sauceLabsSessionId != ""){
        	
        	System.out.println("Calling JIRA Methods : " + this.sauceLabsSessionId);
        	try {
        		Thread.sleep(15000);
				this.getSauceLabsJobAssets(sauceLabsSessionId);
				this.createJIRAIssue(sauceResults.getMethod().getMethodName(), "Unable to locate element: Click here (WARNING: The server did not provide any stacktrace information)", sauceLabsSessionId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    /**
     * @param testResult Test result of the test that just failed.
     */
    @Override
    public void onTestFailure(ITestResult testResult) {
        SauceOnDemandSessionIdProvider sessionIdProvider = (SauceOnDemandSessionIdProvider) testResult.getInstance();
        if (sessionIdProvider != null && sauceREST != null) {
            String sessionId = sessionIdProvider.getSessionId();
            markJobStatus(sessionId, false);
            printOutSessionID(sessionId, testResult.getMethod().getMethodName());
            printPublicJobLink(sessionId);
            System.out.println("Set SessionId: "+ sessionId);
            System.out.println("Failed Method: "+ testResult.getMethod().getMethodName());
            this.sauceLabsSessionId = sessionId;
            this.sauceResults = testResult;
        }
        super.onTestFailure(testResult);
    }

    private void markJobStatus(String sessionId, boolean passed) {

        try {
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("passed", passed);
            //Utils.addBuildNumberToUpdate(updates);
            sauceREST.updateJobInfo(sessionId, updates);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void printPublicJobLink(String sessionId) {
        if (verboseMode) {
            String authLink = this.sauceREST.getPublicJobLink(sessionId);
            // String authLink = "test";
            System.out.println("Job link: " + authLink);
        }
    }

    private void printOutSessionID(String sessionId, String testName) {
        System.out.println(String.format("SauceOnDemandSessionID=%1$s job-name=%2$s", sessionId, testName));
    }

    /**
     * @param testResult Test result for the test that just passed.
     */
    @Override
    public void onTestSuccess(ITestResult testResult) {
        SauceOnDemandSessionIdProvider sessionIdProvider = (SauceOnDemandSessionIdProvider) testResult.getInstance();
        String sessionId = sessionIdProvider.getSessionId();
        printOutSessionID(sessionId, testResult.getMethod().getMethodName());
        markJobStatus(sessionId, true);
        super.onTestSuccess(testResult);
    }
    
    /**
     * JIRA Integration methods
     */
    public void createJIRAIssue(String failedMethod, String description, String sessionId) throws Exception {
		JSONObject sauceLabsJob = this.getSauceLabsJobDetails(sessionId);
		
		StringBuilder jiraBugDescription = new StringBuilder();
		jiraBugDescription.append("SauceLabs JobId: " + sessionId);
		jiraBugDescription.append("\n");
		jiraBugDescription.append("OS: " + sauceLabsJob.get("os"));
		jiraBugDescription.append("\n");
		jiraBugDescription.append("Browser: " + sauceLabsJob.get("browser"));
		jiraBugDescription.append("\n");
		jiraBugDescription.append("Browser Version: " + sauceLabsJob.get("browser_version"));
		jiraBugDescription.append("\n\n");
		jiraBugDescription.append(description);
		
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
								.put("description", jiraBugDescription.toString()).put("issuetype", new JSONObject().put("name", "Bug")))
				.toString();

		System.out.println(jsonString);

		OutputStream os = connection.getOutputStream();
		os.write(jsonString.toString().getBytes("UTF-8"));
		InputStream fis = connection.getInputStream();

		StringWriter response = new StringWriter();
		String encoding = "UTF-8";
		IOUtils.copy(fis, response, encoding);
		System.out.println(response.toString());
		JSONObject jsonData = new JSONObject(response.toString());
		String issueKey = jsonData.getString("key");
		this.addAttachmentToIssue(issueKey, "downloads/test/0012screenshot.png");
	}
	
	public boolean addAttachmentToIssue(String issueKey, String fullfilename) throws IOException{

//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		
//		HttpPost httppost = new HttpPost("https://wbdigital.atlassian.net/rest/api/2/issue/"+issueKey+"/attachments");
//		httppost.setHeader("X-Atlassian-Token", "nocheck");
//		httppost.setHeader("Authorization", "Basic amlnaXNoLmJlbGFuaUB3YmNvbnN1bHRhbnQuY29tOltqYmVsMDkwMTgyXQ==");
//		
//		File fileToUpload = new File(fullfilename);
//		FileBody fileBody = new FileBody(fileToUpload);
//		
//		HttpEntity entity = MultipartEntityBuilder.create()
//				.addPart("file", fileBody)
//				.build();
//		
//		httppost.setEntity(entity);
//        String mess = "executing request " + httppost.getRequestLine();
//        //logger.info(mess);
//        
//        CloseableHttpResponse response;
//		
//        try {
//			response = httpclient.execute(httppost);
//		} finally {
//			httpclient.close();
//		}
//        
//		if(response.getStatusLine().getStatusCode() == 200)
//			return true;
//		else
			return false;

	}
	
	public JSONObject getSauceLabsJobDetails(String jobId) throws Exception{
		//https://jbelaniWB:8c0ad879-a2ae-416c-850e-8d14d186a13d@saucelabs.com/rest/v1/users/jobs/2e60efae941644b5b5262b9fd9f9a3c9
		String url = "https://flixstervideo:4bff95a5-6938-405d-a3ae-856dc7e86da0@saucelabs.com/rest/v1/flixstervideo/jobs/" + jobId;

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
		JSONObject sauceLabsJobJSONObject = new JSONObject(response.toString());
		
		return sauceLabsJobJSONObject;
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
        String fileURL = "https://saucelabs.com/rest/v1/flixstervideo/jobs/" + jobId  + "/assets/" + screenshotsArray.getString(screenshotsArray.length() - 1);
        this.downloadFile(fileURL, "downloads/test");
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
