package com.multunus.one_mdm_client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.RobolectricTestRunner;

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
		Mockito.doReturn(true).when(oneMDMService).isNetworkAvailable();
		oneMDMService.onCreate();
		Mockito.verify(registrationTask).execute(Mockito.anyString());
	}
	
	@Test
	public void shouldNotRegisterTheDeviceIfNetworkIsNotAvailable() {	
		Mockito.doReturn(false).when(oneMDMService).isNetworkAvailable();
		oneMDMService.onCreate();
		Mockito.verify(registrationTask, Mockito.times(0)).execute(Mockito.anyString());
	}

}
