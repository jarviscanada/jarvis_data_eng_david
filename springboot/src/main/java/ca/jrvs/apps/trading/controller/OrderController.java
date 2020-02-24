package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.MarketOrderDto;
import ca.jrvs.apps.trading.model.SecurityOrder;
import ca.jrvs.apps.trading.service.OrderService;
import ca.jrvs.apps.trading.util.ResponseExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/order")
public class OrderController {

  private OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) { this.orderService = orderService; }

  /**
   * Submits a market order
   * @param orderDto market order
   * @return SecurityOrder
   * @throws 404 if accountId or ticker is not found
   * @throws 400 if unable to deposit due to user input
   */
  @PostMapping(path = "/marketOrder")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public SecurityOrder postMarketOrder(@RequestBody MarketOrderDto orderDto) {
   try {
     return orderService.executeMarketOrder(orderDto);
   } catch (Exception e) {
     throw ResponseExceptionUtil.getResponseStatusException(e);
   }
  }
}
