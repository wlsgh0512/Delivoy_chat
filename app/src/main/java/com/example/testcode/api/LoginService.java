package com.example.testcode.api;

import com.example.testcode.model.Add_Friends_Response;
import com.example.testcode.model.Change_roomTitle;
import com.example.testcode.model.Chat_Response;
import com.example.testcode.model.Create_chat_Response;
import com.example.testcode.model.Chat_invite_room_Response;
import com.example.testcode.model.Chat_room_Response;
import com.example.testcode.model.Delete_Room_Response;
import com.example.testcode.model.FriendsResponse;
import com.example.testcode.model.Join_Response;
import com.example.testcode.model.LoginResponse;
import com.example.testcode.model.PollingChat_Response;
import com.example.testcode.model.Secession_Response;
import com.example.testcode.model.Send_msg_Response;
import com.example.testcode.model.User_Consent_Response;
import com.example.testcode.model.User_Response;
import com.example.testcode.model.User_listup_Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {
    /**
     * 0x100 Login 관리
     */
    @GET("login/login_no_get.php")  // 회원번호 Login
    Call<LoginResponse> oldLogin(@Query("ucAreaNo") String ucAreaNo,
                                 @Query("ucDistribId") String ucDistribId,
                                 @Query("ucAgencyId") String ucAgencyId,
                                 @Query("ucMemCourId") String ucMemCourId,
                                 @Query("acPassword") String acPassword);

    @GET("login/login_id_get.php")  // 사용자 ID Login
    Call<LoginResponse> idLogin(@Query("acUserId") String acUserId,
                                @Query("acPassword") String acPassword);

    @GET("login/find_id_get.php")   // ID 찾기
    Call<LoginResponse> findId(@Query("acRealName") String acRealName,
                               @Query("acCellNo") String acCellNo);

    @GET("login/find_pw_get.php")   // PW 찾기
    Call<LoginResponse> findPw(@Query("ucAreaNo") String ucAreaNo,
                               @Query("ucDistribId") String ucDistribId,
                               @Query("ucAgencyId") String ucAgencyId,
                               @Query("ucMemCourId") String ucMemCourId,
                               @Query("acUserId") String acUserId,
                               @Query("acRealName") String acRealName,
                               @Query("acEmailAddress") String acEmailAddress,
                               @Query("acCellNo") String acPassword);

    /**
     * 0x200 User 관리
     */
    @FormUrlEncoded
    @POST("users/user_post.php")    // 신규 가입
    Call<Join_Response> join(@Field("acUserId") String acUserId,
                             @Field("acPassword") String acPassword,
                             @Field("acRealName") String acRealName,
                             @Field("acNickName") String acNickName,
                             @Field("acCellNo") String acCellNo,
                             @Field("acEmailAddress") String acEmailAddress,
                             @Field("ucAccessFlag") String ucAccessFlag);

    @FormUrlEncoded
    @POST("users/user_put.php")     // 정보 수정
    Call<Join_Response> change(@Field("ucAreaNo") String ucAreaNo,
                               @Field("ucDistribId") String ucDistribId,
                               @Field("ucAgencyId") String ucAgencyId,
                               @Field("ucMemCourId") String ucMemCourId,
                               @Field("acNickName") String acNickName,
                               @Field("acCellNo") String acCellNo,
                               @Field("acEmailAddress") String acEmailAdd,
                               @Field("ucAccessFlag") String ucAccessFlag);

    @FormUrlEncoded
    @POST("users/user_put.php")     // PW 수정
    Call<Join_Response> changepw(@Field("ucAreaNo") String ucAreaNo,
                                      @Field("ucDistribId") String ucDistribId,
                                      @Field("ucAgencyId") String ucAgencyId,
                                      @Field("ucMemCourId") String ucMemCourId,
                                      @Field("acUserId") String acUserId,
                                      @Field("acPassword") String acPassword,
                                      @Field("acRealName") String acRealName);

    @FormUrlEncoded
    @POST("users/user_agree.php")     // 동의서
    Call<User_Consent_Response> agree(@Field("ucAreaNo") String ucAreaNo,
                                      @Field("ucDistribId") String ucDistribId,
                                      @Field("ucAgencyId") String ucAgencyId,
                                      @Field("ucMemCourId") String ucMemCourId,
                                      @Field("ucAgreeOption") String ucAgreeOption,
                                      @Field("ucThirdPartyOption") String ucThirdPartyOption);

    @FormUrlEncoded
    @POST("users/user_delete.php")     // 회원 탈퇴
    Call<Secession_Response> secession(@Field("ucAreaNo") String ucAreaNo,
                                       @Field("ucDistribId") String ucDistribId,
                                       @Field("ucAgencyId") String ucAgencyId,
                                       @Field("ucMemCourId") String ucMemCourId,
                                       @Field("acUserId") String acUserId,
                                       @Field("acPassword") String acPassword,
                                       @Field("acRealName") String acRealName);

    @GET("users/user_fetch.php")        // 사용자 모두 조회
    Call<User_Response> all_user(@Query("ucAreaNo") String ucAreaNo,
                                 @Query("ucDistribId") String ucDistribId,
                                 @Query("ucAgencyId") String ucAgencyId,
                                 @Query("ucMemCourId") String ucMemCourId);

    /**
     * 0x300 Room 관리
     */
    @FormUrlEncoded
    @POST("rooms/room_post.php")        // 새로운 채팅방 개설
    Call<Create_chat_Response> Create_chat(@Field("acRoomTitle") String acRoomTitle,
                                           @Field("acImageUrl") String acImageUrl);

    @FormUrlEncoded
    @POST("rooms/room_put.php")         // 새로운 채팅방 개설
    Call<Change_roomTitle> change(@Field("uiRoomNo") String uiRoomNo,
                                  @Field("acRoomTitle") String acRoomTitle,
                                  @Field("acImageUrl") String acImageUrl);

    @FormUrlEncoded
    @POST("rooms/room_delete.php")      // 기존 채팅방 삭제
    Call<Delete_Room_Response> delete(@Field("uiRoomNo") String uiRoomNo);

    @GET("rooms/room_fetch.php")        // 모임에 참여중인 사용자 모두 조회
    Call<User_listup_Response> list_up(@Query("uiRoomNo") String uiRoomNo);



    /**
     * 0x400 Group 관리
     */
    @FormUrlEncoded
    @POST("groups/group_post.php")      // 채팅방에 새로운 사용자 추가
    Call<Chat_invite_room_Response> room_invite(@Field("uiRoomNo") String uiRoomNo,
                                                @Field("ucAreaNo") String ucAreaNo,
                                                @Field("ucDistribId") String ucDistribId,
                                                @Field("ucAgencyId") String ucAgencyId,
                                                @Field("ucMemCourId") String ucMemCourId);

    @FormUrlEncoded
    @POST("groups/group_delete.php")      // 채팅방에 포함된 사용자 삭제
    Call<Chat_invite_room_Response> delete_user(@Field("uiRoomNo") String uiRoomNo,
                                                @Field("ucAreaNo") String ucAreaNo,
                                                @Field("ucDistribId") String ucDistribId,
                                                @Field("ucAgencyId") String ucAgencyId,
                                                @Field("ucMemCourId") String ucMemCourId);

    @GET("groups/group_fetch.php")      // 내가 포함된 모임 (방) 모두 조회
    Call<Chat_room_Response> rooms(@Query("ucAreaNo") String ucAreaNo,
                                   @Query("ucDistribId") String ucDistribId,
                                   @Query("ucAgencyId") String ucAgencyId,
                                   @Query("ucMemCourId") String ucMemCourId);

    /**
     * 0x500 Friend 관리
     */
    @FormUrlEncoded
    @POST("friends/friend_post.php")    // 친구 추가하기
    Call<Add_Friends_Response> addfriends(@Field("ucAreaNo") String ucAreaNo,
                                          @Field("ucDistribId") String ucDistribId,
                                          @Field("ucAgencyId") String ucAgencyId,
                                          @Field("ucMemCourId") String ucMemCourId,
                                          @Field("ucFriAreaNo") String ucFriAreaNo,
                                          @Field("ucFriDistribId") String ucFriDistribId,
                                          @Field("ucFriAgencyId") String ucFriAgencyId,
                                          @Field("ucFriMemCourId") String ucFriMemCourId);

    @FormUrlEncoded
    @POST("friends/friend_delete.php")    // 친구 삭제하기
    Call<Add_Friends_Response> deletefriends(@Field("ucAreaNo") String ucAreaNo,
                                             @Field("ucDistribId") String ucDistribId,
                                             @Field("ucAgencyId") String ucAgencyId,
                                             @Field("ucMemCourId") String ucMemCourId,
                                             @Field("ucFriAreaNo") String ucFriAreaNo,
                                             @Field("ucFriDistribId") String ucFriDistribId,
                                             @Field("ucFriAgencyId") String ucFriAgencyId,
                                             @Field("ucFriMemCourId") String ucFriMemCourId);


    @GET("friends/friend_fetch.php")    // 나와 친구인 사용자 모두 조회
    Call<FriendsResponse> friend(@Query("ucAreaNo") String ucAreaNo,
                                 @Query("ucDistribId") String ucDistribId,
                                 @Query("ucAgencyId") String ucAgencyId,
                                 @Query("ucMemCourId") String ucMemCourId);

    /**
     * 0x600 Talk 관리
     */
    @FormUrlEncoded
    @POST("talks/talk_post.php")      // 채팅 톡 작성
    Call<Send_msg_Response> send_msg(@Field("uiRoomNo") String uiRoomNo,
                                     @Field("ucAreaNo") String ucAreaNo,
                                     @Field("ucDistribId") String ucDistribId,
                                     @Field("ucAgencyId") String ucAgencyId,
                                     @Field("ucMemCourId") String ucMemCourId,
                                     @Field("acTalkMesg") String acTalkMesg);

    @GET("talks/talk_get.php")          // 채팅 톡 조회 (1건 조회)
    Call<Chat_Response> chat(@Query("uiTalkNo") String uiTalkNo,
                             @Query("uiRoomNo") String uiRoomNo,
                             @Query("ucAreaNo") String ucAreaNo,
                             @Query("ucDistribId") String ucDistribId,
                             @Query("ucAgencyId") String ucAgencyId,
                             @Query("ucMemCourId") String ucMemCourId);

    @GET("talks/talk_fetch.php")        // 채팅 톡 조회 (1건 조회)
    Call<PollingChat_Response> pollingchat(@Query("uiTalkNo") int uiTalkNo,
                                           @Query("uiRoomNo") String uiRoomNo,
                                           @Query("ucAreaNo") String ucAreaNo,
                                           @Query("ucDistribId") String ucDistribId,
                                           @Query("ucAgencyId") String ucAgencyId,
                                           @Query("ucMemCourId") String ucMemCourId);
}
