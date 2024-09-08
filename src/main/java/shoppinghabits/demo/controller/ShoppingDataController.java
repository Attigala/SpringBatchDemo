package shoppinghabits.demo.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import shoppinghabits.demo.domain.ShoppingData;
import shoppinghabits.demo.repository.ShoppingDataRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
public class ShoppingDataController {

    @Autowired
    ShoppingDataRepository shoppingDataRepository;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @GetMapping("/shoppingdata")
    public List<ShoppingData> getShoppingData(){
        return shoppingDataRepository.findAll();
    }

    @PostMapping("/shoppingdata/import")
    public ResponseEntity<Void> importShoppingData(@RequestParam("file") MultipartFile file) {
        try {
            Path target = Paths.get("staging", file.getOriginalFilename());
            file.transferTo(target);
            JobParameters jobParameters =
                    new JobParametersBuilder()
                            .addString("param", target.toString()).toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
