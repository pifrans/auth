package com.pifrans.auth.services;

import com.pifrans.auth.models.Record;
import com.pifrans.auth.repositories.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {
    @Autowired
    private RecordRepository recordRepository;

    public List<Record> saveAll(List<Record> records) {
        return recordRepository.saveAll(records);
    }
}
