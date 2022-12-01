package Program.CRUD.Interfaces;

import java.io.IOException;

public interface GenericFileRepository<T> extends GenericRepository<T> {
    void saveToFile() throws IOException;

    void loadFromFile() throws IOException, ClassNotFoundException;

}
