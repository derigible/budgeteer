package derigible.utils;

import java.io.File;

/**
 * Declares if this object has been saved.
 * 
 * @author marcphillips
 * 
 */
public interface Saved {
    /**
     * Declare if this object has been saved.
     * 
     * @return saved
     */
    public abstract boolean saved();

    /**
     * Switch the saved state.
     */
    public abstract void toggleSaved();

    /**
     * Save the object
     * 
     * @param filename
     *            name of the file
     * @return file if success, the file
     */
    public abstract File save(String filename);

    /**
     * Get the name of the object to save.
     * 
     * @return the name to save
     */
    public abstract String getName();
}
