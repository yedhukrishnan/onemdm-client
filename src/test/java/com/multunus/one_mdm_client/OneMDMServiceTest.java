package com.multunus.one_mdm_client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.preference.PreferenceManager;

@RunWith(RobolectricTestRunner.class)
public class OneMDMServiceTest {

	@Spy OneMDMService oneMDMService;
	@Mock RegistrationTask registrationTask;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		oneMDMService = Mockito.spy(new OneMDMService());
		registrationTask = Mockito.mock(RegistrationTask.class);
		Mockito.doReturn(registrationTask).when(oneMDMService).getRegistrationTask();
	}
	
	@Test
	public void shouldRegisterTheDeviceIfNetworkIsAvailable() {	
		Mockito.doReturn(false).when(oneMDMService).isDeviceRegistered();
		Mockito.doReturn(true).when(oneMDMService).isNetworkAvailable();
		oneMDMService.onCreate();
		Mockito.verify(registrationTask).execute(Mockito.anyString());
	}
	
	@Test
	public void shouldNotRegisterTheDeviceIfNetworkIsNotAvailable() {
		Mockito.doReturn(false).when(oneMDMService).isDeviceRegistered();
		Mockito.doReturn(false).when(oneMDMService).isNetworkAvailable();
		oneMDMService.onCreate();
		Mockito.verify(registrationTask, Mockito.times(0)).execute(Mockito.anyString());
	}
	
	@Test
	public void shouldRegisterTheDeviceIfItsNotRegistered() {
		Mockito.doReturn(true).when(oneMDMService).isNetworkAvailable();
		Mockito.doReturn(false).when(oneMDMService).isDeviceRegistered();
		oneMDMService.onCreate();
		Mockito.verify(registrationTask).execute(Mockito.anyString());
	}
	
	@Test
	public void shouldNotRegisterTheDeviceIfItsAlreadyRegistered() {
		Mockito.doReturn(true).when(oneMDMService).isNetworkAvailable();
		Mockito.doReturn(true).when(oneMDMService).isDeviceRegistered();
		oneMDMService.onCreate();
		Mockito.verify(registrationTask, Mockito.times(0)).execute(Mockito.anyString());
	}
	
	@Test
	public void shouldSetTheRegistrationInfoAndStopTheService() {
		String deviceName = "1#testDevice";
		oneMDMService.setRegistrationInfo(deviceName);
		Assert.assertEquals(deviceName, oneMDMService.getSharedPreferences(OneMDMService.ONE_MDM_PREFERENCE_KEY, oneMDMService.MODE_PRIVATE).getString("name", "default"));
	}

}
