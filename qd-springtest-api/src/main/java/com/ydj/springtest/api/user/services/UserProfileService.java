package com.ydj.springtest.api.user.services;

import com.ydj.springtest.api.CommonResponse;
import com.ydj.springtest.api.user.dto.UserProfileDTO;
import com.ydj.springtest.api.user.dto.UserSearchRequest;

import java.util.List;

public interface UserProfileService {

    /**
     * 查询用户profile
     *
     * @param id
     * @return
     */
    CommonResponse<UserProfileDTO> getUserProfileById(Integer id);

    /**
     * 用户搜索
     *
     * @param request
     * @return
     */
    CommonResponse<List<UserProfileDTO>> userSearch(UserSearchRequest request);
}
