import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.multunus.one_mdm_client.OneMDMActivity;
import com.multunus.one_mdm_client.R;


@RunWith(RobolectricTestRunner.class)
public class OneMDMActivityTest {
	
	OneMDMActivity oneMDMActivity;
	
	@Before
	public void setUp() {
		this.oneMDMActivity = Robolectric.buildActivity(OneMDMActivity.class).create().get();
	}
	
	@Test
    public void shouldHaveHappySmiles() throws Exception 
    {
        String hello = this.oneMDMActivity.getString(R.string.hello_world);
        Assert.assertEquals(hello, "Hello world!");
    }

}
