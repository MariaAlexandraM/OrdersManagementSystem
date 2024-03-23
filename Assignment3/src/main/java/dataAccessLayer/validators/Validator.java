package dataAccessLayer.validators;

/**
 * A generic interface for data validation
 *
 * @param <T> the type of data to be validated
 */
public interface Validator<T> {
    /**
     * Validates the given data and returns a validation result message
     *
     * @param t the data to be validated
     * @return a string containing the validation result message
     */
    String validationResult(T t);
}

