package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ca.jrvs.apps.jdbc.util.DataAccessObject;

class OrderDAO extends DataAccessObject<Order> {
  private static final String getOrderById = "SELECT\n"
      + "  c.first_name, c.last_name, c.email, o.order_id,\n"
      + "  o.creation_date, o.total_due, o.status,\n"
      + "  s.first_name, s.last_name, s.email,\n"
      + "  ol.quantity,\n"
      + "  p.code, p.name, p.size, p.variety, p.price\n"
      + "from orders o\n"
      + "  join customer c on o.customer_id = c.customer_id\n"
      + "  join salesperson s on o.salesperson_id=s.salesperson_id\n"
      + "  join order_item ol on ol.order_id=o.order_id\n"
      + "  join product p on ol.product_id = p.product_id\n"
      + "where o.order_id = ?";

  public OrderDAO(Connection connection) {
    super(connection);
  }

  @Override
  public Order findById(long id) {
    Order order = new Order();
    int count = 0;
    List<orderDetails> orderList = new ArrayList<orderDetails>();
    try (PreparedStatement statement = this.connection.prepareStatement(getOrderById);) {
      statement.setLong(1, id);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        if (count == 0) {
          order.setCustomerFirstName(rs.getString(1));
          order.setCustomerLastLane(rs.getString(2));
          order.setCustomerEmail(rs.getString(3));

          order.setId(rs.getLong(4));
          order.setCreationDate(rs.getDate(5));
          order.setTotalDue(rs.getBigDecimal(6));
          order.setStatus(rs.getString(7));

          order.setSalespersonFirstName(rs.getString(8));
          order.setSalespersonLastName(rs.getString(9));
          order.setSalespersonEmail(rs.getString(10));
          count++;
        }
				orderDetails od = new orderDetails();
				od.setOrderItemQuantity(rs.getLong(11));
				od.setProductCode(rs.getString(12));
				od.setProductName(rs.getString(13));
				od.setProductSize(rs.getLong(14));
				od.setProductVariety(rs.getString(15));
				od.setProductPrice(rs.getBigDecimal(16));
				orderList.add(od);
      }
    } catch (SQLException se){
      se.printStackTrace();
      throw new RuntimeException(se);
    }
    order.setOrderList(orderList);
    return order;
  }
}
