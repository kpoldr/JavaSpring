package app;

import dao.OrderDao;
import model.Order;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderController {
    private OrderDao dao;

    public OrderController(OrderDao dao) {
        this.dao = dao;
    }

    @PostMapping("orders")
    public Order insertOrder(@RequestBody @Valid Order order)
    {
        return dao.insertOrder(order);
    }

    @GetMapping("orders/{id}")
    public Order getById(@PathVariable Long id) {
        return dao.findOrderId(id);
    }

    @GetMapping("orders")
    public List<Order> getOrders() {
        return dao.findOrders();
    }

    @DeleteMapping("orders/{id}")
    public void deletePost(@PathVariable Long id) {
        dao.deleteOrderID(id);
    }

}
