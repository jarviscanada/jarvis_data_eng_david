package ca.jrvs.apps.trading.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

  //mock all dependencies
  @Mock
  private AccountDao accountDao;
  @Mock
  private SecurityOrderDao securityOrderDao;
  @Mock
  private QuoteDao quoteDao;
  @Mock
  private PositionDao positionDao;

  //injecting mocked dependencies to the testing class via constructor
  @InjectMocks
  private OrderService orderService;

  private Quote q1;
  private SecurityOrder savedSecurityOrder;
  private MarketOrderDto marketOrderDtoBuy;
  private List<Position> savedPositions;
  private List<Account> savedAccounts;
  private Quote q1_buy;

  @Before
  public void init() {
    savedPositions = new ArrayList<Position>();
    savedAccounts = new ArrayList<Account>();
    //saveQuote
    q1 = new Quote();
    q1.setTicker("IBM");
    q1.setAskPrice(22.3d);
    q1.setAskSize(Long.valueOf(22));
    q1.setBidPrice(44.5d);
    q1.setBidSize(44L);
    q1.setLastPrice(99.3d);

    //need to define new quotes
    q1_buy = q1;

    marketOrderDtoBuy = new MarketOrderDto();
    marketOrderDtoBuy.setAccountId(1);
    marketOrderDtoBuy.setSize(2);
    marketOrderDtoBuy.setTicker("IBM");

    Account savedAccount = new Account();
    savedAccount.setTrader_id(1);
    savedAccount.setId(1);
    savedAccount.setAmount(50.0d);
    savedAccounts.add(savedAccount);

    //apply mocks - get save to work
    when(quoteDao.findById(any(String.class))).thenReturn(java.util.Optional.ofNullable(q1));
    when(quoteDao.existsById("IBM")).thenReturn(true);
    when(quoteDao.save(q1)).thenReturn(q1);
    when(quoteDao.save(q1_buy)).thenReturn(q1_buy);

    when(accountDao.findById(any(Integer.class))).thenReturn(Optional.of(savedAccount));
    when(accountDao.existsById(1)).thenReturn(true);
    when(accountDao.save(savedAccount)).thenReturn(savedAccount);

  }

  @Test
  public void serviceBuy() {
    savedSecurityOrder = new SecurityOrder();
    savedSecurityOrder.setStatus("FILLED");
    savedSecurityOrder.setNotes("Bought 2 orders at: 44.6");
    savedSecurityOrder.setSize(2);
    savedSecurityOrder.setAccount_id(1);
    savedSecurityOrder.setId(1);
    savedSecurityOrder.setTicker("IBM");
    savedSecurityOrder.setPrice(22.3d);

    Position savedPosition = new Position();
    savedPosition.setId(1);
    savedPosition.setTicker("IBM");
    savedPosition.setPosition(2);
    savedPositions.add(savedPosition);

    when(securityOrderDao.save(any(SecurityOrder.class))).thenReturn(savedSecurityOrder);
    when(securityOrderDao.existsById(1)).thenReturn(false);
    when(securityOrderDao.findByAccountId(any(Integer.class))).thenReturn(
        Optional.ofNullable(savedSecurityOrder));
    when(positionDao.findPosById(any(Integer.class))).thenReturn(savedPositions);

    SecurityOrder securityOrder = orderService.executeMarketOrder(marketOrderDtoBuy);
    assertEquals(securityOrder, savedSecurityOrder);
    assertEquals(q1_buy.getAskSize(), new Long(20));

  }

  @Test
  public void serviceBuyNotEnough() {
    savedSecurityOrder = new SecurityOrder();
    savedSecurityOrder.setStatus("FILLED");
    savedSecurityOrder.setNotes("Failed to buy 8 orders. Insufficient funds");
    savedSecurityOrder.setSize(0);
    savedSecurityOrder.setAccount_id(1);
    savedSecurityOrder.setId(1);
    savedSecurityOrder.setTicker("IBM");
    savedSecurityOrder.setPrice(22.3d);

    Position savedPosition = new Position();
    savedPosition.setId(1);
    savedPosition.setTicker("IBM");
    savedPosition.setPosition(2);
    savedPositions.add(savedPosition);

    marketOrderDtoBuy.setSize(8);

    when(securityOrderDao.save(any(SecurityOrder.class))).thenReturn(savedSecurityOrder);
    when(securityOrderDao.existsById(1)).thenReturn(false);

    SecurityOrder securityOrder = orderService.executeMarketOrder(marketOrderDtoBuy);
    assertEquals(securityOrder.getSize(),new Integer(0));
    assertEquals(securityOrder, savedSecurityOrder);
    assertEquals(q1_buy.getAskSize(), new Long(22));

  }

  @Test
  public void serviceSell() {
    serviceBuy();
    SecurityOrder newSavedSecurityOrder = new SecurityOrder();
    newSavedSecurityOrder.setAccount_id(1);
    newSavedSecurityOrder.setId(1);
    newSavedSecurityOrder.setTicker("IBM");
    newSavedSecurityOrder.setPrice(44.5d);
    newSavedSecurityOrder.setStatus("FILLED");
    newSavedSecurityOrder.setNotes("Sold 1 orders at: 44.5");
    newSavedSecurityOrder.setSize(1);

    Position savedPosition = new Position();
    savedPosition.setId(1);
    savedPosition.setTicker("IBM");
    savedPosition.setPosition(1);
    savedPositions.add(savedPosition);

    marketOrderDtoBuy.setSize(-1);

    when(securityOrderDao.save(any(SecurityOrder.class))).thenReturn(savedSecurityOrder);
    when(securityOrderDao.existsById(1)).thenReturn(true);
    when(securityOrderDao.findByAccountId(any(Integer.class))).thenReturn(
        Optional.ofNullable(savedSecurityOrder));
    when(positionDao.findPosById(any(Integer.class))).thenReturn(savedPositions);

    SecurityOrder securityOrder = orderService.executeMarketOrder(marketOrderDtoBuy);
    assertEquals(securityOrder.getSize(),new Integer(1));
    assertEquals(securityOrder, newSavedSecurityOrder);
    assertEquals(q1_buy.getBidSize(), new Long(45));
  }

  @Test
  public void sellTooMuch() {
    SecurityOrder newSavedSecurityOrder = new SecurityOrder();
    newSavedSecurityOrder.setAccount_id(1);
    newSavedSecurityOrder.setId(1);
    newSavedSecurityOrder.setTicker("IBM");
    newSavedSecurityOrder.setPrice(44.5d);
    newSavedSecurityOrder.setStatus("CREATED");
    newSavedSecurityOrder.setNotes("Failed to sell 8 shares. Insufficient amount to sell.");
    newSavedSecurityOrder.setSize(0);

    Position savedPosition = new Position();
    savedPosition.setId(1);
    savedPosition.setTicker("IBM");
    savedPosition.setPosition(1);
    savedPositions.add(savedPosition);

    marketOrderDtoBuy.setSize(-8);

    when(securityOrderDao.save(any(SecurityOrder.class))).thenReturn(newSavedSecurityOrder);
    when(securityOrderDao.existsById(1)).thenReturn(false);
    when(positionDao.findPosById(any(Integer.class))).thenReturn(savedPositions);

    SecurityOrder securityOrder = orderService.executeMarketOrder(marketOrderDtoBuy);
    assertEquals(securityOrder.getSize(),new Integer(0));
    assertEquals(securityOrder, newSavedSecurityOrder);
    assertEquals(q1_buy.getBidSize(), new Long(44));
  }

  @After
  public void clear() {
    savedSecurityOrder = null;
    savedPositions.clear();
    savedAccounts.clear();

  }

}
