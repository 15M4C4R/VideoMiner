package aiss.videominer.controller;

import aiss.videominer.exception.CommentNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Comment;
import aiss.videominer.model.User;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.VideoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Comment", description = "Comment management")
@RestController
@RequestMapping("/videominer/comments")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;
    VideoRepository videoRepository;

    @Operation(
            description = "Get all comments",
            tags = {"comments", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.OK)


    @GetMapping
    public List<Comment> findAll(@RequestParam(required = false) User author,
                                 @RequestParam(required = false) String order,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        Pageable paging;
        if(order != null) {
            if(order.startsWith("-")) {
                paging = PageRequest.of(page, size,
                        Sort.by(order.substring(1)).descending());
            }
            else {
                paging = PageRequest.of(page, size,
                        Sort.by(order).ascending());
            }
        }
        else {
            paging = PageRequest.of(page, size);
        }

        Page<Comment> pageChannels;
        if(author != null) {
            pageChannels = commentRepository.findByAuthor(author, paging);
        }
        else {
            pageChannels = commentRepository.findAll(paging);
        }

        return pageChannels.getContent();
    }


    @Operation(
            description = "Get one comment by specifying its id",
            tags = {"comment", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Comment findOne(@PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isEmpty()) throw new CommentNotFoundException();
        return comment.get();
    }




/*    @Operation(
            description = "Get the comments from a video by specifying its id",
            tags = {"comments", "video", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "", content = { @Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idVideo}")
    public List<Comment> findVideoComments(@PathVariable String idVideo) throws VideoNotFoundException {
        Optional<Video> video = videoRepository.findById(idVideo);
        if(video.isEmpty()) throw new VideoNotFoundException();
        return video.get().getComments();
    }
*/
}
