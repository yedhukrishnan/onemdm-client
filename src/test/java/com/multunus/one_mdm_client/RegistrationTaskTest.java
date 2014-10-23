package com.multunus.one_mdm_client;
import static org.robolectric.util.Strings.fromStream;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.multunus.one_mdm_client.RegistrationTask;


@RunWith(RobolectricTestRunner.class)
public class RegistrationTaskTest {
	@Mock OneMDMService oneMDMService;
	String jsonResponse;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		jsonResponse = "{\"device\": \"1#TestDevice\"}";
	}
	
	@Test
	public void shouldRegisterTheDeviceToTheServer() throws Exception {
		String deviceName = "Test Device 123";
		HttpResponse httpResponse = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), 200, "OK");
		httpResponse.setEntity(new StringEntity(jsonResponse));
		Robolectric.addPendingHttpResponse(httpResponse);
		
		new RegistrationTask(oneMDMService).execute(deviceName);
		Robolectric.runBackgroundTasks();
		
		HttpPost postRequest = (HttpPost)Robolectric.getLatestSentHttpRequest();	
		String postBody = fromStream(postRequest.getEntity().getContent());
		JSONObject registrationData = new JSONObject(postBody);
		
		Assert.assertEquals(deviceName, registrationData.get("name"));
		Mockito.verify(oneMDMService).setRegistrationInfo(Mockito.anyString());
	}
	
}
