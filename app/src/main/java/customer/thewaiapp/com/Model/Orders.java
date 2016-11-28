package customer.thewaiapp.com.Model;

import io.realm.RealmObject;

/**
 * Created by keviv on 20/09/2016.
 */

public class Orders extends RealmObject {
    private Order order;
    private Address address;
    private OrderAmount orderAmount;

    public Orders() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderAmount getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(OrderAmount orderAmount) {
        this.orderAmount = orderAmount;
    }
}
