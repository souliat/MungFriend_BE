package com.project.mungfriend.service;

import com.project.mungfriend.dto.GetPostDetailResponseDto;
import com.project.mungfriend.dto.GetPostResponseDto;
import com.project.mungfriend.dto.RegisterPostRequestDto;
import com.project.mungfriend.dto.RegisterPostResponseDto;
import com.project.mungfriend.model.Dog;
import com.project.mungfriend.model.DogImageFile;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.model.Post;
import com.project.mungfriend.repository.DogRepository;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.repository.PostRepository;
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

    public GetPostDetailResponseDto getPostDetail(Long id, String username) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 ID의 게시글이 존재하지 않습니다.")
        );

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("게시글 작성은 로그인 후 가능합니다.")
        );

        GetPostDetailResponseDto responseDto = new GetPostDetailResponseDto(post);
        responseDto.getApplyList().addAll(post.getApplies());
        responseDto.getDogList().addAll(member.getDogList());

        return responseDto;
    }
}
