package om.fdmgroup.ses.modelTest;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.fdmgroup.ses.model.OwnedShare;
import com.fdmgroup.ses.model.TransactionHistory;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OwnedShareTest {

	@Before
	public void setUp() throws Exception {
		
	}

	/**
	 * getAveragePurchasePrice() calculates the average price of purchases of this share
	 * # Accurate to two d.p.
	 * # Ignores sales 
	 */
	@Test
	public void getAveragePurchasePriceTest() {
		
		// Setup purchases/sales: avg. purhchase price £1.50
		List<TransactionHistory> txHistory = new ArrayList<>();
		
		TransactionHistory cheapPurchase = new TransactionHistory();
		cheapPurchase.setQuantity(100l);
		cheapPurchase.setValue(new BigDecimal(100)); // unit price £1
		
		TransactionHistory expensivePurchase = new TransactionHistory();
		expensivePurchase.setQuantity(100l);
		expensivePurchase.setValue(new BigDecimal(200)); // unit price £2
		
		TransactionHistory sale = new TransactionHistory();
		sale.setQuantity(-100l);
		sale.setValue(new BigDecimal(200000)); // Should be ignored
		
		txHistory.addAll(Arrays.asList(cheapPurchase, expensivePurchase, sale));
		
		// Setup owned share record
		OwnedShare os = new OwnedShare();
		os.setTransactionHistory(txHistory);
		
		// Get avg. purchase price
		BigDecimal result = os.getAveragePurchasePrice();
		
		// Confirm result
		assertTrue("Wrong result. Expected: 1.50; actual: " + result,
				result.compareTo(new BigDecimal(1.50)) == 0);
	}
	
	/**
	 * getAveragePurchasePrice() doesn't calculate if averagePurchasePrice isn't null 
	 */
	@Test
	public void getAveragePurchasePriceAlreadyCalculatedTest() {
		OwnedShare os = new OwnedShare();
		os.setAveragePurchasePrice(new BigDecimal(777));
		BigDecimal result = os.getAveragePurchasePrice();
		assertTrue("Average purchase price was recalculated", result.compareTo(new BigDecimal(777)) == 0);
	}

}
