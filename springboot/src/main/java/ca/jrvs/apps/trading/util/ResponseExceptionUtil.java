package ca.jrvs.apps.trading.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResponseExceptionUtil {

  private static final Logger logger = LoggerFactory.getLogger(ResponseExceptionUtil.class);

  /**
   * Map exception to HTTP response code
   * @param ex exception
   * @return Http response code according to exception
   */
  public static ResponseStatusException getResponseStatusException(Exception ex) {
    if (ex instanceof IllegalArgumentException) {
      logger.debug("Invalid input", ex);
      return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage()); //status code 400
    } else {
      logger.error("Internal Error", ex);
      return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Internal Error: Please contact Admin"); //status code 500
    }
  }

}
