package dataAccessLayer.dao;

import businessLayer.OrdersBLL;
import dataAccessLayer.connection.ConnectionFactory;
import model.Orders;
import model.Products;

import javax.swing.table.DefaultTableModel;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a generic Data Access Object that provides common database operations
 * for working with objects of any type.
 * <p>
 * This class serves as a base class for specific DAO implementations and includes generic methods
 * for object retrieval, insertion, editing, and deletion. It also provides methods for creating table
 * models, performing database transactions, and retrieving object IDs based on field values.
 * <p>
 * The AbstractDAO class utilizes Java reflection to dynamically generate SQL queries based on the
 * structure of the object's class and the provided id column name
 *
 * @param <T> the type of object managed by the DAO
 */

public class AbstractDAO <T> {

    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    public Class<T> getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    /**
     * Creates a SELECT query to retrieve a single object based on the provided id column name
     *
     * @param idName the name of the id column
     * @return the SELECT query string
     */
    private String createSelectQuery(String idName) {
        return ("select * from " + type.getSimpleName() + " where " + idName + " = ?");
    }

    /**
     * Creates an INSERT query to insert a new object into the database
     *
     * @param fields the fields of the object
     * @return the INSERT query string
     */
    private String createInsertQuery(Field[] fields) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("insert into ").append(type.getSimpleName()).append(" (");
        for (Field field : fields) {
            stringBuilder.append(field.getName()).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        stringBuilder.append(") values (");
        for (Field field : fields) {
            stringBuilder.append("?, ");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    /**
     * Creates an UPDATE query to edit an existing object in the database based on the provided id column name
     *
     * @param idName the name of the id column
     * @return the UPDATE query string
     */
    private String createEditQuery(String idName) {
        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(type.getSimpleName()).append(" set ");
        for (Field field : type.getDeclaredFields()) {
            sb.append(field.getName()).append(" = ").append("?, ");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(" where ").append(idName).append(" = ?");
        return sb.toString();
    }

    /**
     * Creates a DELETE query to delete an object from the database based on the provided id column name
     *
     * @param idName the name of the id column
     * @return the DELETE query string
     */
    private String createDeleteQuery(String idName) {
        return ("delete from " + type.getSimpleName() + " where " + idName + " = ?");
    }

    /**
     * Inserts an object into the database and returns the id of the newly inserted row
     *
     * @param t the object to insert
     * @return the id of the newly inserted row
     */
    public int insertObject(T t) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int insertedid = -1;
        String query = createInsertQuery(type.getDeclaredFields());
        try {
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            getFields(t, preparedStatement);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                insertedid = resultSet.getInt(1);
            }
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, "DAO:insertObject " + sqlException.getMessage());
        }
        return insertedid;
    }

    /**
     * Edits an existing object in the database based on the provided id column name
     *
     * @param t      the object to edit
     * @param idName the name of the id column
     */
    public void editObject(T t, String idName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String query = createEditQuery(idName);
        try {
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(query);
            Field[] fields = getFields(t, preparedStatement);

            preparedStatement.setInt(fields.length + 1, (Integer) fields[0].get(t));
            preparedStatement.executeUpdate();

        } catch (SQLException | IllegalAccessException sqlException) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:editObject " + sqlException.getMessage());
        }
    }

    /**
     * Deletes an object from the database based on the provided id column name
     *
     * @param t      the object to delete
     * @param idName the name of the id column
     */
    public void deleteObject(T t, String idName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String query = createDeleteQuery(idName);
        try {
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(query);

            Field fields = type.getDeclaredFields()[0];
            fields.setAccessible(true);
            preparedStatement.setInt(1, (Integer) fields.get(t));
            preparedStatement.executeUpdate();

        } catch (SQLException | IllegalAccessException sqlException) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:deleteObject " + sqlException.getMessage());
        }
    }

    /**
     * Retrieves the fields of an object and sets the corresponding values in the prepared statement.
     *
     * @param t                  the object
     * @param preparedStatement the prepared statement
     * @return the fields of the object
     */
    private Field[] getFields(T t, PreparedStatement preparedStatement) {
        try {
            Field[] fields = type.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object object = fields[i].get(t);
                if (object instanceof String) {
                    preparedStatement.setString(i + 1, (String) object);
                } else if (object instanceof Integer) {
                    preparedStatement.setInt(i + 1, (Integer) object);
                } else if (object instanceof Double) {
                    preparedStatement.setDouble(i + 1, (Double) object);
                }
            }
            return fields;
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a list of objects based on the given result set
     *
     * @param resultSet the result set
     * @return a list of objects
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();

        try {
            while (resultSet.next()) {
                T instance = type.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException |
                 InvocationTargetException | SQLException | IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Creates a table based on a list of objects
     *
     * @param objectsList the list of objects
     * @return the created table
     */
    public DefaultTableModel createTable(List<T> objectsList) {
        Vector<String> tableHeader = new Vector<>();
        Vector<Vector<Object>> tableData = new Vector<>();
        Object firstObject = objectsList.get(0);
        for (Field field : firstObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                tableHeader.add(field.getName());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        for (T t : objectsList) {
            Vector<Object> newRow = new Vector<>();
            for (Field field : t.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(t);
                    newRow.add(value);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            tableData.add(newRow);
        }
        return new DefaultTableModel(tableData, tableHeader);
    }

    /**
     * Retrieves all objects of the specified type from the database
     *
     * @return a list of objects
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "select * from " + type.getSimpleName();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + sqlException.getMessage());
        }
        return null;
    }

    /**
     * Finds an object by its id in the database based on the provided id value and column name.
     *
     * @param id      the id value
     * @param idName  the name of the id column
     * @return the found object, null if not found
     */
    public T findById(int id, String idName) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery(idName);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findByid " + sqlException.getMessage());
        }
        return null;
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

        AbstractDAO<Products> productDAO = new ProductsDAO();
        Products product = productDAO.findById(productId, "productId");
        int updatedQuantity = product.getQuantity() - orderedQuantity;
        product.setQuantity(updatedQuantity);
        productDAO.editObject(product, "productid");

        Orders order = new Orders(clientId, productId, orderedQuantity);
        OrdersBLL orderBll = new OrdersBLL();
        return orderBll.insertObject(order);
    }

    /**
     * Retrieves the id of an object based on its name and the provided field and id column names
     *
     * @param objectValue the value of the object
     * @param fieldName   the name of the field to look for
     * @param idName      the name of the id column
     * @return the id of the found object or -1 if not found
     */
    public int getIdByName(String objectValue, String fieldName, String idName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        StringBuilder sb = new StringBuilder();
        sb.append("select ").append(idName).append(" from ").
                append(type.getSimpleName()).append(" where ").append(fieldName).append(" = ?");
        String query = sb.toString();

        try {
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, objectValue);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(idName);
            }
        } catch (SQLException sqlException) {
            LOGGER.log(Level.WARNING, "DAO:getidByName " + sqlException.getMessage());
        }
        return -1;
    }

}
