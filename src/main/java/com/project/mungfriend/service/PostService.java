package com.project.mungfriend.service;

import com.project.mungfriend.dto.*;
import com.project.mungfriend.model.Dog;
import com.project.mungfriend.model.DogImageFile;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Post;
import com.project.mungfriend.repository.ApplyRepository;
import com.project.mungfriend.repository.DogRepository;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.PostRepository;
import com.project.mungfriend.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    private final DogRepository dogRepository;
    private final ApplyRepository applyRepository;

    //게시글 등록
    @Transactional
    public RegisterPostResponseDto registerPost(String username,
                                                RegisterPostRequestDto requestDto) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("게시글 작성은 로그인 후 가능합니다.")
        );
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

            // 신청자 수 세팅. 2022-06-28 인기천 추가.
            Long applyCount = applyRepository.countByPostId(post.getId());
            getPostResponseDto.setApplyCount(applyCount);

            List<String> imagePath = getPostResponseDto.getImagePath();

            String dogProfileIds = post.getDogProfileIds();
            String[] dogProfileIdsArr = dogProfileIds.split(",");
            for (String s : dogProfileIdsArr) {
                Dog dog = dogRepository.findById(Long.parseLong(s)).orElseThrow(
                        () -> new IllegalArgumentException("해당 ID의 멍멍이 프로필이 존재하지 않습니다.")
                );
                DogImageFile dogImageFile = dog.getDogImageFiles().get(0);
                String imageUrl = dogImageFile.getImageUrl();
                imagePath.add(imageUrl);
            }

            postResponseDtoList.add(getPostResponseDto);
        }
        return postResponseDtoList;
    }

    //게시글 상세 조회
    public GetPostDetailResponseDto getPostDetail(Long id, String username) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 ID의 게시글이 존재하지 않습니다.")
        );

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("게시글 작성은 로그인 후 가능합니다.")
        );


        GetPostDetailResponseDto responseDto = new GetPostDetailResponseDto(post);

        // 신청자 수, 내가 신청했는지 아닌지 여부 판단 추가. 2022-06-28 인기천.
        Long applyCount = applyRepository.countByPostId(post.getId());
        Boolean applyByMe = applyRepository.existsByApplicantIdAndPostId(member.getId(), post.getId());

        responseDto.setApplyCount(applyCount);
        responseDto.setApplyByMe(applyByMe);

        responseDto.getApplyList().addAll(post.getApplyList());

        String dogProfileIds = post.getDogProfileIds();
        String[] dogProfileIdsArr = dogProfileIds.split(",");
        for (String s : dogProfileIdsArr) {
            Dog dog = dogRepository.findById(Long.parseLong(s)).orElseThrow(
                    () -> new IllegalArgumentException("해당 ID의 멍멍이 프로필이 존재하지 않습니다.")
            );
            responseDto.getDogList().add(dog);
        }

        return responseDto;
    }

    //게시글 수정
    @Transactional
    public PutPostResponseDto updatePost(Long id, PutPostRequestDto requestDto, String username) {
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
}
