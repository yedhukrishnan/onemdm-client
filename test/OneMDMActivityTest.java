import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.content.Intent;

import com.multunus.one_mdm_client.OneMDMActivity;
import com.multunus.one_mdm_client.OneMDMService;


@RunWith(RobolectricTestRunner.class)
public class OneMDMActivityTest {
	
	OneMDMActivity oneMDMActivity;
	
	@Before
	public void setUp() {
		this.oneMDMActivity = Robolectric.buildActivity(OneMDMActivity.class).create().get();
	}
	
	@Test
    public void shouldStartTheOneMDMService() throws Exception 
    {
		Intent intent = Robolectric.shadowOf(oneMDMActivity).getNextStartedService();
		Assert.assertNotNull(intent);
		Assert.assertEquals(OneMDMService.class.getName(), intent.getComponent().getClassName());
    }

}
