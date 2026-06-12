package com.project.lms.Service;

import com.project.lms.Entity.Reader;
import com.project.lms.Repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderService {

    @Autowired
    private ReaderRepository readerRepository;

    public List<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    public Reader getReaderById(int id) {
        return readerRepository.findById(id).orElse(null);
    }

    public Reader addReader(Reader reader) {
        return readerRepository.save(reader);
    }

    public Reader updateReader(int id, Reader reader) {

        Reader existingReader =
                readerRepository.findById(id).orElse(null);

        if (existingReader != null) {
            existingReader.setEmail(reader.getEmail());
            existingReader.setName(reader.getName());
            existingReader.setAddress(reader.getAddress());
            existingReader.setPhones(reader.getPhones());

            return readerRepository.save(existingReader);
        }

        return null;
    }

    public void deleteReader(int id) {
        readerRepository.deleteById(id);
    }
}