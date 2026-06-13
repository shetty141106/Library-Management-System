package com.project.lms.Service;

import com.project.lms.Entity.Reader;
import com.project.lms.Dao.ReaderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderService {

    @Autowired
    private ReaderDao readerDao;
    @Autowired

    public List<Reader> getAllReaders() {
        return readerDao.findAll();
    }

    public Reader getReaderById(int id) {
        return readerDao.findById(id);
    }

    public Reader addReader(Reader reader) {
        return readerDao.save(reader);
    }


    public Reader updateReader(int id, Reader reader) {

        Reader existingReader = readerDao.findById(id);

        if (existingReader != null) {
            existingReader.setEmail(reader.getEmail());
            existingReader.setName(reader.getName());
            existingReader.setAddress(reader.getAddress());
            existingReader.setPhones(reader.getPhones());

            return readerDao.save(existingReader);
        }

        return null;
    }

    public void deleteReader(int id) {
        readerDao.deleteById(id);
    }
}