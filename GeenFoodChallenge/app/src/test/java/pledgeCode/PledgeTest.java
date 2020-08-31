package pledgeCode;

import android.support.v4.media.AudioAttributesCompat;

import org.junit.Test;

import static org.junit.Assert.*;
public class PledgeTest {

    @Test
    public void getName() {
        Pledge testPledge = new Pledge("myPledge","myCity", 1, 1234L);

        assertEquals("myPledge", testPledge.getName());
        assertNotEquals("",testPledge.getName());
    }

    @Test
    public void setName() {
        Pledge testPledge = new Pledge("myPledge","myCity", 1, 1234L);

        testPledge.setName("newPledge");

        assertEquals("newPledge", testPledge.getName());
        assertNotEquals("", testPledge.getName());
    }

    @Test
    public void getMunicipality() {
        Pledge testPledge = new Pledge("myPledge","myCity", 1, 1234L);

        assertEquals("myCity", testPledge.getMunicipality());
        assertNotEquals("", testPledge.getMunicipality());
    }

    @Test
    public void setMunicipality() {
        Pledge testPledge = new Pledge("myPledge","myCity", 1, 1234L);

        testPledge.setName("newCity");

        assertEquals("newCity", testPledge.getName());
        assertNotEquals("", testPledge.getName());
    }

    @Test
    public void getCo2Pledged() {
        Pledge testPledge = new Pledge("myPledge","myCity", 1, 1234L);

    }

    @Test
    public void setCo2Pledged() {
    }

    @Test
    public void getTimeStamp() {
        Pledge testPledge = new Pledge("myPledge","myCity", 1, 1234L);

        long val = testPledge.getTimeStamp();

        assertEquals(1234L, val);
    }
}