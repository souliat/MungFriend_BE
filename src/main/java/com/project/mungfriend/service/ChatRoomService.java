package com.project.mungfriend.service;

import com.project.mungfriend.dto.chat.ChatRoomListDto;
import com.project.mungfriend.dto.chat.ChatRoomRequestDto;
import com.project.mungfriend.dto.chat.ChatRoomResponseDto;
import com.project.mungfriend.model.ChatMessage;
import com.project.mungfriend.model.ChatRoom;
import com.project.mungfriend.model.Member;
import com.project.mungfriend.repository.ChatMessageRepository;
import com.project.mungfriend.repository.ChatRoomRepository;
import com.project.mungfriend.repository.MemberRepository;
import com.project.mungfriend.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    //레디스 저장소 사용
    //key hashKey value 구조
    private final RedisTemplate<String, Object> redisTemplate;
//    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Long> hashOpsEnterInfo;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageService chatMessageService;
    private final MemberRepository memberRepository;
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId 와 채팅룸 id 를 맵핑한 정보 저장

    @PostConstruct
    private void init() {
        hashOpsEnterInfo = redisTemplate.opsForHash();
    }

    // 유저가 입장한 채팅방 ID 와 유저 세션 ID 맵핑 정보 저장
    // Enter라는 곳에 sessionId와 roomId를 맵핑시켜놓음
    public void setUserEnterInfo(String sessionId, Long roomId) {
        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(String.class));
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Long.class));
//        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Long.class));
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public Long getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션정보와 맵핑된 채팅방 ID 삭제
    //한 유저는 하나의 룸 아이디에만 맵핑되어있다!
    // 실시간으로 보는 방은 하나이기 떄문이다.
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    // destination 정보에서 roomId 추출(String형)
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }


    // 채팅방 생성
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto requestDto) {
        String username = SecurityUtil.getCurrentMemberUsername();
        Member writer = memberRepository.findByUsername(username).orElse(null);

        Member applicant = memberRepository.findByNickname(requestDto.getNickname()).orElseThrow(
                () -> new NullPointerException("해당하는 신청자를 찾을 수 없습니다.")
        );

        boolean isExist = false;
        List<ChatRoom> writerChatRoomList = chatRoomRepository.findAllByMemberListIsContaining(writer);
        for (ChatRoom chatRoom : writerChatRoomList) {
            isExist = chatRoom.getMemberList().contains(applicant);
        }

        if (isExist){
            return null;
        }

        assert writer != null;
        ChatRoom chatRoom = new ChatRoom(writer, applicant);
        chatRoomRepository.save(chatRoom);

        return new ChatRoomResponseDto(chatRoom, writer);
    }

    // 전체 채팅방 조회
    public List<ChatRoomListDto> getAllChatRooms(Member member) {

        List<ChatRoomListDto> userChatRoom = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomRepository.findAllByOrderByCreatedAtDesc()) {
            if(chatRoom.getMemberList().contains(member)){
                userChatRoom.add(new ChatRoomListDto(chatRoom, chatRoom.getMemberList().get(0)));
            }
        }
        return userChatRoom;
    }

    // 특정 채팅방 조회
    public ChatRoomResponseDto showChatRoom(Long roomId, Member member) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("찾는 채팅방이 존재하지 않습니다.")
        );

        return new ChatRoomResponseDto(chatRoom, member);
    }

    // 특정 채팅방 삭제
    // 삭제한 사람만 삭제되고, 남은 사람에겐 ~님이 나갔습니다. 띄우기
    @Transactional
    public boolean deleteChatRoom(Long roomId){
        String username = SecurityUtil.getCurrentMemberUsername();
        Member loginMember = memberRepository.findByUsername(username).orElse(null);

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new NullPointerException("해당하는 채팅방이 없습니다."));

        // 나가기 누른 사람은 채팅룸의 멤버리스트에서 제거
        chatRoom.getMemberList().remove(loginMember);

        // 현재 채팅룸에 남은 사람이 아무도 없다면 채팅룸 객체를 아예 삭제
        if(chatRoom.getMemberList().size() == 0){
            // 채팅방 삭제 전 채팅 메세지 삭제
            chatMessageRepository.delete(roomId);
            // 채팅방 삭제
            chatRoomRepository.deleteById(roomId);
        }else{
            // 한명이라도 남아있다면 현재 나가기 누른 사람의 퇴장 메세지를 보내줌
            assert loginMember != null;
            chatMessageService.sendChatMessage(
                    ChatMessage.builder()
                            .type(ChatMessage.MessageType.QUIT)
                            .roomId(roomId)
                            .sender(loginMember.getNickname())
                            .build()
            );
        }
        return true;
    }
}
