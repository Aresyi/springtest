package com.ydj.springtest.service.user.remote.impl;

import com.ydj.springtest.api.CommonResponse;
import com.ydj.springtest.api.user.dto.UserProfileDTO;
import com.ydj.springtest.api.user.dto.UserSearchRequest;
import com.ydj.springtest.api.user.services.UserProfileService;

import java.util.List;

public class UserProfileServiceImpl implements UserProfileService {

    @Override
    public CommonResponse<UserProfileDTO> getUserProfileById(Integer id) {
        return null;
    }

    @Override
    public CommonResponse<List<UserProfileDTO>> userSearch(UserSearchRequest request) {
        return null;
    }


}
