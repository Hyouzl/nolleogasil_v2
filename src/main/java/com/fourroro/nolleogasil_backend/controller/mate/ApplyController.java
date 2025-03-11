package com.fourroro.nolleogasil_backend.controller.mate;

import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import com.fourroro.nolleogasil_backend.dto.mate.ApplyDto;
import com.fourroro.nolleogasil_backend.dto.mate.ApplyStatus;
import com.fourroro.nolleogasil_backend.dto.mate.MateDto;
import com.fourroro.nolleogasil_backend.dto.mate.MateMemberDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.service.chat.ChatRoomService;
import com.fourroro.nolleogasil_backend.service.mate.ApplyService;
import com.fourroro.nolleogasil_backend.service.mate.MateMemberService;
import com.fourroro.nolleogasil_backend.service.mate.MateService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/apply")
public class ApplyController {

    private final ApplyService applyService;
    private final MateService mateService;
    private final MateMemberService mateMemberService;
    private final ChatRoomService chatRoomService;
    private final TokenProvider tokenProvider;



    /**
     * 맛집메이트의 신청 버튼 클릭 시, 로그인한 사용자의 신청 정보 저장
     *
     * @param mateId //해당 맛집메이트 ID
     * @param session //현재 사용자의 세션 객체
     * @return HTTP 상태 코드가 201(신청 저장 성공 시), 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
    @PostMapping("/{mateId}")
    public ResponseEntity<Void> createApply(@PathVariable Long mateId, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (mateId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            // 1. JWT 토큰 추출
            String token = authorizationHeader.replace("Bearer ", "");

            // 2. 토큰에서 userId 추출
            Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

            ApplyDto applyDto = ApplyDto.builder()
                    .mateId(mateId)
                    .applicantId(userId)
                    .isApply(ApplyStatus.대기)
                    .build();

            applyService.insertApply(applyDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 받은 신청 목록에서 수락이나 거절 클릭 시, 해당 신청의 신청 상태(isApply 값) 변경
     *
     * @param applyId //해당 신청 ID
     * @param status //클릭한 신청 상태(수락 or 거절)
     * @return HTTP 상태 코드가 200(성공 시), 404(해당 신청을 찾지 못할 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
    @PatchMapping("/{applyId}")
    public ResponseEntity<Void> updateIsApply(@PathVariable Long applyId, @RequestParam String status) {
        try {
            ApplyDto applyDto = applyService.getApply(applyId);
            if (applyDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            //String타입의 status값을 ApplyStatus타입으로 변경
            ApplyStatus isApply = ApplyStatus.valueOf(status);
            applyDto.setIsApply(isApply);
            MateDto mateDto = MateDto.changeToDto(mateService.getMate(applyDto.getMateId()));

            if (status.equals("수락")) {
                //isApply 변경
                applyService.updateIsApply(applyDto);

                //MateMember에 추가
                Long chatroomId = chatRoomService.getChatRoomIdByMateId(applyDto.getMateId());
                MateMemberDto mateMemberDto = mateMemberService.creatMateMemberDto(mateDto, chatroomId, applyDto.getApplicantId());
                mateMemberService.insertMateMember(mateMemberDto);

            } else {  //status.equals("거절")
                //isApply 변경
                applyService.updateIsApply(applyDto);
            }
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 로그인한 사용자의 신청 상태 조회
     * Mate.js에서 사용 -> 신청 상태에 따라 신청 버튼 내 text 다르게 출력
     *
     * @param mateId 신청 상태를 조회할 맛집메이트 ID
     * @param session 현재 사용자의 세션 객체
     * @return 조회된 신청 상태를 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         조회된 신청 상태가 없을 시 HTTP 상태 코드가 204인 ResponseEntity 객체,
     *         서버 오류 발생 시 HTTP 상태 코드가 500인 ResponseEntity 객체
     */
    @GetMapping("/{mateId}/status")
    public ResponseEntity<ApplyStatus> checkApplyStatus(@PathVariable Long mateId, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. JWT 토큰 추출
            String token = authorizationHeader.replace("Bearer ", "");

            // 2. 토큰에서 userId 추출
            Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

            //apply 유무 확인
            boolean checkingApply = applyService.checkApplyColumn(mateId, userId);

            if (checkingApply) {
                ApplyDto applyDto = applyService.getApplyByMateIdAndUsersId(mateId, userId);
                return ResponseEntity.ok(applyDto.getIsApply());
            } else {
                //사용자가 해당 mate에 신청한 적이 없다면
                return ResponseEntity.noContent().build();
            }
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 로그인한 사용자의 보낸 신청 목록 조회
     *
     * @param session 현재 사용자의 세션 객체
     * @return 조회된 보낸 신청 목록을 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         서버 오류 발생 시 HTTP 상태 코드가 500인 ResponseEntity 객체
     */
    @GetMapping("/send")
    public ResponseEntity<List<ApplyDto>> getSendApplyList(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. JWT 토큰 추출
            String token = authorizationHeader.replace("Bearer ", "");

            // 2. 토큰에서 userId 추출
            Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

            List<ApplyDto> sendApplyList = applyService.getSendApplyList(userId);
            return ResponseEntity.ok(sendApplyList);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 로그인한 사용자의 받은 신청 목록 조회
     *
     * @param session 현재 사용자의 세션 객체
     * @return 조회된 받은 신청 목록을 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         서버 오류 발생 시 HTTP 상태 코드가 500인 ResponseEntity 객체
     */
    @GetMapping("/receive")
    public ResponseEntity<List<ApplyDto>> getReceiveApplyList(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // 1. JWT 토큰 추출
            String token = authorizationHeader.replace("Bearer ", "");

            // 2. 토큰에서 userId 추출
            Long userId = Long.valueOf(tokenProvider.getClaims(token).getSubject());

            List<ApplyDto> receiveApplyList = applyService.getReceiveApplyList(userId);
            return ResponseEntity.ok(receiveApplyList);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 신청 취소나 삭제 버튼 클릭 시, 신청 정보 삭제
     *
     * @param applyId 삭제할 신청 ID
     * @return HTTP 상태 코드가 204(삭제 성공 시), 400(잘못된 요청 시), 500(서버 오류 발생 시)인 ResponseEntity 객체
     */
    //해당 신청 삭제(및 취소)
    @DeleteMapping("/{applyId}")
    public ResponseEntity<Void> deleteApply(@PathVariable Long applyId) {
        try {
            if (applyId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            applyService.deleteApply(applyId);
            return ResponseEntity.noContent().build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}