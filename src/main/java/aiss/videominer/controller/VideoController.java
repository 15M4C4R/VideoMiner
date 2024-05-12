package aiss.videominer.controller;

import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Video;
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

@Tag(name = "Video", description = "Video management")
@RestController
@RequestMapping("/videominer/videos")
public class VideoController {

    @Autowired
    VideoRepository videoRepository;


    @Operation(
            description = "Get all videos",
            tags = {"videos", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Video.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Video> findAll(@RequestParam(required = false) String name,
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

        Page<Video> pageChannels;
        if(name != null) {
            pageChannels = videoRepository.findByNameContaining(name, paging);
        }
        else {
            pageChannels = videoRepository.findAll(paging);
        }

        return pageChannels.getContent();
    }


    @Operation(
            description = "Get one video by specifying its id",
            tags = {"video", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Video.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Video findOne(@PathVariable String id) throws VideoNotFoundException {
        Optional<Video> video = videoRepository.findById(id);
        if(video.isEmpty()) throw new VideoNotFoundException();
        return video.get();
    }

}
