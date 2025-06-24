package com.achinth.Controllers;
import com.achinth.demo.Price; // or wherever your Price entity is
import com.achinth.demo.PriceRepository; // if you placed repo in another package

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prices")
public class PriceController {

    private final PriceRepository repo;
    private final KafkaTemplate<String, Price> kafkaTemplate;
    private static final String TOPIC = "price-events";

    public PriceController(PriceRepository repo, KafkaTemplate<String, Price> kafkaTemplate) {
        this.repo = repo;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public List<Price> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public Price save(@RequestBody Price price) {
        kafkaTemplate.send(TOPIC, price);  // send to Kafka
        return repo.save(price);           // save to DB
    }
}
