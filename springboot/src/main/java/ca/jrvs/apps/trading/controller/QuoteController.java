package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.model.IexQuote;
import ca.jrvs.apps.trading.service.QuoteService;
import ca.jrvs.apps.trading.util.ResponseExceptionUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

//@Api(value = "quote", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Controller
@RequestMapping("/quote")
public class QuoteController {

  private QuoteService quoteService;

  @Autowired
  public QuoteController(QuoteService quoteService) {
    this.quoteService = quoteService;
  }

  /**
   * Update market data
   */
  @PutMapping(path= "/iexMarketData")
  @ResponseStatus(HttpStatus.OK)
  public void updateMarketData() {
    try {
      quoteService.updateMarketData();
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  /**
   * Returns IexQuote corresponding to ticker
   * @param ticker String
   * @return IexQuote object
   * @throws Exception throw with relation to HTTP status code
   */
  //@ApiOperation(value = "Show iexQuote", notes = "Show iexQuote for a given ticker/symbol")
  //@ApiResponses(value = {@ApiResponse(code = 404, message = "ticker is not found")})
  @GetMapping(path = "/iex/ticker/{ticker}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public IexQuote getQuote(@PathVariable String ticker) {
    try {
      return quoteService.findIexQuoteByTicker(ticker);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  /**
   * Returns IexQuote List corresponding to ticker List
   * @param tickers String List
   * @return IexQuote object List
   * @throws Exception throw with relation to HTTP status code
   */
  @GetMapping(path = "/iex/tickers/")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<IexQuote> getQuotes(@RequestParam("symbol") List<String> tickers) {
    try {
      return quoteService.findAllIexQuoteByTickers(tickers);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }
}
