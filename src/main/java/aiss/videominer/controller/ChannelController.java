package aiss.videominer.controller;

import aiss.videominer.exception.ChannelNotFoundException;
import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Channel", description = "Channel management")
@RestController
@RequestMapping("/videominer/channels")
public class ChannelController {

    @Autowired
    ChannelRepository channelRepository;


    @Operation(
            description = "Get all channels",
            tags = {"channels", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Channel.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Channel> findAll(@RequestParam(required = false) String name,
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

        Page<Channel> pageChannels;
        if(name != null) {
            pageChannels = channelRepository.findByName(name, paging);
        }
        else {
            pageChannels = channelRepository.findAll(paging);
        }

        return pageChannels.getContent();
    }


    @Operation(
            description = "Get a channel by specifying its id",
            tags = {"channel", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Channel.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Channel findById(@PathVariable String id) throws ChannelNotFoundException {
        Optional<Channel> channel = channelRepository.findById(id);
        if (channel.isEmpty()) {
            throw new ChannelNotFoundException();
        }
        return channel.get();
    }



    @Operation(
            description = "Create a channel",
            tags = {"channel", "post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = Channel.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Channel createChannel(@Valid @RequestBody Channel channel) {
        return channelRepository.save(channel);
    }




    @Operation(
            description = "Update a channel",
            tags = {"channel" , "put"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateChannel(@Valid @RequestBody Channel updatedChannel, @PathVariable String id) throws ChannelNotFoundException {
        Optional<Channel> channelData = channelRepository.findById(id);
        if(channelData.isEmpty()) throw new ChannelNotFoundException();
        Channel _channel = channelData.get();
        _channel.setCreatedTime(updatedChannel.getCreatedTime());
        _channel.setDescription(updatedChannel.getDescription());
        _channel.setName(updatedChannel.getName());
        _channel.setVideos(updatedChannel.getVideos());
        channelRepository.save(_channel);
    }




    @Operation(
            description = "Delete a channel by specifying its id",
            tags = {"channel", "delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteChannel(@PathVariable String id) throws ChannelNotFoundException {
        if(channelRepository.existsById(id)) channelRepository.deleteById(id);
        else throw new ChannelNotFoundException();
    }

}
