package example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.IExecutionListener;

public class ExecutionListener implements IExecutionListener {

	@Override
	public void onExecutionFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExecutionStart() {
		// TODO Auto-generated method stub
		System.out.println("TestNG is starting.");
	}

	

}
