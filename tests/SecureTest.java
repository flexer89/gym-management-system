import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import utils.Secure;

@RunWith(org.junit.runners.JUnit4.class)
public class SecureTest {
    @Test
    public void testHashWithSalt() {
        String input = "password";
        String salt = "somesalt";
        String expectedHash = "6714b60328784cf6e57c2d9133c0def4e45a45f25853df25f1ee92cc6fea8a3f";
        
        String actualHash = Secure.hashWithSalt(input, salt);
        
        Assert.assertEquals(expectedHash, actualHash);
    }
    
    @Test
    public void testHashWithSaltEmptyInput() {
        String input = "";
        String salt = "somesalt";
        String expectedHash = "cac98b50ede4252ac97d5148a4137aa82d83103335a1f02bd38538a8f37496c6";
        
        String actualHash = Secure.hashWithSalt(input, salt);
        
        Assert.assertEquals(expectedHash, actualHash);
    }
    
    @Test
    public void testHashWithSaltEmptySalt() {
        String input = "password";
        String salt = "";
        String expectedHash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
        
        String actualHash = Secure.hashWithSalt(input, salt);
        
        Assert.assertEquals(expectedHash, actualHash);
    }
    
    @Test
    public void testHashWithSaltEmptyInputAndSalt() {
        String input = "";
        String salt = "";
        String expectedHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        
        String actualHash = Secure.hashWithSalt(input, salt);
        
        Assert.assertEquals(expectedHash, actualHash);
    }
}