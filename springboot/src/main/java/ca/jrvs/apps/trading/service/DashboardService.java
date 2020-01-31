package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.*;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DashboardService {

  private TraderDao traderDao;
  private PositionDao positionDao;
  private AccountDao accountDao;
  private QuoteDao quoteDao;

  @Autowired
  public DashboardService(TraderDao traderDao, PositionDao positionDao,
      AccountDao accountDao, QuoteDao quoteDao) {
    this.traderDao = traderDao;
    this.positionDao = positionDao;
    this.accountDao = accountDao;
    this.quoteDao = quoteDao;
  }

  /**
   * Create and return a traderAccountView by trader ID - get trader account by id - get trader info
   * by id - create and return a traderAccountView
   *
   * @param traderId must not be null
   * @return traderAccountView
   * @throws IllegalArgumentException if traderId is null or not found
   */
  public TraderAccountView getTraderAccount(Integer traderId) {
    Trader trader = traderDao.findById(traderId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid traderId"));
    Account account = findAccountByTraderId(traderId);

    return new TraderAccountView(trader, account);
  }

  /**
   * Create and return portfolioView by trader ID - get account by trader id - get positions by
   * account id - create and return a portfolioView
   *
   * @param traderId must not be null
   * @return portfolioView
   * @throws IllegalArgumentException if traderId is null or not found
   */
  public PortfolioView getProfileViewByTraderId(Integer traderId) {
    if (traderId == null) {
      throw new IllegalArgumentException("traderId is null");
    }
    List<SecurityRow> securityOrders = new ArrayList<SecurityRow>();
    List<Position> positions = null;
    Account account = findAccountByTraderId(traderId);
    positions = positionDao.findPosById(account.getId());

    if (positions != null) {
      for (Position position : positions) {
        String ticker = position.getTicker();
        Quote quote = quoteDao.findById(ticker).get();
        securityOrders.add(new SecurityRow(position, quote, ticker));
      }
    }
    return new PortfolioView(securityOrders);
  }

  /**
   * @throws IllegalArgumentException if traderId is not found
   */
  private Account findAccountByTraderId(Integer traderId) {
    return accountDao.findAccountByTraderId(traderId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid traderId"));
  }

}
