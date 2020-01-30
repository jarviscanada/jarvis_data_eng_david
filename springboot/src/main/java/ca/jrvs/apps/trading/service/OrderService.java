package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.*;
import ca.jrvs.apps.trading.model.SecurityOrder;
import java.math.RoundingMode;
import java.security.Security;
import java.text.DecimalFormat;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

  private AccountDao accountDao;
  private SecurityOrderDao securityOrderDao;
  private QuoteDao quoteDao;
  private PositionDao positionDao;
  private DecimalFormat df;

  @Autowired
  public OrderService(AccountDao accountDao,
      SecurityOrderDao securityOrderDao, QuoteDao quoteDao,
      PositionDao positionDao) {
    this.accountDao = accountDao;
    this.securityOrderDao = securityOrderDao;
    this.quoteDao = quoteDao;
    this.positionDao = positionDao;
    df = new DecimalFormat("#.##");
    df.setRoundingMode(RoundingMode.CEILING);
  }

  /**
   * Execute a market order
   * <p>
   * - validate the order (e.g. size, and ticker) - Create a securityOrder (for security_order
   * table) - Handle buy or sell order - buy order : check account balance (calls helper method) -
   * sell order : check position for the ticker/symbol (calls helper method) - (olease don't forget
   * to update securityOrder.status) - Save and return securityOrder
   * <p>
   * NOTE: you will need to some helper methods (protected or private)
   *
   * @param orderDto market order
   * @return SecurityOrder from security_order table
   * @throws IllegalArgumentException for invalid input
   */
  public SecurityOrder executeMarketOrder(MarketOrderDto orderDto) throws DataAccessException {
    if (orderDto == null) {
      throw new IllegalArgumentException("orderDto is null");
    }
    if (!quoteDao.existsById(orderDto.getTicker())) {
      throw new IllegalArgumentException(
          "orderDto ticker: " + orderDto.getTicker() + " could not be found.");
    }
    if (!accountDao.existsById(orderDto.getAccountId())) {
      throw new IllegalArgumentException(
          "orderDto ticker: " + orderDto.getAccountId() + " does not exist.");
    }

    Quote quote = quoteDao.findById(orderDto.getTicker()).get();
    SecurityOrder securityOrder = null;
    if (!securityOrderDao.existsById(orderDto.getAccountId())) {
      securityOrder = new SecurityOrder();
      securityOrder.setId(null);
      securityOrder.setTicker(orderDto.getTicker().toUpperCase());
      securityOrder.setStatus("CREATED");
      securityOrder.setNotes("Recently created Security");
      securityOrder.setAccount_id(orderDto.getAccountId());
      securityOrder.setSize(0);
    } else {
      securityOrder = securityOrderDao.findByAccountId(orderDto.getAccountId()).get();
    }
    Account account = accountDao.findById(orderDto.getAccountId()).get();

    // Buyer use ask_price, seller as bid_price
    if (orderDto.getSize() >= 0) {
      if (orderDto.getSize() > quote.getAskSize()) {
        throw new IllegalArgumentException("Order size of: " + orderDto.getSize()
            + " is greater than volume sellers are willing to sell: " + quote.getAskSize());
      }
      securityOrder.setPrice(quote.getAskPrice());
      handleBuyMarketOrder(orderDto, securityOrder, account);

      if (! securityOrder.getNotes().contains("Failed")) {
        quote.setAskSize(quote.getAskSize() - orderDto.getSize());
      }
    } else {
      if (Math.abs(orderDto.getSize()) > quote.getBidSize()) {
        throw new IllegalArgumentException("Order size selling of: " + Math.abs(orderDto.getSize())
            + " is greater than volume buyers are willing to buy: " + quote.getBidSize());
      }
      securityOrder.setPrice(quote.getBidPrice());
      handleSellMarketOrder(orderDto, securityOrder, account);

      if (! securityOrder.getNotes().contains("Failed")) {
        quote.setBidSize(quote.getBidSize() + Math.abs(orderDto.getSize()));
      }
    }
    quoteDao.save(quote);
    return securityOrder;
  }

  /**
   * Helper method that execute a sell order
   *
   * @param marketOrderDto user order
   * @param securityOrder  to be  saved in data database
   * @param account        account
   */
  protected void handleBuyMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder,
      Account account) {

    Double balance = account.getAmount();

    Double cost = marketOrderDto.getSize() * securityOrder.getPrice();
    // fulfill order
    if (balance > cost) {
      securityOrder.setSize(securityOrder.getSize() + marketOrderDto.getSize());
      String strAmount = df.format(balance - cost);
      account.setAmount(Double.valueOf(strAmount));
      accountDao.save(account);
      String msg = "Bought " + marketOrderDto.getSize() + " orders at: " + cost;
      securityOrder.setNotes(msg);
    } else {
      String error =
          "Failed to buy " + marketOrderDto.getSize() + " orders. Insufficient funds" ;
      securityOrder.setNotes(error);
    }

    securityOrder.setStatus("FILLED");
    SecurityOrder newSecurityOrder = securityOrderDao.save(securityOrder);
    securityOrder.setId(newSecurityOrder.getId());
    securityOrder.setNotes(newSecurityOrder.getNotes());
    securityOrder.setSize(newSecurityOrder.getSize());


  }

  /**
   * Helper method that execute a sell order
   *
   * @param marketOrderDto user order
   * @param securityOrder  to be saved in data database
   * @param account        account
   */
  protected void handleSellMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder,
      Account account) {

    Double balance = account.getAmount();
    Integer marketOrderSold = Math.abs(marketOrderDto.getSize());

    Double revenue = marketOrderSold * securityOrder.getPrice();
    // fulfill order
    List<Position> positions = positionDao.findPosById(marketOrderDto.getAccountId());
    boolean enoughVolumeToSell = false;

    for (Position position : positions) {
      if (position == null) { continue; }

      if (position.getTicker().equals(marketOrderDto.getTicker())) {
        if (position.getPosition() > marketOrderSold) enoughVolumeToSell = true;
        break;
      }
    }

    if (enoughVolumeToSell) {
      securityOrder.setSize(securityOrder.getSize() - marketOrderSold);
      String strAmount = df.format(balance + revenue);
      account.setAmount(Double.valueOf(strAmount));
      accountDao.save(account);
      String msg = "Sold " + marketOrderSold + " orders at: " + revenue;
      securityOrder.setNotes(msg);
    } else {
      String error =
          "Failed to sell " + marketOrderSold + " orders. Insufficient amount to sell." ;
      securityOrder.setNotes(error);
    }
    SecurityOrder newSecurityOrder = securityOrderDao.save(securityOrder);
    securityOrder.setId(newSecurityOrder.getId());
    securityOrder.setNotes(newSecurityOrder.getNotes());
    securityOrder.setSize(newSecurityOrder.getSize());
  }

}
