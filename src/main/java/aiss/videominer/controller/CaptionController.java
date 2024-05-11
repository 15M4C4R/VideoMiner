package aiss.videominer.controller;

import aiss.videominer.exception.CaptionNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Caption;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CaptionRepository;
import aiss.videominer.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer/captions")
public class CaptionController {

    @Autowired
    CaptionRepository captionRepository;
    VideoRepository videoRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Caption> findAll(){
        return captionRepository.findAll();
    }
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Caption findOne(@PathVariable String id) throws CaptionNotFoundException {
        Optional<Caption> caption = captionRepository.findById(id);
        if(caption.isEmpty()) throw new CaptionNotFoundException();
        return caption.get();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idVideo}")
    public List<Caption> findVideoCaptions(@PathVariable String idVideo) throws VideoNotFoundException {
        Optional<Video> video = videoRepository.findById(idVideo);
        if(video.isEmpty()) throw new VideoNotFoundException();
        return video.get().getCaptions();
    }

}
