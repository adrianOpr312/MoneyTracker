package Program.CRUD;

import Program.CRUD.Interfaces.GenericFileRepository;

import java.io.*;

@SuppressWarnings("unchecked")
public class FileRepository<T> extends Repository<T> implements GenericFileRepository<T> {

    private final String filePath;

    public FileRepository(String filePath) {
        this.filePath = filePath;
    }

    public static <T> void saveObject(T object, String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();

    }

    public static <T> T loadObject(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filePath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return (T) ois.readObject();

    }

    public void saveToFile() throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        for (T entity : entities) {
            oos.writeObject(entity);
        }
        oos.writeObject(null);
        oos.close();
    }

    public void loadFromFile() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filePath);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        T entity = (T) ois.readObject();
        while (entity != null) {
            addEntity(entity);
            entity = (T) ois.readObject();
        }
        ois.close();

    }
}
