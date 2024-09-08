package shoppinghabits.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.jpa.JpaTransactionManager;
import shoppinghabits.demo.domain.ShoppingData;
import shoppinghabits.demo.repository.ShoppingDataRepository;

@Configuration
public class ShoppingDataJobConfiguration {

    @Autowired
    ShoppingDataRepository shoppingDataRepository;
    @Autowired ShoppingDataTableWriter tableWriter;

    @Bean
    public Job job(JobRepository jobRepository, Step step1, Step step2, Step step3) {
        return new JobBuilder("ShoppingJob", jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    public Step Step1(
            JobRepository jobRepository, JpaTransactionManager transactionManager,
            ItemReader<ShoppingData> shoppingDataFileReader) {
        return new StepBuilder("fileImport", jobRepository)
                .<ShoppingData, ShoppingData>chunk(100, transactionManager)
                .reader(shoppingDataFileReader)
                .writer(tableWriter)
                .build();
    }

    @Bean
    @JobScope
    public FlatFileItemReader<ShoppingData> shoppingDataFileReader(@Value("#{jobParameters['param']}") String param) {
        return new FlatFileItemReaderBuilder<ShoppingData>()
                .name("shoppingDataFileReader")
                .resource(new FileSystemResource(param))
                .delimited()
                .names("id","shoppingDate", "shop","item", "quantity", "unit", "price")
                .targetType(ShoppingData.class)
                .build();
    }

    @Bean
    public RepositoryItemWriter<ShoppingData> writer() {
        RepositoryItemWriter<ShoppingData> writer = new RepositoryItemWriter<>();
        writer.setRepository(shoppingDataRepository);
        writer.setMethodName("save");
        return writer;
    }
}
