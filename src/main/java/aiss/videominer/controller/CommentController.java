package aiss.videominer.controller;

import aiss.videominer.exception.CommentNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer/comments")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;
    VideoRepository videoRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Comment> findAll(){
        return commentRepository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Comment findOne(@PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isEmpty()) throw new CommentNotFoundException();
        return comment.get();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idVideo}")
    public List<Comment> findVideoComments(@PathVariable String idVideo) throws VideoNotFoundException {
        Optional<Video> video = videoRepository.findById(idVideo);
        if(video.isEmpty()) throw new VideoNotFoundException();
        return video.get().getComments();
    }

}
