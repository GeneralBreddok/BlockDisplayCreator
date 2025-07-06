package me.general_breddok.blockdisplaycreator.data.manager;

/**
 * This interface represents a source of a PersistentDataTypeManager.
 * It provides a method to retrieve the PersistentDataTypeManager instance.
 */
public interface PersistentDataTypeManagerProvider {

    /**
     * Returns the PersistentDataTypeManager instance.
     *
     * @return the PersistentDataTypeManager instance
     */
    PersistentDataTypeManager getPersistentDataTypeManager();
}
