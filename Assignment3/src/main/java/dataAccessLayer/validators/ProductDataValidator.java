package dataAccessLayer.validators;

import model.Products;

import java.util.regex.Pattern;

public class ProductDataValidator implements Validator<Products> {

    /**
     * Validates the product name.
     *
     * @param product the product object to validate
     * @return true if the product name is valid, false otherwise.
     */
    public boolean validateProductName(Products product) {
        /*
         * In this example, the isValidProductName() method takes a String as input
         * and uses a regular expression pattern (^[a-zA-Z0-9 ]+$) to validate the product name.
         * The pattern ^[a-zA-Z0-9 ]+$ ensures that the string contains
         * only alphanumeric characters (uppercase and lowercase) and spaces.
         * */

        String NAME_PATTERN = "^[a-zA-Z0-9 ]+$";
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        return pattern.matcher((product.getProductName())).matches();
    }

    /**
     * Validates the product quantity.
     *
     * @param product the product quantity to validate
     * @return true if the product quantity is valid, false otherwise.
     */
    public boolean validateProductQuantity(Products product) {
        try {
            Integer.parseInt(String.valueOf(product.getQuantity()));
        } catch(NumberFormatException e) {
            System.out.println("Pretul introdus este double, dar trebuie int");
            return false;
        }
        return true;
    }

    /**
     * Validates the product price.
     *
     * @param product the product object to validate
     * @return true if the product price is valid, false otherwise.
     */
    public boolean validateProductPrice(Products product) {
        try {
            Double.parseDouble(String.valueOf(product.getPrice()));
        } catch(NumberFormatException e) {
            System.out.println("Ceva eroare la validarea pretului");
            return false;
        }
        return true;
    }

    /**
     * Manages and combines all other validations
     *
     * @param product the product object to validate
     * @return true if the other validation results are valid, false otherwise
     */
    @Override
    public String validationResult(Products product) {
        if(!validateProductName(product)) {
            return ("Nume produs incorect!");
        }
        if(!validateProductQuantity(product)) {
            return ("Cantitate produs incorecta!");
        }
        if(!validateProductPrice(product)) {
            return ("Pret produs incorect!");
        }
        return ("Date produs valide!");
    }
}
