package shoppinghabits.demo;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shoppinghabits.demo.domain.ShoppingData;
import shoppinghabits.demo.repository.ShoppingDataRepository;

@Component
public class ShoppingDataTableWriter implements ItemWriter<ShoppingData> {

    @Autowired
    private ShoppingDataRepository repository;

    @Override
    public void write(Chunk<? extends ShoppingData> chunk) throws Exception {
        System.out.println("Writer Thread "+Thread.currentThread().getName());
        repository.saveAll(chunk);
    }
}