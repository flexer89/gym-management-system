import org.junit.Assert;
import org.junit.Test;
import utils.GenerateCard;

public class GenerateCardTest {
    @Test
    public void testGenerateClientCardNumber() {
        String cardNumber = GenerateCard.generateClientCardNumber();
        Assert.assertNotNull(cardNumber);
        Assert.assertEquals(GenerateCard.getCardNumberLength(), cardNumber.length());
        Assert.assertTrue(cardNumber.startsWith(GenerateCard.getClientNumberPrefix()));
    }

    @Test
    public void testGenerateEmployeeCardNumber() {
        String cardNumber = GenerateCard.generateEmployeeCardNumber();
        Assert.assertNotNull(cardNumber);
        Assert.assertEquals(GenerateCard.getCardNumberLength(), cardNumber.length());
        Assert.assertTrue(cardNumber.startsWith(GenerateCard.getEmployeeNumberPrefix()));
    }
}