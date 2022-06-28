package com.project.mungfriend.controller;

import com.project.mungfriend.dto.DogProfileRequestDto;
import com.project.mungfriend.dto.DogProfileResponseDto;
import com.project.mungfriend.model.Dog;
import com.project.mungfriend.service.DogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DogController {

    private final DogService dogService;

    // 멍 프로필 등록
    @PostMapping("/api/dogs")
    public DogProfileResponseDto addProfile(@RequestPart(value="image") MultipartFile multipartFile,
                                            @RequestPart(value="infos") DogProfileRequestDto requestDto) throws IOException {

        return dogService.addProfile(multipartFile, requestDto);
    }

    // 멍 프로필 전체조회
    @GetMapping("/api/dogs")
    public List<Dog> getAllProfiles() {
        return dogService.getAllProfiles();
    }

    // 멍 프로필 삭제
    @DeleteMapping("/api/dogs/{id}")
    public DogProfileResponseDto deleteProfile(@PathVariable Long id) {
        return dogService.deleteProfile(id);
    }
}
