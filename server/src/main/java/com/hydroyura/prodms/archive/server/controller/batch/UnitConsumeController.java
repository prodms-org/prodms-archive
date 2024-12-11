package com.hydroyura.prodms.archive.server.controller.batch;

import com.hydroyura.prodms.archive.client.model.req.CreateUnitReq;
import com.hydroyura.prodms.archive.server.batch.CsvFileReaderFactory;
import com.hydroyura.prodms.archive.server.db.entity.Unit;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/batch/v1/units")
@Slf4j
public class UnitConsumeController {

    private static final String MEDIA_TYPE_CSV_VALUE = "text/csv;charset=UTF-8";


    private final CsvFileReaderFactory csvFileReaderFactory;
    private final ItemProcessor<CreateUnitReq, CreateUnitReq> unitItemProcessor;
    private final ItemWriter<CreateUnitReq> unitItemWriter;
    private final JobRepository jobRepository;
    private final TransactionManager transactionManager;
    private final JobLauncher jobLauncher;



    @RequestMapping(method = RequestMethod.POST, value = {"", "/"}, consumes = MEDIA_TYPE_CSV_VALUE)
    public ResponseEntity<?> uploadUnits(HttpServletRequest request, @RequestBody byte[] body)
        throws Exception {
        Resource csv = new ByteArrayResource(body);


        ItemReader<CreateUnitReq> reader = csvFileReaderFactory.getItemReader(csv);
        var step = step(reader);
        var job = job(step);

        var jobParams = new JobParametersBuilder()
            .addDate("some date keyfd123213", Date.from(Instant.now()))
            .toJobParameters();

        var jobExecution = jobLauncher.run(job, jobParams);

        while (jobExecution.isRunning()) {
            log.info("++++++++++++++");
        }


        return ResponseEntity.badRequest().build();
    }


    private Step step(ItemReader<CreateUnitReq> reader) {
        return new StepBuilder("some step name22", jobRepository)
            .<CreateUnitReq, CreateUnitReq>chunk(100, (PlatformTransactionManager) transactionManager)
            .reader(reader)
            .processor(unitItemProcessor)
            .writer(unitItemWriter)
            .build();

    }

    private Job job(Step step) {
        return new JobBuilder("some job23333", jobRepository)
            .start(step)
            .build();
    }
}
