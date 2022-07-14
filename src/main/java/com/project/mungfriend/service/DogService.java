package com.project.mungfriend.service;

import com.project.mungfriend.dto.dog.DogCaptainResponseDto;
import com.project.mungfriend.dto.dog.DogProfileRequestDto;
import com.project.mungfriend.dto.dog.DogProfileResponseDto;
import com.project.mungfriend.model.*;
import com.project.mungfriend.repository.DogImageFileRepository;
import com.project.mungfriend.repository.DogRepository;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
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

        // 로그인한 사용자의 대표 멍멍이 프로필 사진 url set해주기
        if (dog.isRepresentative()){
            setDogProfileImgUrl(member, imageUrl);
        }
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

        String username = SecurityUtil.getCurrentMemberUsername();
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("해당하는 ID의 회원이 존재하지 않습니다."));

        // S3 버킷에서 등록했던 이미지 삭제
        String imageUrl = dog.getDogImageFiles().get(0).getImageUrl();
        String[] splitArray = imageUrl.split("com/");
        String key = splitArray[1];
        s3Uploader.deleteS3(key);

        // cascade = CascadeType.PERSIST, orphanRemoval = true 인 상황에서 @Transactional을 붙이고 테스트 완료
        // dog.getDogImageFiles().remove(0);

        dogRepository.deleteById(dog.getId());

        if(dog.isRepresentative()) {
            List<Dog> dogList = member.getDogList();
            if(dogList.size() > 0) {
                Dog representativeDog = dogList.get(0);
                representativeDog.setRepresentative(true);
                dogRepository.save(representativeDog);
                setDogProfileImgUrl(member, representativeDog.getDogImageFiles().get(0).getImageUrl());
            }else{
                member.setDogProfileImgUrl("");
                memberRepository.save(member);
            }
        }

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
                //대표 멍멍이 사진 변경
                setDogProfileImgUrl(member, dog.getDogImageFiles().get(0).getImageUrl());
            }else {
                dog.setRepresentative(false);
                dogRepository.save(dog);
            }
        }

        return new DogCaptainResponseDto("true", "대표 멍멍이가 바뀌었습니다.");
    }

    // 사용자의 대표 멍멍이 프로필 사진 등록 모듈화
    private void setDogProfileImgUrl(Member member, String imageUrl) {
        member.setDogProfileImgUrl(imageUrl);
        memberRepository.save(member);
        List<Apply> applyList = member.getApplyList();
        List<Review> giverReviews = member.getGiverReviews();
        for (Apply apply : applyList) {
            apply.setDogProfileImgUrl(imageUrl);
        }
        for(Review review : giverReviews){
            review.setGiverDogProfileImgUrl(imageUrl);
        }
    }
}
