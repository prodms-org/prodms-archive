package com.hydroyura.prodms.archive.server.batch;


import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.core.io.Resource;

public class CsvFileReaderFactory {

    public ItemReader<CreateUnitReq> getItemReader(Resource resource) {
        FlatFileItemReader<CreateUnitReq> reader = new FlatFileItemReaderBuilder<CreateUnitReq>()
            .name("Some nae")
            .resource(resource)
            .addComment("My some comment")
            .comments("Comment 1", "Comment 2", "Comment 3")
            .linesToSkip(5)
            .lineMapper(lineMapper())
            .build();
        reader.setResource(resource);

        return reader;
    }

    private LineMapper<CreateUnitReq> lineMapper() {
        DefaultLineMapper<CreateUnitReq> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer());
        lineMapper.setFieldSetMapper(fieldSetMapper());
        return lineMapper;
    }

    private LineTokenizer lineTokenizer() {
        var lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("number", "name", "status", "type", "additional");
        return lineTokenizer;
    }

    private FieldSetMapper<CreateUnitReq> fieldSetMapper() {
        var fieldSetMapper = new BeanWrapperFieldSetMapper<CreateUnitReq>();
        fieldSetMapper.setTargetType(CreateUnitReq.class);
        return fieldSetMapper;
    }

}


/*

delimited()
      .names(new String[] { "brand", "origin", "characteristics" })
      .fieldSetMapper(new BeanWrapperFieldSetMapper() {{
          setTargetType(Coffee.class);
      }})


 */