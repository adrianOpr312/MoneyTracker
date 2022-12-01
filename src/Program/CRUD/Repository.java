package Program.CRUD;

import Program.CRUD.Interfaces.GenericRepository;

import java.util.ArrayList;
import java.util.List;

public class Repository<T> implements GenericRepository<T> {

    protected final List<T> entities = new ArrayList<>();

    @Override
    public List<T> getEntities() {
        return entities;
    }

    @Override
    public void addEntity(T entity) {
        entities.add(entity);
    }

    @Override
    public void deleteEntity(int index) {
        entities.remove(index);
    }

    @Override
    public void updateEntity(int index, T entity) {
        entities.set(index, entity);
    }

    @Override
    public int findEntity(T t) {
        return entities.indexOf(t);
    }
}
