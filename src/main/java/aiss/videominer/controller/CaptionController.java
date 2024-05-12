package aiss.videominer.controller;

import aiss.videominer.exception.CaptionNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Caption;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CaptionRepository;
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
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Tag(name = "Caption", description = "Captions management" )
@RestController
@RequestMapping("/videominer/captions")
public class CaptionController {

    @Autowired
    CaptionRepository captionRepository;
    VideoRepository videoRepository;

    @Operation(
            description = "Get all the captions",
            tags = {"captions", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Caption.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.OK)


    @GetMapping
    public List<Caption> findAll(@RequestParam(required = false) String language,
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

        Page<Caption> pageChannels;
        if(language != null) {
            pageChannels = captionRepository.findByLanguage(language, paging);
        }
        else {
            pageChannels = captionRepository.findAll(paging);
        }

        return pageChannels.getContent();
    }


    @Operation(
            description = "Get one Caption by specifying its id",
            tags = {"caption", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Caption.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Caption findOne(@PathVariable String id) throws CaptionNotFoundException {
        Optional<Caption> caption = captionRepository.findById(id);
        if(caption.isEmpty()) throw new CaptionNotFoundException();
        return caption.get();
    }





/*    @Operation(
            description = "Get a caption from a video by specifying its id",
            tags = {"caption", "video", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Caption.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idVideo}")
    public List<Caption> findVideoCaptions(@PathVariable String idVideo) throws VideoNotFoundException {
        Optional<Video> video = videoRepository.findById(idVideo);
        if(video.isEmpty()) throw new VideoNotFoundException();
        return video.get().getCaptions();
    }
*/
}
