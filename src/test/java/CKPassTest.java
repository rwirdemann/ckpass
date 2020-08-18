import de.codekeepers.CKPass;
import org.junit.Assert;
import org.junit.Test;

public class CKPassTest {

    @Test
    public void HappyPath() {
        CKPass pass = new CKPass();
        Assert.assertTrue(pass.build("123456789"));
    }
}
