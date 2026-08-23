package com.janick_mediadb.janick_mediaapi;

import com.janick_mediadb.janick_mediaapi.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JanickMediaapiApplication {

    @Resource
    FileStorageService fileStorageService;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

	static void main(String[] args) {
		SpringApplication.run(JanickMediaapiApplication.class, args);
	}

    @PostConstruct
    public void init() {
        fileStorageService.init();
    }

}
