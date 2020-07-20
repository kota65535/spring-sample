package com.sre.aip.sample.core.db;

import com.google.common.base.Stopwatch;
import com.google.common.io.Resources;
import com.opencsv.bean.CsvToBeanBuilder;
import com.sre.aip.sample.core.app.repository.pet.PetEntity;
import com.sre.aip.sample.core.app.repository.pet.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootApplication(scanBasePackages = {
        "com.sre.aip.sample.core.db",
        "com.sre.aip.sample.core.app"
})
public class Application implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  private final PetRepository repository;

  public Application(PetRepository repository) {
    this.repository = repository;
  }

  @Override
  public void run(String... args) throws Exception {
    // load CSV
    URL url = Resources.getResource("data.csv");
    BufferedReader reader = Resources.asCharSource(url, StandardCharsets.UTF_8)
            .openBufferedStream();
    List<PetCsvModel> models = new CsvToBeanBuilder<PetCsvModel>(reader)
            .withType(PetCsvModel.class)
            .build()
            .parse();

    // convert to entity
    List<PetEntity> entities = models.stream()
            .map(m -> PetEntity.builder()
                    .name(m.getName())
                    .tag(m.getTag())
                    .build())
            .collect(Collectors.toList());

    // insert them all
    Stopwatch sw = Stopwatch.createStarted();
    logger.info("{} entries to import.", entities.size());
    repository.bulkInsert(entities, PetEntity.class);
    logger.info("{} entries imported in {} sec.", entities.size(), sw.elapsed(TimeUnit.SECONDS));
  }
}
