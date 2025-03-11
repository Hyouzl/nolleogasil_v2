package com.fourroro.nolleogasil_backend.controller.users;

import com.fourroro.nolleogasil_backend.auth.jwt.util.TokenProvider;
import com.fourroro.nolleogasil_backend.dto.users.KakaoDto;
import com.fourroro.nolleogasil_backend.dto.users.UsersDto;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.service.users.UsersServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 이 클래스는 회원 정보 관리를 위한 컨트롤러입니다.
 * @author 장민정
 * @author 박초은 - getUsersInfo()
 * @since 2024-01-05
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UsersController {
    private final UsersServiceImpl usersService;
    private final TokenProvider tokenProvider;

    //회원가입 및 로그인
    @PostMapping("/profile")
    public ResponseEntity<Long> setUserProfile(HttpSession session, @RequestBody KakaoDto kakaoRequest){
        try{
            //카카오로부터 받은 사용자 정보 中 phone_number
            String kakaoUsersPhone = kakaoRequest.getPhone();
            String kakaoUsersEmail = kakaoRequest.getEmail();
            //기존 회원 여부 확인
            boolean isDuplicate = usersService.validateDuplicateUsers(kakaoRequest.toDto());

            if(isDuplicate) { //기존 회원인 경우
                Users existingUsers = usersService.findByEmail(kakaoUsersEmail);
                String existingPhone = existingUsers.getPhone();

                //전화번호가 변경되었으면
                if(!existingPhone.equals(kakaoUsersPhone)){
                    usersService.validateDuplicatePhoneAndUpdate(kakaoUsersEmail, kakaoUsersPhone);
                }

                UsersDto usersDto = UsersDto.changeToDto(existingUsers);

                //세션에 사용자 정보 저장
                session.setAttribute("users", usersDto);

                //프론트엔드로 기존 회원임을 전달
                return ResponseEntity.ok().body(usersDto.getUsersId());
            }else { //신규 회원인 경우
                usersService.insertUsers(kakaoRequest.toDto());
                Users users = usersService.findByEmail(kakaoRequest.getEmail());
                UsersDto usersDto = UsersDto.changeToDto(users);

                //세션에 사용자 정보 저장
                session.setAttribute("users", usersDto);

                //프론트엔드로 신규 회원임을 전달
                return ResponseEntity.status(HttpStatus.CREATED).body(-1L);
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Long> commonLogin(HttpSession session, @RequestParam("email") String email){
        try{
            Users existingUsers = usersService.findUsersByEmail(email);

            if(existingUsers != null){ //기존 회원
                UsersDto usersDto = UsersDto.changeToDto(existingUsers);
                session.setAttribute("users", usersDto);

                return ResponseEntity.ok().body(usersDto.getUsersId());
            }else{ //신규 회원
                return ResponseEntity.status(HttpStatus.CREATED).body(-1L); // 201 Created로 회원가입 신호
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Long> commonRegister(HttpSession session, @RequestBody UsersDto registeredUser){
        try{
            usersService.insertUsers(registeredUser);
            Users users = usersService.findUsersByEmail(registeredUser.getEmail());
            UsersDto usersDto = UsersDto.changeToDto(users);
            //세션에 사용자 정보 저장
            session.setAttribute("users", usersDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(usersDto.getUsersId());
        }catch(Exception e){
            //예외 발생 시 BAD_REQUEST와 함께 -1L 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1L);
        }
    }

/*
    @GetMapping("/callback")
    public KakaoDto getKakaoAccount(@RequestParam("code") String code){
        log.debug("code = {}", code);
        return kakaoService.getInfo(code).getKakaoDto();
    }
*/

    @GetMapping("/info")
    public ResponseEntity<UsersDto> getUserInfo(@RequestParam Long usersId){
        try {
            Users users = usersService.findByUsersId(usersId);
            UsersDto usersDto = UsersDto.changeToDto(users);
            return ResponseEntity.ok(usersDto);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    //회원정보 수정
    @PatchMapping("/info/{usersId}")
    public ResponseEntity<UsersDto> updateForm(@PathVariable Long usersId,
                                               @RequestParam(name = "nickname") String nickname){

        try {
            usersService.updateUsers(nickname, usersId);
            Users users = usersService.findByUsersId(usersId);
            UsersDto usersDto = UsersDto.changeToDto(users);
            if (usersDto != null) { //회원 정보 존재 시
                return ResponseEntity.ok(usersDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/info/{usersId}")
    public ResponseEntity<String> updateUsers(HttpSession session, @PathVariable Long usersId, @RequestBody Map<String, String> requestBody, HttpServletRequest request){
        try{
            String nickname = requestBody.get("nickname");

            usersService.updateUsers(nickname, usersId);

            // 세션에 저장된 사용자 정보 업데이트
            if(session.getAttribute("users") != null) {
                UsersDto userInfo = (UsersDto) session.getAttribute("users");

                if(userInfo != null) {
                    userInfo.setNickname(nickname);

                    // 세션 무효화
                    session.invalidate();
                    // 새로운 세션 생성 및 사용자 정보 설정
                    HttpSession newSession = request.getSession(true);
                    newSession.setAttribute("users", userInfo);
                }
            }else{
                System.out.println("session is empty!");
            }

            return ResponseEntity.ok("User info update successfully!");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * @author 박초은
     * 맛집메이트의 게시자의 사용자 정보 조회 or 멤버의 사용자 정보 조회
     *
     * @param usersId 조회할 사용자 ID
     * @return 조회된 사용자 정보를 포함한 HTTP 상태 코드가 200인 ResponseEntity 객체,
     *         서버 오류 발생 시 HTTP 상태 코드가 500인 ResponseEntity 객체
     */
    @GetMapping("/{usersId}/info")
    public ResponseEntity<UsersDto> getUsersInfo(@PathVariable Long usersId) {
        try {
            Users users = usersService.findByUsersId(usersId);
            UsersDto usersDto = UsersDto.changeToDto(users);
            return ResponseEntity.ok(usersDto);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //로그아웃
    @RequestMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) throws Exception{
        //세션에서 사용자 정보 제거
        if(session != null) {
            //세션 무효화
            session.invalidate();

            return ResponseEntity.ok("Logout successful");
        }else{
            return ResponseEntity.ok("No active session found");
        }
    }

    //회원탈퇴
    @DeleteMapping("/delete/{usersId}")
    public ResponseEntity<String> deleteUsers(HttpSession session, @PathVariable Long usersId){
        usersService.deleteUsers(usersId);
        //세션 무효화
        session.invalidate();
        return ResponseEntity.ok("User delete successfully");
    }
}