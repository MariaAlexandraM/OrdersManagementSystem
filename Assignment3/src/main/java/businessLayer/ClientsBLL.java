package businessLayer;

import dataAccessLayer.dao.AbstractDAO;
import dataAccessLayer.dao.ClientsDAO;
import model.Clients;

/**
 * Represents the class that sends requests to the database using its respective DAO
 */
public class ClientsBLL {

    private AbstractDAO<Clients> clientsAbstractDAO = new ClientsDAO();
    public ClientsBLL() {

    }
    /**
     * Inserts a client into the database and returns the id of the newly inserted row
     *
     * @param client the client to insert
     * @return the id of the newly inserted row
     */
    public int insertObject(Clients client) {
        return clientsAbstractDAO.insertObject(client);
    }

    /**
     * Edits an existing client in the database based on the provided id column name
     *
     * @param client the client to edit
     * @param idName the name of the id column
     */
    public void editObject(Clients client, String idName) {
        clientsAbstractDAO.editObject(client, idName);
    }

    /**
     * Retrieves the id of a client based on its name and the provided field and id column names
     *
     * @param objectValue the value of the object
     * @param fieldName   the name of the field to look for
     * @param idName      the name of the id column
     * @return the id of the found client or -1 if not found
     */
    public int getIdByName(String objectValue, String fieldName, String idName) {
        return clientsAbstractDAO.getIdByName(objectValue, fieldName, idName);
    }

    /**
     * Deletes a client from the database based on the provided id column name
     *
     * @param client the client to delete
     * @param idName the name of the id column
     */
    public void deleteObject(Clients client, String idName) {
        clientsAbstractDAO.deleteObject(client, idName);
    }

}
