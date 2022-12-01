package Program.CRUD.Interfaces;

import java.util.List;

public interface GenericRepository<T> {

    void addEntity(T t);

    List<T> getEntities();

    void updateEntity(int i, T t);

    void deleteEntity(int i);

    int findEntity(T t);

}
