package com.project.mungfriend.service;

import com.project.mungfriend.dto.post.*;
import com.project.mungfriend.model.Dog;
import com.project.mungfriend.model.DogImageFile;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Post;
import com.project.mungfriend.repository.*;
import com.project.mungfriend.util.DistanceCalculator;
import com.project.mungfriend.util.DistanceComparator;
import com.project.mungfriend.util.TimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ReviewRepository reviewRepository;
    private final DogRepository dogRepository;
    private final ApplyRepository applyRepository;

    //게시글 등록
    @Transactional
    public RegisterPostResponseDto registerPost(String username,
                                                RegisterPostRequestDto requestDto) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("게시글 작성은 로그인 후 가능합니다.")
        );

        // 게시글 등록 시 요청 시작 및 종료 일자 유효성 체크
        TimeValidator.validateTime(requestDto.getRequestStartDate(), requestDto.getRequestEndDate());

        for (Long dogId : requestDto.getDogIdList()) {
            if(!dogRepository.existsById(dogId)){
                throw new NullPointerException("선택한 ID의 멍멍이가 존재하지 않습니다.");
            }
        }

        Post post = new Post(requestDto);
        post.setMember(member);
        postRepository.save(post);

        RegisterPostResponseDto registerPostResponseDto = new RegisterPostResponseDto();
        registerPostResponseDto.ok();
        return registerPostResponseDto;
    }

    //게시글 전체 조회
    public List<GetPostResponseDto> getAllPosts() {
        List<GetPostResponseDto> postResponseDtoList = new ArrayList<>();
        List<Post> postList = postRepository.findAllByOrderByRequestStartDate();

        for (Post post : postList) {
            GetPostResponseDto getPostResponseDto = new GetPostResponseDto(post);
            // 게시글 전체 조회, 거리순 조회 시 현재 시간을 기준으로 요청 시간이 지났으면 모집 종료로 바꿔주기
            if ( post.getRequestStartDate().isBefore(LocalDateTime.now()) ){
                post.setIsComplete(true);
                postRepository.save(post);
            }
            getPostResponseDto.setIsComplete(post.getIsComplete());


            // 신청자 수 세팅. 2022-06-28 인기천 추가.
            setApplyCntAndImgPath(post, getPostResponseDto);
            postResponseDtoList.add(getPostResponseDto);
        }
        return postResponseDtoList;
    }



    // 게시글 가까운 거리순 조회
    public List<GetPostResponseDto> getPostsByCalcDistance(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("가까운 거리순 게시글 조회는 로그인 후 사용 가능합니다.")
        );

        List<GetPostResponseDto> postResponseDtoList = new ArrayList<>();
        List<Post> postList = postRepository.findAll();

        for (Post post : postList){
            GetPostResponseDto getPostResponseDto = new GetPostResponseDto(post);

            // 로그인한 유저의 위치와 게시글 작성자의 거리를 계산하여 같이 저장한다.
            getPostResponseDto.setDistance(DistanceCalculator.calcDistance(
                    Double.parseDouble(member.getLatitude()),
                    Double.parseDouble(member.getLongitude()),
                    Double.parseDouble(post.getLatitude()),
                    Double.parseDouble(post.getLongitude()), "kilometer"));

            getPostResponseDto.setIsComplete(post.getIsComplete());

            // 신청자 수 세팅. 2022-06-28 인기천 추가.
            setApplyCntAndImgPath(post, getPostResponseDto);
            postResponseDtoList.add(getPostResponseDto);
        }
        // util에 정의한 DistanceComparator를 활용하여 정렬하기 (가까운 것이 맨 위)
        postResponseDtoList.sort(new DistanceComparator());
        return postResponseDtoList;
    }



    //게시글 상세 조회
    public GetPostDetailResponseDto getPostDetail(Long id, String username) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 ID의 게시글이 존재하지 않습니다.")
        );

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("게시글 상세 조회는 로그인 후 가능합니다.")
        );

        GetPostDetailResponseDto responseDto = new GetPostDetailResponseDto(post);

        // 220715 추가. 매칭된 사람의 프로필 전달하기 위한 로직.
        if(post.getMatchedApplicantId() != null) {
            Member matchedApplicant = memberRepository.findById(post.getMatchedApplicantId()).orElseThrow(
                    () -> new NullPointerException("해당하는 ID가 없습니다.")
            );
            responseDto.setMatchedNickname(matchedApplicant.getNickname());
            responseDto.setMatchedDogProfileImgUrl(matchedApplicant.getDogProfileImgUrl());
        }

        responseDto.setDogProfileImgUrl(post.getMember().getDogProfileImgUrl());

        // 로그인한 사용자와 게시글 작성자 간 거리 (단위: km)
        responseDto.setDistance(DistanceCalculator.calcDistance(
                Double.parseDouble(member.getLatitude()),
                Double.parseDouble(member.getLongitude()),
                Double.parseDouble(post.getLatitude()),
                Double.parseDouble(post.getLongitude()), "kilometer"));

        // 신청자 수, 내가 신청했는지 아닌지 여부 판단 추가. 2022-06-28 인기천.
        Long applyCount = applyRepository.countByPostId(post.getId());
        Boolean applyByMe = applyRepository.existsByApplicantIdAndPostId(member.getId(), post.getId());
        responseDto.setApplyCount(applyCount);
        responseDto.setApplyByMe(applyByMe);

        // 작성자 입장에서 상세 게시글 조회하는 경우에만 유효한 값
        Boolean reviewByMe = reviewRepository.existsByPostIdAndGiverId(id, member.getId());
        if (reviewByMe) {
            responseDto.setReviewCount(1L);
        }

        responseDto.getApplyList().addAll(post.getApplyList());

        String dogProfileIds = post.getDogProfileIds();
        String[] dogProfileIdsArr = dogProfileIds.split(",");
        for (String s : dogProfileIdsArr) {
            dogRepository.findById(Long.parseLong(s)).ifPresent(
                    dog -> responseDto.getDogList().add(dog));
        }

        return responseDto;
    }

    //게시글 수정
    @Transactional
    public PutPostResponseDto updatePost(Long id, PutPostRequestDto requestDto, String username) {
        // TimeValidator 추가
        TimeValidator.validateTime(requestDto.getRequestStartDate(), requestDto.getRequestEndDate());

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 ID의 게시글이 존재하지 않습니다.")
        );
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("게시글 수정은 로그인 후 가능합니다.")
        );
        if(!member.getNickname().equals(post.getMember().getNickname())){
            throw new IllegalArgumentException("본인의 게시글만 수정할 수 있습니다.");
        }

        post.updatePost(requestDto);
        postRepository.save(post);

        PutPostResponseDto responseDto = new PutPostResponseDto();
        responseDto.ok();
        return responseDto;
    }

    //게시글 삭제
    @Transactional
    public DeletePostResponseDto deletePost(Long id, String username) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 ID의 게시글이 존재하지 않습니다.")
        );
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("게시글 삭제는 로그인 후 가능합니다.")
        );

        if(!member.getNickname().equals(post.getMember().getNickname())){
            throw new IllegalArgumentException("본인의 게시글만 삭제할 수 있습니다.");
        }
        
        postRepository.deleteById(post.getId());

        DeletePostResponseDto responseDto = new DeletePostResponseDto();
        responseDto.ok();
        return responseDto;
    }

    // 신청 수 계산과 이미지 url 저장하는 로직은 리팩터링
    private void setApplyCntAndImgPath(Post post, GetPostResponseDto getPostResponseDto) {
        Long applyCount = applyRepository.countByPostId(post.getId());
        getPostResponseDto.setApplyCount(applyCount);

        List<String> imagePath = getPostResponseDto.getImagePath();

        String dogProfileIds = post.getDogProfileIds();
        String[] dogProfileIdsArr = dogProfileIds.split(",");
        for (String s : dogProfileIdsArr) {
            Dog dog = dogRepository.findById(Long.parseLong(s)).orElse(null);
            // 강아지가 없을 경우 Exception 발생시키는 로직 수정 -> 없으면 그냥 넘어가기
            if(dog != null){
                DogImageFile dogImageFile = dog.getDogImageFiles().get(0);
                String imageUrl = dogImageFile.getImageUrl();
                imagePath.add(imageUrl);
            }
        }
    }
}
