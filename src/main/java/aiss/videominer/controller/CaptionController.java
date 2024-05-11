package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.repository.CaptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/model/Caption")

public class CaptionController {
    private final CaptionRepository repository;

    public CaptionController(CaptionRepository repository){
        this.repository=repository;
    }

    @GetMapping
    public List<Caption> FindAll(){
        return repository.findAll();
    }
    @GetMapping("/{id}")
    public Caption findOne(@PathVariable String id){
        return repository.findOne(id);
    }


}
