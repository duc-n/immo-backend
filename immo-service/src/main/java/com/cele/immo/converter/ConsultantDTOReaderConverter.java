package com.cele.immo.converter;

import com.cele.immo.dto.ConsultantDTO;
import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@ReadingConverter
public class ConsultantDTOReaderConverter implements Converter<DBObject, ConsultantDTO> {

    @Override
    public ConsultantDTO convert(DBObject source) {
        return ConsultantDTO.builder()
                .id((String) source.get("_id"))
                .nom((String) source.get("nom"))
                .username((String) source.get("username"))
                .build();
    }
}
