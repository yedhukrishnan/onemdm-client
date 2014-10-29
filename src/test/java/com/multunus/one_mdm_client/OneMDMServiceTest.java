package com.multunus.one_mdm_client;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

@RunWith(RobolectricTestRunner.class)
public class OneMDMServiceTest {

	@Spy OneMDMService oneMDMService;
	@Mock RegistrationTask registrationTask;
	@Mock ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		oneMDMService = Mockito.spy(new OneMDMService());
		registrationTask = Mockito.mock(RegistrationTask.class);
		scheduledThreadPoolExecutor = Mockito.mock(ScheduledThreadPoolExecutor.class);
		Mockito.doReturn(registrationTask).when(oneMDMService).getRegistrationTask();
	}
	
	@Test
	public void shouldRegisterTheDeviceIfNetworkIsAvailable() {	
		setNetworkAvailability(true);
		setDeviceRegistrationStatus(false);
		oneMDMService.onCreate();
		Mockito.verify(registrationTask).execute(Mockito.anyString());
	}
	
	@Test
	public void shouldNotRegisterTheDeviceIfNetworkIsNotAvailable() {
		setNetworkAvailability(false);
		setDeviceRegistrationStatus(false);
		oneMDMService.onCreate();
		Mockito.verify(registrationTask, Mockito.times(0)).execute(Mockito.anyString());
	}
	
	@Test
	public void shouldRegisterTheDeviceIfItsNotRegistered() {
		setNetworkAvailability(true);
		setDeviceRegistrationStatus(false);
		oneMDMService.onCreate();
		Mockito.verify(registrationTask).execute(Mockito.anyString());
	}
	
	@Test
	public void shouldNotRegisterTheDeviceIfItsAlreadyRegistered() {
		setNetworkAvailability(true);
		setDeviceRegistrationStatus(true);
		oneMDMService.onCreate();
		Mockito.verify(registrationTask, Mockito.times(0)).execute(Mockito.anyString());
	}
	
	@Test
	public void shouldSetTheRegistrationInfoAndStopTheService() {
		int deviceID = 123;
		oneMDMService.setRegistrationInfo(deviceID);
		Assert.assertEquals(deviceID, oneMDMService.getSharedPreferences(OneMDMService.ONE_MDM_PREFERENCE_KEY, oneMDMService.MODE_PRIVATE).getInt("deviceID", 123));
	}
	
	@Test
	public void shouldScheduleHeartbeatsAtFixedInterval() {
		setNetworkAvailability(true);
		setDeviceRegistrationStatus(true);
		Mockito.doReturn(scheduledThreadPoolExecutor).when(oneMDMService).getScheduledThreadPoolExecutor();
		oneMDMService.onCreate();
		Mockito.verify(scheduledThreadPoolExecutor).scheduleAtFixedRate(Mockito.any(Runnable.class), Mockito.anyLong(), Mockito.eq(Config.HEARTBEAT_INTERVAL), Mockito.eq(TimeUnit.SECONDS));
	}
	
	private void setNetworkAvailability(boolean status) {
		Mockito.doReturn(status).when(oneMDMService).isNetworkAvailable();
	}
	
	private void setDeviceRegistrationStatus(boolean status) {
		Mockito.doReturn(status).when(oneMDMService).isDeviceRegistered();
	}

}
