package com.project.mungfriend.service;

import com.project.mungfriend.dto.DogCaptainResponseDto;
import com.project.mungfriend.dto.DogProfileRequestDto;
import com.project.mungfriend.dto.DogProfileResponseDto;
import com.project.mungfriend.model.Dog;
import com.project.mungfriend.model.DogImageFile;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.repository.DogImageFileRepository;
import com.project.mungfriend.repository.DogRepository;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;
    private final DogImageFileRepository dogImageFileRepository;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    // 멍 프로필 등록
    public DogProfileResponseDto addProfile(MultipartFile multipartFile,
                                            DogProfileRequestDto requestDto) throws IOException {

        String username = SecurityUtil.getCurrentMemberUsername();
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당하는 ID의 회원이 존재하지 않습니다."));

        // 멍 프로필이 이미 3개가 등록되었을 경우
        if(member.getDogList().size() == 3) {
            return new DogProfileResponseDto("false", "멍 프로필 등록은 최대 3개까지만 가능합니다.");
        }

        // 처음 멍프로필 등록 시 첫 강아지가 자동으로 대표강아지로 등록
        if(member.getDogList().size() == 0) {
            requestDto.setIsRepresentative(true);
        }

        // Dog 엔티티에 image 빼고 정보 저장
        Dog dog = new Dog(requestDto);
        dog.setMember(member); // 연관관계 편의 메소드
        dogRepository.save(dog);

        // DogImageFile 엔티티에 받은 image 저장
        String imageUrl = s3Uploader.upload(multipartFile, "static");
        System.out.println("S3 업로드된 이미지 경로 : " + imageUrl);
        DogImageFile dogImageFile = new DogImageFile(imageUrl);
        dogImageFile.setDog(dog); // 연관관계 편의 메소드
        dogImageFileRepository.save(dogImageFile);

        return new DogProfileResponseDto("true", "멍멍이 등록 성공!");
    }

    // 멍 프로필 전체 조회
    public List<Dog> getAllProfiles() {

        String username = SecurityUtil.getCurrentMemberUsername();
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당하는 ID의 회원이 존재하지 않습니다."));

        return member.getDogList();
    }

    // 멍 프로필 삭제
    public DogProfileResponseDto deleteProfile(Long id) {
        Dog dog = dogRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 되는 멍 프로필 id가 없습니다."));

        dogRepository.deleteById(dog.getId());
        return new DogProfileResponseDto("true", "멍 프로필이 삭제 되었습니다.");
    }

    // 대표 멍멍이 선택
    public DogCaptainResponseDto selectCaptainDog(Long id) {
        String username = SecurityUtil.getCurrentMemberUsername();
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당하는 ID의 회원이 존재하지 않습니다."));

        List<Dog> dogList = member.getDogList();

        for (Dog dog : dogList) {
            if(dog.getId().equals(id)) {
                dog.setRepresentative(true);
                dogRepository.save(dog);
            }else {
                dog.setRepresentative(false);
                dogRepository.save(dog);
            }
        }

        return new DogCaptainResponseDto("true", "대표 멍멍이가 바뀌었습니다.");
    }
}
