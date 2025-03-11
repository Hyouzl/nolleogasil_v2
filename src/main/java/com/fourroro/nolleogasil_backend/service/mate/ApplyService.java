/**
 * 맛집메이트의 신청 관리를 위한 Service interface입니다.
 * @author 박초은
 * @since 2024-01-10
 */
package com.fourroro.nolleogasil_backend.service.mate;

import com.fourroro.nolleogasil_backend.dto.mate.ApplyDto;

import java.util.List;

public interface ApplyService {

    /** insert apply */
    public void insertApply(ApplyDto applyDto);

    /** 해당 apply의 isApply값 변경 */
    public void updateIsApply(ApplyDto applyDto);

    /** 보낸 신청 목록 조회 */
    public List<ApplyDto> getSendApplyList(Long applicantId);

    /** 받은 신청 목록 조회 */
    public List<ApplyDto> getReceiveApplyList(Long usersId);

    /** 1개의 apply 조회(applyId 이용) */
    public ApplyDto getApply(Long applyId);

    /** 1개의 apply 조회(mateId, applicantId(usersId) 이용) */
    public ApplyDto getApplyByMateIdAndUsersId(Long mateId, Long usersId);

    /** apply 유무 확인 */
    public boolean checkApplyColumn(Long mateId, Long usersId);

    /** apply 삭제 */
    public void deleteApply(Long applyId);

}
