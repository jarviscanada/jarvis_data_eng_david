package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.*;
import ca.jrvs.apps.trading.model.SecurityOrder;
import java.security.Security;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  public OrderService(AccountDao accountDao,
      SecurityOrderDao securityOrderDao, QuoteDao quoteDao,
      PositionDao positionDao) {
    this.accountDao = accountDao;
    this.securityOrderDao = securityOrderDao;
    this.quoteDao = quoteDao;
    this.positionDao = positionDao;
  }

  /**
   * Execute a market order
   *
   *  - validate the order (e.g. size, and ticker)
   *  - Create a securityOrder (for security_order table)
   *  - Handle buy or sell order
   *    - buy order : check account balance (calls helper method)
   *    - sell order : check position for the ticker/symbol (calls helper method)
   *    - (olease don't forget to update securityOrder.status)
   *  - Save and return securityOrder
   *
   *  NOTE: you will need to some helper methods (protected or private)
   *
   * @param orderDto market order
   * @return SecurityOrder from security_order table
   * @throws org.springframework.dao.DataAccessException if unable to get data from DAO
   * @throws IllegalArgumentException for invalid input
   */
  public SecurityOrder executeMarketOrder(MarketOrderDto orderDto) {


    SecurityOrder securityOrder = new SecurityOrder();
    return null;
  }

  /**
   * Helper method that execute a sell order
   * @param marketOrderDto user order
   * @param securityOrder to be  saved in data database
   * @param account account
   */
  protected void handleBuyMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder, Account account) {

  }

  /**
   * Helper method that execute a sell order
   * @param marketOrderDto user order
   * @param securityOrder to be saved in data database
   * @param account account
   */
  protected void handleSellMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder, Account account) {

  }

}
