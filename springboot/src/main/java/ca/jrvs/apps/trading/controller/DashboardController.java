package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.PortfolioView;
import ca.jrvs.apps.trading.model.TraderAccountView;
import ca.jrvs.apps.trading.service.DashboardService;
import ca.jrvs.apps.trading.util.ResponseExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

  private DashboardService dashboardService;

  @Autowired
  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

  /**
   * Show trader profile by traderId
   * trader profile should include:
   * - trader object
   * - account object
   * - traderId and accountId should be identical
   * @param traderId
   * @return TraderAccountView
   */
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/profile/traderId/{traderId}", produces = {
      MediaType.APPLICATION_JSON_UTF8_VALUE})
  public TraderAccountView getAccount(@PathVariable Integer traderId) {
    try {
      return dashboardService.getTraderAccount(traderId);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  /**
   * Show portfolio by traderId
   * @param traderId
   * @return PortfolioView
   */
  @GetMapping(path = "/portfolio/traderId/{traderId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public PortfolioView getPortfolioView(@PathVariable Integer traderId) {
    try {
      return dashboardService.getProfileViewByTraderId(traderId);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }
}

