package dao;

import model.Order;
import model.OrderRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class OrderDao {

    private JdbcTemplate template;
    private static final String ORDER_ID = "order_id";
    private static final String ORDER_ROW_ID = "order_row_id";
    private static final String ITEM_NAME = "item_name";
    private static final String QUANTITY = "quantity";
    private static final String PRICE = "price";
    private static final String ORDER_NUMBER = "order_number";

    public OrderDao(JdbcTemplate template) {
    this.template = template;
}


    public List<Order> findOrders() {

        var handler = new OrderRowHandler();
        String sql = "select * from orders JOIN order_rows on orders.order_id = order_rows.order_id";
        template.query(sql, handler);
        return handler.getOrders();
    }

    public Order findOrderId(Long id) {
        var handler = new OrderRowHandler();

        String sql = "select * from orders LEFT JOIN order_rows on orders.order_id = order_rows.order_id" +
                " WHERE orders.order_id = ?";

        template.query(sql, handler, id);

        return handler.getOrders().get(0);
    }

    public Order insertOrder(Order order) {

        var data = new BeanPropertySqlParameterSource(order);

        Number number = new SimpleJdbcInsert(template)
                .withTableName("orders")
                .usingGeneratedKeyColumns(ORDER_ID)
                .executeAndReturnKey(data);

        order.setId(number.longValue());


        OrderRow[] orderRows = order.getOrderRows();

        String sqlOrderRows = "insert into order_rows (order_id, item_name, quantity, price) values(?, ?, ?, ?)";

        if (orderRows != null) {
            for (OrderRow orderRow: orderRows) {

                template.update(sqlOrderRows, order.getId(), orderRow.getItemName(),
                        orderRow.getQuantity(), orderRow.getPrice());

            }
        }

        return order;
    }


    public void deleteOrderID(Long id) {

        String sql = "DELETE from orders where order_id = ?";

        template.update(sql, id);

    }

    public OrderRow[] convertOrderRows(List<OrderRow> listOrderRows) {
        int orderRowSize = listOrderRows.size();
        OrderRow[] orderRows = new OrderRow[orderRowSize];


        for (int i = 0; i < orderRowSize; i++) {
            orderRows[i] = listOrderRows.get(i);
        }

        return orderRows;
    }


    private class OrderRowHandler implements RowCallbackHandler {
        private List<Order> orders = new ArrayList<>();

        public void processRow(ResultSet rs) throws SQLException {

            List<OrderRow> orderRows = new ArrayList<>();
            long oldOrder = rs.getLong(ORDER_ID);
            String oldOrderNumber = rs.getString(ORDER_NUMBER);

            OrderRow orderRow = new OrderRow(rs.getLong(ORDER_ROW_ID),
                rs.getString(ITEM_NAME),
                rs.getInt(QUANTITY),
                rs.getDouble(PRICE));

            orderRows.add(orderRow);



            // Check if only 1 result is given
            if (rs.isLast()) {
                Order order;

                String item = rs.getString(ITEM_NAME);

                if (item == null) {
                    order = new Order(oldOrder,
                            oldOrderNumber,
                            null);
                } else {
                    order = new Order(oldOrder,
                            oldOrderNumber,
                            convertOrderRows(orderRows));
                }


                orders.add(order);
            }

            while (rs.next()){
                orderRow = new OrderRow(rs.getLong(ORDER_ROW_ID),
                        rs.getString(ITEM_NAME),
                        rs.getInt(QUANTITY),
                        rs.getDouble(PRICE));

                if (oldOrder == rs.getLong(ORDER_ID)) {
                    orderRows.add(orderRow);
                    oldOrder = rs.getLong(ORDER_ID);
                    oldOrderNumber = rs.getString(ORDER_NUMBER);

                    if (!rs.isLast()) {
                        continue;
                    }
                }

                Order order = new Order(oldOrder,
                        oldOrderNumber,
                        convertOrderRows(orderRows));

                orderRows.clear();
                orderRows.add(orderRow);
                orders.add(order);

                oldOrder = rs.getLong(ORDER_ID);}

        }

        public List<Order> getOrders()
        { return orders;}

    }
}
