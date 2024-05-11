package aiss.videominer.controller;

import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Video;
import aiss.videominer.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer/videos")
public class VideoController {

    @Autowired
    VideoRepository videoRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Video> findAll(){
        return videoRepository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Video findOne(@PathVariable String id) throws VideoNotFoundException {
        Optional<Video> video = videoRepository.findById(id);
        if(video.isEmpty()) throw new VideoNotFoundException();
        return video.get();
    }

}
