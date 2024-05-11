package aiss.videominer.controller;

import aiss.videominer.exception.ChannelNotFoundException;
import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer/channels")
public class ChannelController {

    @Autowired
    ChannelRepository channelRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Channel findById(@PathVariable String id) throws ChannelNotFoundException {
        Optional<Channel> channel = channelRepository.findById(id);
        if (channel.isEmpty()) {
            throw new ChannelNotFoundException();
        }
        return channel.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Channel createChannel(@Valid @RequestBody Channel channel) {
        return channelRepository.save(channel);
    }

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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteChannel(@PathVariable String id) throws ChannelNotFoundException {
        if(channelRepository.existsById(id)) channelRepository.deleteById(id);
        else throw new ChannelNotFoundException();
    }

}
