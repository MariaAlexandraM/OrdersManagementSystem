package businessLayer;

import dataAccessLayer.dao.AbstractDAO;
import dataAccessLayer.dao.OrdersDAO;
import model.Orders;

/**
 * Represents the class that sends requests to the database using its respective DAO
 */
public class OrdersBLL {

    private AbstractDAO<Orders> ordersAbstractDAO = new OrdersDAO();
    public OrdersBLL() {

    }
    /**
     * Inserts an object into the database and returns the id of the newly inserted row
     *
     * @param order the order to insert
     * @return the id of the newly inserted row
     */
    public int insertObject(Orders order) {
        return ordersAbstractDAO.insertObject(order);
    }

    /**
     * Makes an order by subtracting from the quantity of a product with the ordered quantity and inserting a new order in the ORders table
     *
     * @param clientId        the id of the client making the order
     * @param productId       the id of the ordered product
     * @param orderedQuantity the requsted quantity to be ordered
     * @return the id of the inserted order or -1 if the operation fails
     */
    public int makeOrder(int clientId, int productId, int orderedQuantity) {
        return ordersAbstractDAO.makeOrder(clientId, productId, orderedQuantity);
    }

}
