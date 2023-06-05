package com.example.onekids_project.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.query.spi.QueryImplementor;

import java.io.Serializable;
import java.util.stream.Stream;

public class StringGeneratorEmployeeCode implements IdentifierGenerator {
    private String prefix = "EP";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String queryString="SELECT e.id FROM Employee e";
        // Select all id
        QueryImplementor<String> query = session.createQuery(queryString, String.class);
        try (Stream<String> stream = query.stream()) {
            Long max = stream.map(t -> t.replace(prefix, "")) // EP0001 => 0001
                    .mapToLong(Long::parseLong)	// String -> Long
                    .max()						// Tìm số lớn nhất
                    .orElse(0L);				// Nếu không có thì set là 0
            return prefix + String.format("%05d", max + 1); // Tăng lên 1 thành EMP0002
        }
    }
}
