package businessLayer;

import dataAccessLayer.dao.AbstractDAO;
import dataAccessLayer.dao.ProductsDAO;
import model.Products;

/**
 * Represents the class that sends requests to the database using its respective DAO
 */
public class ProductsBLL {

    private AbstractDAO<Products> productsAbstractDAO = new ProductsDAO();
    public ProductsBLL() {

    }
    /**
     * Inserts a product into the database and returns the id of the newly inserted row
     *
     * @param product the product to insert
     * @return the id of the newly inserted row
     */
    public int insertObject(Products product) {
        return productsAbstractDAO.insertObject(product);
    }

    /**
     * Edits an existing product in the database based on the provided id column name
     *
     * @param product the object to edit
     * @param idName the name of the id column
     */
    public void editObject(Products product, String idName) {
        productsAbstractDAO.editObject(product, idName);
    }

    /**
     * Retrieves the id of a product based on its name and the provided field and id column names
     *
     * @param objectValue the value of the object
     * @param fieldName   the name of the field to look for
     * @param idName      the name of the id column
     * @return the id of the found product or -1 if not found
     */
    public int getIdByName(String objectValue, String fieldName, String idName) {
        return productsAbstractDAO.getIdByName(objectValue, fieldName, idName);
    }

    /**
     * Deletes a product from the database based on the provided id column name
     *
     * @param product the product to delete
     * @param idName the name of the id column
     */
    public void deleteObject(Products product, String idName) {
        productsAbstractDAO.deleteObject(product, idName);
    }

    /**
     * Finds a product by its id in the database based on the provided id value and column name.
     *
     * @param id      the id value
     * @param idName  the name of the id column
     * @return the found product, null if not found
     */
    public Products findById(int id, String idName) {
        return productsAbstractDAO.findById(id, idName);
    }

}